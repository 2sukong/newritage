package com.newritage.app.ble

import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import com.newritage.app.data.BaselineData
import com.newritage.app.data.SensorReading
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.UUID

class BleManager(
    private val context: Context,
    private val onBaselineReceived: (BaselineData) -> Unit,
    private val onDataReceived: (SensorReading) -> Unit,
    private val onVibrationEvent: () -> Unit
) {
    // ⚠️ 기기와 맞춰야 하는 값 (하드웨어 쪽 펌웨어 코드에 있는 것과 똑같아야 함)
    private val DEVICE_NAME = "NewRitage_Device"
    private val SERVICE_UUID = UUID.fromString("0000xxxx-0000-1000-8000-00805f9b34fb")
    private val CHARACTERISTIC_UUID = UUID.fromString("0000yyyy-0000-1000-8000-00805f9b34fb")

    private var bluetoothGatt: BluetoothGatt? = null
    private val bluetoothAdapter: BluetoothAdapter =
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    private var scanner: BluetoothLeScanner? = null

    private var currentSessionId: Long = -1

    fun startScan() {
        scanner = bluetoothAdapter.bluetoothLeScanner
        scanner?.startScan(scanCallback)
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if (result.device.name == DEVICE_NAME) {
                scanner?.stopScan(this)
                connectToDevice(result.device)
            }
        }
    }

    private fun connectToDevice(device: BluetoothDevice) {
        bluetoothGatt = device.connectGatt(context, false, gattCallback)
    }

    private val gattCallback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            val characteristic = gatt.getService(SERVICE_UUID)
                ?.getCharacteristic(CHARACTERISTIC_UUID)

            characteristic?.let {
                gatt.setCharacteristicNotification(it, true)
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            val packet = characteristic.value
            handleIncomingPacket(packet)
        }
    }

    private fun handleIncomingPacket(packet: ByteArray) {
        if (packet.isEmpty()) return

        when (packet[0].toInt()) {
            0x01 -> onBaselineReceived(parseBaselinePacket(packet))
            0x02 -> onDataReceived(parseSensorPacket(packet))
            0x03 -> onVibrationEvent()
            else -> Log.w("BleManager", "알 수 없는 패킷 타입: ${packet[0]}")
        }
    }

    // ⚠️ 실제 패킷 구조(바이트 순서)를 알아야 정확히 채울 수 있음
    private fun parseBaselinePacket(packet: ByteArray): BaselineData {
        val buffer = ByteBuffer.wrap(packet, 1, packet.size - 1).order(ByteOrder.LITTLE_ENDIAN)
        val thumb = buffer.float
        val im = buffer.float
        val palm = buffer.float
        val overall = buffer.float

        return BaselineData(
            thumb = thumb,
            indexMiddle = im,
            palm = palm,
            overall = overall
        )
    }

    private fun parseSensorPacket(packet: ByteArray): SensorReading {
        val buffer = ByteBuffer.wrap(packet, 1, packet.size - 1).order(ByteOrder.LITTLE_ENDIAN)
        val timestamp = buffer.long
        val thumb = buffer.float
        val im = buffer.float
        val palm = buffer.float
        val overall = (thumb + im + palm) / 3f

        return SensorReading(
            sessionId = currentSessionId,
            timestamp = timestamp,
            thumb = thumb,
            indexMiddle = im,
            palm = palm,
            overall = overall
        )
    }

    fun setCurrentSession(sessionId: Long) {
        currentSessionId = sessionId
    }

    fun disconnect() {
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
    }
}
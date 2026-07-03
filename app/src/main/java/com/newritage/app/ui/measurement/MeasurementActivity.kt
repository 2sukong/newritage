package com.newritage.app.ui.measurement

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.newritage.app.R
import com.newritage.app.ble.BleManager
import com.newritage.app.data.SensorReading
import com.newritage.app.data.SessionDataHolder
import com.newritage.app.databinding.ActivityMeasurementBinding

class MeasurementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMeasurementBinding
    private lateinit var bleManager: BleManager

    private val handler = Handler(Looper.getMainLooper())
    private var measuring = false
    private var elapsedSeconds = 0

    // 기존: Float 하나만 저장 → 이제 SensorReading 통째로 저장 (부위별 값 포함)
    private val sensorReadings = mutableListOf<SensorReading>()
    private val chartEntries = mutableListOf<Entry>()
    private var vibrationCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeasurementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupChart()
        setupBle()
        showGuideDialog()

        binding.btnBack.setOnClickListener { finish() }
        binding.btnStop.setOnClickListener { stopMeasurement() }
    }

    private fun setupBle() {
        bleManager = BleManager(
            context = this,
            onBaselineReceived = { },  // 이 화면에서는 안 씀
            onDataReceived = { reading ->
                if (measuring) {
                    sensorReadings.add(reading)

                    runOnUiThread {
                        val min = elapsedSeconds / 60
                        val sec = elapsedSeconds % 60
                        binding.tvTimer.text = String.format("%02d:%02d", min, sec)
                        binding.tvCurrentPressure.text = String.format("%.1f", reading.overall)

                        chartEntries.add(Entry(elapsedSeconds.toFloat(), reading.overall))
                        updateChart()
                    }
                }
            },
            onVibrationEvent = { vibrationCount++ }
        )
        bleManager.startScan()
    }

    private fun showGuideDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_measurement_guide, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<View>(R.id.btnStartGuide).setOnClickListener {
            dialog.dismiss()
            startMeasurement()
        }
        dialog.show()
    }

    private fun setupChart() {
        binding.lineChart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(false)
            setBackgroundColor(Color.TRANSPARENT)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = Color.parseColor("#5A6B5A")
            xAxis.setDrawGridLines(false)
            axisLeft.textColor = Color.parseColor("#5A6B5A")
            axisLeft.axisMinimum = 0f
            axisLeft.axisMaximum = 80f
            axisRight.isEnabled = false
        }
    }

    private fun startMeasurement() {
        measuring = true
        elapsedSeconds = 0
        sensorReadings.clear()
        chartEntries.clear()
        vibrationCount = 0
        binding.btnStop.isEnabled = true
        timerLoop.run()
    }

    // 기존 measureLoop(가짜 값 생성)에서 타이머 기능만 남긴 버전
    // 실제 압력 값은 setupBle()의 onDataReceived에서 옴
    private val timerLoop = object : Runnable {
        override fun run() {
            if (!measuring) return
            elapsedSeconds++
            handler.postDelayed(this, 1000L)
        }
    }

    private fun updateChart() {
        val visibleEntries = if (chartEntries.size > 60) {
            chartEntries.takeLast(60)
        } else {
            chartEntries.toList()
        }

        val dataSet = LineDataSet(visibleEntries, "압력").apply {
            color = Color.parseColor("#8B9E7B")
            setDrawCircles(false)
            lineWidth = 2f
            setDrawFilled(true)
            fillColor = Color.parseColor("#8B9E7B")
            fillAlpha = 50
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }
        binding.lineChart.data = LineData(dataSet)
        binding.lineChart.notifyDataSetChanged()
        binding.lineChart.invalidate()
    }

    private fun stopMeasurement() {
        measuring = false
        handler.removeCallbacks(timerLoop)
        bleManager.disconnect()

        if (sensorReadings.isEmpty()) {
            finish()
            return
        }

        // 기존: avg/max/min 계산해서 Intent로 숫자만 전달
        val avgPressure = sensorReadings.map { it.overall }.average().toFloat()
        val maxPressure = sensorReadings.maxOf { it.overall }
        val minPressure = sensorReadings.minOf { it.overall }

        // 새로 추가: 원시 데이터 전체 + 진동 횟수는 Holder에 담아둠
        SessionDataHolder.sensorReadings = sensorReadings.toList()
        SessionDataHolder.vibrationCount = vibrationCount

        // 기존 방식 그대로: 숫자 4개는 Intent로 전달 (화면 표시는 이 값 그대로 씀)
        val intent = Intent(this, SessionCompleteActivity::class.java).apply {
            putExtra("duration_seconds", elapsedSeconds)
            putExtra("avg_pressure", avgPressure)
            putExtra("max_pressure", maxPressure)
            putExtra("min_pressure", minPressure)
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        measuring = false
        handler.removeCallbacks(timerLoop)
        bleManager.disconnect()
    }
}
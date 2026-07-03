package com.newritage.app.ui.baseline

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.newritage.app.R
import com.newritage.app.ble.BleManager
import com.newritage.app.data.BaselineData
import com.newritage.app.data.UserPreferences
import com.newritage.app.databinding.ActivityBaselineMeasurementBinding
import com.newritage.app.ui.main.MainActivity

class BaselineMeasurementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaselineMeasurementBinding
    private lateinit var prefs: UserPreferences
    private lateinit var bleManager: BleManager

    private val handler = Handler(Looper.getMainLooper())
    private var measuring = false
    private var elapsedSeconds = 0

    // 화면 표시용 타이머 길이 (실제 계산은 기기가 함, 이건 UX용 진행률 표시일 뿐)
    private val measureDuration = 180

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaselineMeasurementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = UserPreferences(this)
        setupBle()
        setupUI()

        handler.post {
            if (!isFinishing) {
                showMeasurementGuideDialog()
            }
        }
    }

    private fun setupBle() {
        bleManager = BleManager(
            context = this,
            onBaselineReceived = { baselineData ->
                // 기기가 3분 측정 끝내고 계산까지 마친 뒤 보내주는 최종 결과
                runOnUiThread {
                    completeMeasurement(baselineData)
                }
            },
            onDataReceived = { reading ->
                // 측정 중 기기가 실시간으로 값을 흘려보내준다면, 화면에 실시간 표시만
                if (measuring) {
                    runOnUiThread {
                        binding.tvLivePressureValue.text = String.format("%d", reading.overall.toInt())
                    }
                }
            },
            onVibrationEvent = { }  // 이 화면에서는 안 씀
        )
        bleManager.startScan()
    }

    private fun setupUI() {
        showScreen(Screen.GUIDE)

        binding.btnStartMeasure.setOnClickListener {
            startMeasurement()
        }

        binding.btnGoMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun showMeasurementGuideDialog() {
        val dialog = BottomSheetDialog(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_baseline_measurement_complete, null)
        dialog.setContentView(dialogView)

        dialogView.setOnClickListener { dialog.dismiss() }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    private fun startMeasurement() {
        showScreen(Screen.MEASURING)
        measuring = true
        elapsedSeconds = 0

        binding.circularProgressBar.max = measureDuration
        binding.circularProgressBar.progress = 0

        // ⚠️ 여기서 기기에게 "기준값 측정 시작해"라는 명령을 BLE로 보내야 할 수도 있어요.
        //     (펌웨어가 버튼으로 시작하는 게 아니라 앱 명령으로 시작한다면 필요)
        timerLoop.run()
    }

    // 화면 진행률(원형 프로그레스바, 타이머 텍스트)만 담당. 실제 압력 계산은 기기가 함.
    private val timerLoop = object : Runnable {
        override fun run() {
            if (!measuring) return
            elapsedSeconds++

            binding.tvLiveTimer.text = String.format("%d:%02d", elapsedSeconds / 60, elapsedSeconds % 60)
            binding.circularProgressBar.progress = elapsedSeconds

            val currentProgressLevel = elapsedSeconds.toFloat() / measureDuration
            binding.waveAnimationView.setWaveProgress(currentProgressLevel)

            if (elapsedSeconds < measureDuration) {
                handler.postDelayed(this, 1000L)
            }
            // 180초가 지나도 여기서 강제로 completeMeasurement() 호출하지 않음!
            // 완료 시점은 기기가 onBaselineReceived로 알려줄 때임 (기기 계산이 끝나야 진짜 완료)
        }
    }

    // 기기로부터 최종 기준값을 받았을 때 호출됨
    private fun completeMeasurement(baselineData: BaselineData) {
        measuring = false
        handler.removeCallbacks(timerLoop)

        prefs.baselineThumb = baselineData.thumb
        prefs.baselineIM = baselineData.indexMiddle
        prefs.baselinePalm = baselineData.palm
        prefs.baselineOverall = baselineData.overall
        prefs.isBaselineDone = true

        // 완료 화면 중앙에 최종 결과 표기
        binding.tvLivePressureValue.text = String.format("%d", baselineData.overall.toInt())
        showScreen(Screen.COMPLETE)
    }

    private fun showScreen(screen: Screen) {
        binding.layoutReady.visibility = if (screen == Screen.GUIDE) View.VISIBLE else View.GONE
        binding.layoutMeasuring.visibility = if (screen == Screen.MEASURING) View.VISIBLE else View.GONE
        binding.layoutComplete.visibility = if (screen == Screen.COMPLETE) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        measuring = false
        handler.removeCallbacks(timerLoop)
        bleManager.disconnect()
    }

    enum class Screen { GUIDE, MEASURING, COMPLETE }
}
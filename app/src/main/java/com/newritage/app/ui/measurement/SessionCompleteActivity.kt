package com.newritage.app.ui.measurement

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.newritage.app.R
import com.newritage.app.data.AppDatabase
import com.newritage.app.data.Session
import com.newritage.app.ui.main.MainActivity
import com.newritage.app.ui.util.WaveView
import com.newritage.app.util.ThreadColors
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SessionCompleteActivity : AppCompatActivity() {

    private enum class Screen { RECORD, COMPLETE, THREAD }

    // RECORD
    private lateinit var screenRecord: View
    private lateinit var tvSessionTimeVal: TextView
    private lateinit var tvAvgPressureVal: TextView
    private lateinit var tvMaxMinVal: TextView
    private lateinit var tvDeviationVal: TextView
    private lateinit var etEmotion: EditText
    private lateinit var btnSkip: Button
    private lateinit var btnRecord: Button

    // COMPLETE
    private lateinit var screenComplete: View
    private lateinit var waveViewComplete: WaveView
    private lateinit var tvStreakDays: TextView

    // THREAD
    private lateinit var screenThread: View
    private lateinit var tvThreadDate: TextView
    private lateinit var threadColorView: View
    private lateinit var tvTensionGauge: TextView
    private lateinit var tvPressureDesc1: TextView
    private lateinit var tvPressureDesc2: TextView
    private lateinit var btnViewColorDesc: Button

    // Extras
    private var durationSeconds = 0
    private var avgPressure = 32f
    private var maxPressure = 48f
    private var minPressure = 18f
    private var deviationCount = 0
    private var startTime = ""
    private var endTime = ""

    private var isFirstSession = true
    private var assignedColor: ThreadColors.ThreadColor? = null

    private val dao by lazy { AppDatabase.getDatabase(this).sessionDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_complete)

        // Extras
        durationSeconds = intent.getIntExtra("duration_seconds", 0)
        avgPressure     = intent.getFloatExtra("avg_pressure", 32f)
        maxPressure     = intent.getFloatExtra("max_pressure", 48f)
        minPressure     = intent.getFloatExtra("min_pressure", 18f)
        deviationCount  = intent.getIntExtra("deviation_count", 0)
        startTime       = intent.getStringExtra("start_time") ?: ""
        endTime         = intent.getStringExtra("end_time") ?: ""

        initViews()
        setupListeners()
        loadStats()
        checkFirstSessionToday()
        showScreen(Screen.RECORD)
    }

    private fun initViews() {
        screenRecord = findViewById(R.id.screenRecord)
        tvSessionTimeVal = findViewById(R.id.tvSessionTimeVal)
        tvAvgPressureVal = findViewById(R.id.tvAvgPressureVal)
        tvMaxMinVal = findViewById(R.id.tvMaxMinVal)
        tvDeviationVal = findViewById(R.id.tvDeviationVal)
        etEmotion = findViewById(R.id.etEmotion)
        btnSkip = findViewById(R.id.btnSkip)
        btnRecord = findViewById(R.id.btnRecord)

        screenComplete = findViewById(R.id.screenComplete)
        waveViewComplete = findViewById(R.id.waveViewComplete)
        tvStreakDays = findViewById(R.id.tvStreakDays)

        screenThread = findViewById(R.id.screenThread)
        tvThreadDate = findViewById(R.id.tvThreadDate)
        threadColorView = findViewById(R.id.threadColorView)
        tvTensionGauge = findViewById(R.id.tvTensionGauge)
        tvPressureDesc1 = findViewById(R.id.tvPressureDesc1)
        tvPressureDesc2 = findViewById(R.id.tvPressureDesc2)
        btnViewColorDesc = findViewById(R.id.btnViewColorDesc)

        findViewById<ImageButton>(R.id.btnBackRecord).setOnClickListener { finish() }
        findViewById<TextView>(R.id.tvHeaderHome).setOnClickListener { goHome() }
    }

    private fun setupListeners() {
        btnSkip.setOnClickListener { saveSession("") }
        btnRecord.setOnClickListener { saveSession(etEmotion.text.toString()) }
        btnViewColorDesc.setOnClickListener { goHome() }
    }

    private fun loadStats() {
        tvSessionTimeVal.text = if (startTime.isNotEmpty() && endTime.isNotEmpty()) {
            "$startTime-$endTime"
        } else {
            val minutes = durationSeconds / 60
            val seconds = durationSeconds % 60
            "%02d:%02d".format(minutes, seconds)
        }
        
        // Using "N" as per the prototype image
        tvAvgPressureVal.text = "%.0fN".format(avgPressure)
        tvMaxMinVal.text = "%.0fN/%.0fN".format(minPressure, maxPressure)
        tvDeviationVal.text = "${deviationCount}회"
    }

    private fun checkFirstSessionToday() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        lifecycleScope.launch {
            val count = dao.countSessionsByDate(today)
            isFirstSession = (count == 0)
        }
    }

    private fun saveSession(emotion: String) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val colorObj = ThreadColors.assignColor(avgPressure)
        assignedColor = colorObj

        lifecycleScope.launch {
            // Get current session index
            val countToday = dao.countSessionsByDate(today)
            
            val session = Session(
                date = today,
                sessionIndex = countToday + 1,
                hasThread = isFirstSession,
                startTime = startTime,
                endTime = endTime,
                durationSeconds = durationSeconds,
                avgPressure = avgPressure,
                maxPressure = maxPressure,
                minPressure = minPressure,
                emotion = emotion,
                threadColor = colorObj.hex,
                threadColorName = colorObj.nameKr
            )

            dao.insert(session)
            
            if (isFirstSession) {
                showScreen(Screen.THREAD)
                prepareThreadScreen()
            } else {
                showScreen(Screen.COMPLETE)
                prepareCompleteScreen()
            }
        }
    }

    private fun showScreen(screen: Screen) {
        screenRecord.visibility = if (screen == Screen.RECORD) View.VISIBLE else View.GONE
        screenComplete.visibility = if (screen == Screen.COMPLETE) View.VISIBLE else View.GONE
        screenThread.visibility = if (screen == Screen.THREAD) View.VISIBLE else View.GONE
    }

    private fun prepareCompleteScreen() {
        waveViewComplete.setPressure(avgPressure)
        lifecycleScope.launch {
            val totalDays = dao.getTotalActiveDays()
            tvStreakDays.text = getString(R.string.streak_days_format, totalDays)
        }
        // Auto finish after 2.5 seconds
        screenComplete.postDelayed({ goHome() }, 2500)
    }

    private fun prepareThreadScreen() {
        val todayStr = SimpleDateFormat("yyyy년 M월 d일", Locale.getDefault()).format(Date())
        tvThreadDate.text = todayStr
        assignedColor?.let {
            threadColorView.setBackgroundColor(Color.parseColor(it.hex))
            tvTensionGauge.text = "긴장도 ${it.level}/보통"
        }
        tvPressureDesc1.text = "오늘 당신의 명상은 매우 안정적이었습니다."
        tvPressureDesc2.text = "꾸준한 명상을 통해 마음의 근육을 키워보세요."
    }

    private fun goHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}

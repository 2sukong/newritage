package com.newritage.app.ui.main.analysis

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.newritage.app.R
import com.newritage.app.data.AppDatabase
import com.newritage.app.data.Session
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class WeeklyAnalysisFragment : Fragment() {

    private lateinit var tvDateRange: TextView
    private lateinit var tvAvg: TextView
    private lateinit var tvMax: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvCount: TextView
    private lateinit var lineChart: LineChart
    
    // Comparison Card
    private lateinit var layoutComparison: View
    private lateinit var compAvgPressure: View
    private lateinit var compMaxPressure: View
    private lateinit var compMeditationTime: View
    private lateinit var compMeditationCount: View
    
    // Comment Card
    private lateinit var layoutComment: View
    private lateinit var tvCommentTitle: TextView
    private lateinit var tvCommentContent: TextView
    
    private lateinit var btnPrev: ImageButton
    private lateinit var btnNext: ImageButton

    private var currentWeekStart = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
    }

    private val dao by lazy { AppDatabase.getDatabase(requireContext()).sessionDao() }
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val dispSdf = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.fragment_weekly_analysis, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener { parentFragmentManager.popBackStack() }

        tvDateRange = view.findViewById(R.id.tvDateRange)
        tvAvg = view.findViewById(R.id.tvAvgPressure)
        tvMax = view.findViewById(R.id.tvMaxPressure)
        tvTime = view.findViewById(R.id.tvSessionTime)
        tvCount = view.findViewById(R.id.tvSessionCount)
        lineChart = view.findViewById(R.id.lineChart)
        
        layoutComparison = view.findViewById(R.id.layoutComparison)
        compAvgPressure = layoutComparison.findViewById(R.id.compAvgPressure)
        compMaxPressure = layoutComparison.findViewById(R.id.compMaxPressure)
        compMeditationTime = layoutComparison.findViewById(R.id.compMeditationTime)
        compMeditationCount = layoutComparison.findViewById(R.id.compMeditationCount)
        
        layoutComment = view.findViewById(R.id.layoutComment)
        tvCommentTitle = layoutComment.findViewById(R.id.tvCommentTitle)
        tvCommentContent = layoutComment.findViewById(R.id.tvCommentContent)
        
        btnPrev = view.findViewById(R.id.btnPrev)
        btnNext = view.findViewById(R.id.btnNext)

        btnPrev.setOnClickListener { currentWeekStart.add(Calendar.WEEK_OF_YEAR, -1); loadData() }
        btnNext.setOnClickListener { currentWeekStart.add(Calendar.WEEK_OF_YEAR, 1); loadData() }

        setupChart()
        loadData()
    }

    private fun setupChart() {
        lineChart.apply {
            description.isEnabled = false
            legend.isEnabled = true
            setDrawGridBackground(false)
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                textColor = Color.parseColor("#AAAAAA")
                labelCount = 7
                valueFormatter = object : ValueFormatter() {
                    private val days = listOf("월","화","수","목","금","토","일")
                    override fun getFormattedValue(v: Float): String = days.getOrElse(v.toInt()) { "" }
                }
            }
            axisLeft.apply {
                axisMinimum = 0f; axisMaximum = 80f
                textColor = Color.parseColor("#AAAAAA")
                gridColor = Color.parseColor("#E0E0D8")
            }
            axisRight.isEnabled = false
        }
    }

    private fun loadData() {
        val weekEnd = (currentWeekStart.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, 6) }
        val prevWeekStart = (currentWeekStart.clone() as Calendar).apply { add(Calendar.WEEK_OF_YEAR, -1) }
        val prevWeekEnd = (currentWeekStart.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -1) }

        tvDateRange.text = "${dispSdf.format(currentWeekStart.time)} - ${dispSdf.format(weekEnd.time)}"

        lifecycleScope.launch {
            val sessions = dao.getSessionsInRange(sdf.format(currentWeekStart.time), sdf.format(weekEnd.time))
            val prevSessions = dao.getSessionsInRange(sdf.format(prevWeekStart.time), sdf.format(prevWeekEnd.time))
            updateUI(sessions, prevSessions)
        }
    }

    private fun updateUI(sessions: List<Session>, prevSessions: List<Session>) {
        if (sessions.isEmpty()) {
            tvAvg.text = "-"; tvMax.text = "-"; tvTime.text = "-"; tvCount.text = "0회"
            tvCommentContent.text = getString(R.string.analysis_no_data)
            lineChart.clear(); return
        }
        val totalSecs = sessions.sumOf { it.durationSeconds }
        val allAvg = sessions.map { it.avgPressure }.average().toFloat()
        val allMax = sessions.maxOf { it.maxPressure }

        tvAvg.text = "%.1f".format(allAvg)
        tvMax.text = "%.1f".format(allMax)
        tvTime.text = "%02d:%02d".format(totalSecs / 60, totalSecs % 60)
        tvCount.text = "${sessions.size}회"

        // Previous week comparison
        setupComparison(sessions, prevSessions)

        // Chart: this week vs prev week
        val thisWeekEntries = buildDailyAverages(sessions)
        val prevWeekEntries = buildDailyAverages(prevSessions)

        val ds1 = LineDataSet(thisWeekEntries, "이번 주").apply {
            color = Color.parseColor("#8B9E7B"); lineWidth = 2f
            setDrawCircles(true); circleRadius = 4f; setCircleColor(Color.parseColor("#8B9E7B"))
            setDrawValues(true); valueTextSize = 9f; valueTextColor = Color.parseColor("#6B6B6B")
            mode = LineDataSet.Mode.CUBIC_BEZIER; setDrawFilled(false)
        }
        val dataSets = mutableListOf<ILineDataSet>()
        dataSets.add(ds1)
        if (prevWeekEntries.isNotEmpty()) {
            val ds2 = LineDataSet(prevWeekEntries, "저번 주").apply {
                color = Color.parseColor("#C0C0B0"); lineWidth = 1.5f
                setDrawCircles(true); circleRadius = 3f; setCircleColor(Color.parseColor("#C0C0B0"))
                setDrawValues(false); mode = LineDataSet.Mode.CUBIC_BEZIER
                enableDashedLine(10f, 5f, 0f)
            }
            dataSets.add(ds2)
        }
        lineChart.data = LineData(dataSets)
        lineChart.invalidate()

        // tvComment already bound in onViewCreated
        tvCommentTitle.text = "주간 코멘트"
        tvCommentContent.text = when {
            allAvg < 25f -> "이번 주는 매우 편안한 명상을 하셨어요!"
            allAvg < 45f -> "이번 주 명상이 전반적으로 좋았습니다."
            else -> "이번 주는 긴장이 높았습니다. 더 편안한 환경을 만들어보세요."
        }
    }

    private fun setupComparison(sessions: List<Session>, prevSessions: List<Session>) {
        layoutComparison.findViewById<TextView>(R.id.tvComparisonTitle).text = "저번주와 비교"
        
        if (prevSessions.isEmpty()) {
            // Placeholder or disable
            return
        }

        // Avg Pressure
        val currAvg = sessions.map { it.avgPressure }.average().toFloat()
        val prevAvg = prevSessions.map { it.avgPressure }.average().toFloat()
        updateCompItem(compAvgPressure, "평균 압력", currAvg, prevAvg, isPercentage = true, inverseColor = true)

        // Max Pressure
        val currMax = sessions.maxOf { it.maxPressure }
        val prevMax = prevSessions.maxOf { it.maxPressure }
        updateCompItem(compMaxPressure, "최고 압력", currMax, prevMax, isPercentage = true, inverseColor = true)

        // Meditation Time
        val currTime = sessions.sumOf { it.durationSeconds } / 60
        val prevTime = prevSessions.sumOf { it.durationSeconds } / 60
        updateCompItem(compMeditationTime, "명상 시간", currTime.toFloat(), prevTime.toFloat(), isPercentage = false, unit = "분")

        // Meditation Count
        val currCount = sessions.size
        val prevCount = prevSessions.size
        updateCompItem(compMeditationCount, "명상 횟수", currCount.toFloat(), prevCount.toFloat(), isPercentage = false, unit = "회")
    }

    private fun updateCompItem(view: View, title: String, curr: Float, prev: Float, isPercentage: Boolean, inverseColor: Boolean = false, unit: String = "") {
        val tvTitle = view.findViewById<TextView>(R.id.tvCompTitle)
        val ivArrow = view.findViewById<ImageView>(R.id.ivCompArrow)
        val tvValue = view.findViewById<TextView>(R.id.tvCompValue)

        tvTitle.text = title
        val diff = curr - prev
        val isIncr = diff > 0
        
        ivArrow.rotation = if (isIncr) 180f else 0f // Rotate ic_arrow_left to point up/down? Actually ic_arrow_left is left.
        // Better: let's use a simple arrow char for now or fix rotation. 
        // If ic_arrow_left points Left (180deg from Right), then 90 is Down, 270 is Up.
        ivArrow.rotation = if (isIncr) 270f else 90f
        
        val colorGreen = Color.parseColor("#8B9E7B")
        val colorRed = Color.parseColor("#D32F2F")
        
        // Color rule: Pressure lower is better (Green), Time/Count higher is better (Green)
        val goodColor = if (inverseColor) (if (!isIncr) colorGreen else colorRed) else (if (isIncr) colorGreen else colorRed)
        ivArrow.setColorFilter(goodColor)
        tvValue.setTextColor(goodColor)

        val diffVal = kotlin.math.abs(diff)
        if (isPercentage) {
            val percent = if (prev != 0f) (diffVal / prev * 100) else 0f
            tvValue.text = "${if (isIncr) "▲" else "▼"} ${"%.1f".format(percent)}%"
        } else {
            tvValue.text = "${if (isIncr) "▲" else "▼"} ${if (isIncr) "+" else "-"}${diffVal.toInt()}$unit"
        }
    }

    private fun buildDailyAverages(sessions: List<Session>): List<Entry> {
        val grouped = sessions.groupBy { it.date }
        return (0..6).map { dayIdx ->
            val cal = (currentWeekStart.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, dayIdx) }
            val key = sdf.format(cal.time)
            val avg = grouped[key]?.map { it.avgPressure }?.average()?.toFloat() ?: 0f
            Entry(dayIdx.toFloat(), avg)
        }.filter { it.y > 0 }
    }
}

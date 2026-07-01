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
    private lateinit var tvPrevCompare: TextView
    private lateinit var tvComment: TextView
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
        tvPrevCompare = view.findViewById(R.id.tvPrevCompare)
        tvComment = view.findViewById(R.id.tvComment)
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
            tvPrevCompare.text = "이전 주 데이터 없음"
            lineChart.clear(); return
        }
        val totalSecs = sessions.sumOf { it.durationSeconds }
        val allAvg = sessions.map { it.avgPressure }.average().toFloat()
        val allMax = sessions.maxOf { it.maxPressure }

        tvAvg.text = "%.1f kPa".format(allAvg)
        tvMax.text = "%.1f kPa".format(allMax)
        tvTime.text = "%02d:%02d".format(totalSecs / 60, totalSecs % 60)
        tvCount.text = "${sessions.size}회"

        // Previous week comparison
        if (prevSessions.isNotEmpty()) {
            val prevAvg = prevSessions.map { it.avgPressure }.average().toFloat()
            val diff = allAvg - prevAvg
            val arrow = if (diff < 0) "▼" else "▲"
            val direction = if (diff < 0) "감소" else "증가"
            tvPrevCompare.text = "저번 주 대비 평균 압력 $arrow ${"%.1f".format(kotlin.math.abs(diff))} kPa $direction"
            tvPrevCompare.setTextColor(if (diff < 0) Color.parseColor("#5B9070") else Color.parseColor("#C07070"))
        } else {
            tvPrevCompare.text = "저번 주 비교 데이터 없음"
        }

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
        tvComment.text = when {
            allAvg < 25f -> "이번 주는 매우 편안한 명상을 하셨어요!"
            allAvg < 45f -> "이번 주 명상이 전반적으로 좋았습니다."
            else -> "이번 주는 긴장이 높았습니다. 더 편안한 환경을 만들어보세요."
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

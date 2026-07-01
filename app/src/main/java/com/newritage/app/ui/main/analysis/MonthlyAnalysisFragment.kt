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

class MonthlyAnalysisFragment : Fragment() {

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

    private var currentMonth = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
    }

    private val dao by lazy { AppDatabase.getDatabase(requireContext()).sessionDao() }
    private val monthSdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    private val displaySdf = SimpleDateFormat("yyyy년 M월", Locale.getDefault())

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.fragment_monthly_analysis, c, false)

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

        btnPrev.setOnClickListener { currentMonth.add(Calendar.MONTH, -1); loadData() }
        btnNext.setOnClickListener { currentMonth.add(Calendar.MONTH, 1); loadData() }

        setupChart()
        loadData()
    }

    private fun setupChart() {
        lineChart.apply {
            description.isEnabled = false; legend.isEnabled = true
            setDrawGridBackground(false)
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                textColor = Color.parseColor("#AAAAAA")
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(v: Float) = "${v.toInt()}일"
                }
                labelCount = 6
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
        val monthStr = monthSdf.format(currentMonth.time)
        tvDateRange.text = displaySdf.format(currentMonth.time)

        val prevMonth = (currentMonth.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
        val prevMonthStr = monthSdf.format(prevMonth.time)

        lifecycleScope.launch {
            val sessions = dao.getSessionsByMonth("$monthStr%")
            val prevSessions = dao.getSessionsByMonth("$prevMonthStr%")
            updateUI(sessions, prevSessions)
        }
    }

    private fun updateUI(sessions: List<Session>, prevSessions: List<Session>) {
        if (sessions.isEmpty()) {
            tvAvg.text = "-"; tvMax.text = "-"; tvTime.text = "-"; tvCount.text = "0회"
            tvPrevCompare.text = "이전 달 데이터 없음"
            lineChart.clear(); return
        }
        val totalSecs = sessions.sumOf { it.durationSeconds }
        val allAvg = sessions.map { it.avgPressure }.average().toFloat()
        val allMax = sessions.maxOf { it.maxPressure }

        tvAvg.text = "%.1f kPa".format(allAvg)
        tvMax.text = "%.1f kPa".format(allMax)
        tvTime.text = "%02d:%02d".format(totalSecs / 60, totalSecs % 60)
        tvCount.text = "${sessions.size}회"

        // Prev month comparison
        if (prevSessions.isNotEmpty()) {
            val prevAvg = prevSessions.map { it.avgPressure }.average().toFloat()
            val diff = allAvg - prevAvg
            val arrow = if (diff < 0) "▼" else "▲"
            tvPrevCompare.text = "저번 달 대비 평균 압력 $arrow ${"%.1f".format(kotlin.math.abs(diff))} kPa"
            tvPrevCompare.setTextColor(if (diff < 0) Color.parseColor("#5B9070") else Color.parseColor("#C07070"))
        } else {
            tvPrevCompare.text = "저번 달 비교 데이터 없음"
        }

        // Daily averages chart (this month + last month)
        val sdf = SimpleDateFormat("dd", Locale.getDefault())
        val grouped = sessions.groupBy { it.date.takeLast(2).trimStart('0').ifEmpty { "0" } }
        val thisMonthEntries = grouped.map { (day, sess) ->
            Entry(day.toFloat(), sess.map { it.avgPressure }.average().toFloat())
        }.sortedBy { it.x }

        val ds1 = LineDataSet(thisMonthEntries, "이번 달").apply {
            color = Color.parseColor("#8B9E7B"); lineWidth = 2f
            setDrawCircles(true); circleRadius = 3f; setCircleColor(Color.parseColor("#8B9E7B"))
            setDrawValues(false); mode = LineDataSet.Mode.CUBIC_BEZIER
        }
        val dataSets = mutableListOf<ILineDataSet>()
        dataSets.add(ds1)

        if (prevSessions.isNotEmpty()) {
            val prevGrouped = prevSessions.groupBy { it.date.takeLast(2).trimStart('0').ifEmpty { "0" } }
            val prevEntries = prevGrouped.map { (day, sess) ->
                Entry(day.toFloat(), sess.map { it.avgPressure }.average().toFloat())
            }.sortedBy { it.x }
            val ds2 = LineDataSet(prevEntries, "저번 달").apply {
                color = Color.parseColor("#C0C0B0"); lineWidth = 1.5f
                setDrawCircles(true); circleRadius = 2f; setCircleColor(Color.parseColor("#C0C0B0"))
                setDrawValues(false); mode = LineDataSet.Mode.CUBIC_BEZIER
                enableDashedLine(10f, 5f, 0f)
            }
            dataSets.add(ds2)
        }

        lineChart.data = LineData(dataSets)
        lineChart.invalidate()

        tvComment.text = when {
            allAvg < 25f -> "이번 달은 전반적으로 매우 안정적인 명상을 하셨어요!"
            allAvg < 45f -> "이번 달 명상이 전반적으로 좋았습니다. 꾸준히 유지해보세요."
            else -> "이번 달은 긴장이 높았습니다. 더 편안한 명상 환경을 만들어보세요."
        }
    }
}

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
import com.github.mikephil.charting.formatter.ValueFormatter
import com.newritage.app.R
import com.newritage.app.data.AppDatabase
import com.newritage.app.data.Session
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DailyAnalysisFragment : Fragment() {

    private val graphColors = intArrayOf(
        Color.parseColor("#8B9E7B"),
        Color.parseColor("#7B8EAB"),
        Color.parseColor("#AB8B7B"),
        Color.parseColor("#8BAB8B"),
        Color.parseColor("#AB9B7B")
    )

    private lateinit var tvDate: TextView
    private lateinit var tvAvg: TextView
    private lateinit var tvMax: TextView
    private lateinit var tvTime: TextView
    private lateinit var lineChart: LineChart
    private lateinit var tvComment: TextView
    private lateinit var btnPrev: ImageButton
    private lateinit var btnNext: ImageButton

    private var currentDate = Calendar.getInstance()
    private val dao by lazy { AppDatabase.getDatabase(requireContext()).sessionDao() }
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displaySdf = SimpleDateFormat("yyyy.MM.dd (E)", Locale("ko"))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View =
        inflater.inflate(R.layout.fragment_daily_analysis, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        tvDate = view.findViewById(R.id.tvDate)
        tvAvg = view.findViewById(R.id.tvAvgPressure)
        tvMax = view.findViewById(R.id.tvMaxPressure)
        tvTime = view.findViewById(R.id.tvSessionTime)
        lineChart = view.findViewById(R.id.lineChart)
        tvComment = view.findViewById(R.id.tvComment)
        btnPrev = view.findViewById(R.id.btnPrev)
        btnNext = view.findViewById(R.id.btnNext)

        btnPrev.setOnClickListener { currentDate.add(Calendar.DAY_OF_YEAR, -1); loadData() }
        btnNext.setOnClickListener { currentDate.add(Calendar.DAY_OF_YEAR, 1); loadData() }

        setupChart()
        loadData()
    }

    private fun setupChart() {
        lineChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            legend.isEnabled = true
            setDrawGridBackground(false)
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                textColor = Color.parseColor("#AAAAAA")
                axisMinimum = 0f
                axisMaximum = 600f // 10 minutes max for summary view
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(v: Float): String {
                        val t = v.toInt()
                        return "%d초".format(t)
                    }
                }
                labelCount = 5
            }
            axisLeft.apply {
                axisMinimum = 0f; axisMaximum = 80f
                textColor = Color.parseColor("#AAAAAA")
                setDrawGridLines(true)
                gridColor = Color.parseColor("#F0F0F0")
            }
            axisRight.isEnabled = false
        }
    }

    private fun loadData() {
        val dateStr = sdf.format(currentDate.time)
        tvDate.text = displaySdf.format(currentDate.time)

        lifecycleScope.launch {
            val sessions = dao.getSessionsByDate(dateStr)
            updateUI(sessions)
        }
    }

    private fun updateUI(sessions: List<Session>) {
        if (sessions.isEmpty()) {
            tvAvg.text = "-"; tvMax.text = "-"; tvTime.text = "-"
            tvComment.text = getString(R.string.analysis_no_data)
            lineChart.clear()
            return
        }

        // Combined stats across all sessions for the day
        val totalSecs = sessions.sumOf { it.durationSeconds }
        val allAvg = sessions.map { it.avgPressure }.average().toFloat()
        val allMax = sessions.maxOf { it.maxPressure }
        val maxDuration = sessions.maxOf { it.durationSeconds }.toFloat()
        
        tvAvg.text = "%.1f kPa".format(allAvg)
        tvMax.text = "%.1f kPa".format(allMax)
        tvTime.text = "%02d:%02d".format(totalSecs / 60, totalSecs % 60)

        // Set X-axis max to the longest meditation time
        lineChart.xAxis.axisMaximum = if (maxDuration > 0) maxDuration else 600f

        // Multi-session chart: overlay multiple lines
        val dataSets = sessions.mapIndexed { idx, session ->
            val entries = generateSessionEntries(session, 0) // Start all at X=0 as requested "fixed XY"
            LineDataSet(entries, "${idx + 1}회차").apply {
                color = graphColors[idx % graphColors.size]
                lineWidth = 1.8f
                setDrawCircles(false)
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawFilled(false)
            }
        }
        
        lineChart.data = LineData(dataSets)
        lineChart.invalidate()

        tvComment.text = when {
            allAvg < 25f -> "오늘은 매우 안정적인 명상을 하셨어요!"
            allAvg < 45f -> "전반적으로 명상 압력이 적절히 유지되었습니다."
            else -> "조금 더 편안한 자세로 명상에 집중해보세요."
        }
    }

    private fun generateSessionEntries(session: Session, timeOffsetSecs: Int): List<Entry> {
        val entries = mutableListOf<Entry>()
        val count = maxOf(session.durationSeconds / 10, 5)
        var p = session.avgPressure + (Math.random() * 10 - 5).toFloat()
        for (i in 0..count) {
            val t = (timeOffsetSecs + i * (session.durationSeconds / count)).toFloat()
            val noise = (Math.random() * 8 - 4).toFloat()
            p = (p + noise).coerceIn(5f, 75f)
            entries.add(Entry(t, p))
        }
        return entries
    }
}

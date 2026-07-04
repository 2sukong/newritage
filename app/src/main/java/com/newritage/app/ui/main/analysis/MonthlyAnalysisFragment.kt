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
    
    // Comparison Card
    private lateinit var layoutComparison: View
    private lateinit var compAvgPressure: View
    private lateinit var compMaxPressure: View
    private lateinit var compMeditationTime: View
    private lateinit var compMeditationCount: View
    
    // Monthly Calendar
    private lateinit var calendarGrid: GridLayout
    
    // AI Comprehensive Comment
    private lateinit var layoutAIComment: View
    private lateinit var tvCommentTitle: TextView
    private lateinit var tvCommentContent: TextView
    
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
        
        layoutComparison = view.findViewById(R.id.layoutComparison)
        compAvgPressure = layoutComparison.findViewById(R.id.compAvgPressure)
        compMaxPressure = layoutComparison.findViewById(R.id.compMaxPressure)
        compMeditationTime = layoutComparison.findViewById(R.id.compMeditationTime)
        compMeditationCount = layoutComparison.findViewById(R.id.compMeditationCount)
        
        calendarGrid = view.findViewById(R.id.calendarGrid)
        
        layoutAIComment = view.findViewById(R.id.layoutAIComment)
        tvCommentTitle = layoutAIComment.findViewById(R.id.tvCommentTitle)
        tvCommentContent = layoutAIComment.findViewById(R.id.tvCommentContent)
        
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
            tvCommentContent.text = getString(R.string.analysis_no_data)
            lineChart.clear()
            calendarGrid.removeAllViews()
            return
        }
        val totalSecs = sessions.sumOf { it.durationSeconds }
        val allAvg = sessions.map { it.avgPressure }.average().toFloat()
        val allMax = sessions.maxOf { it.maxPressure }

        tvAvg.text = "%.1f".format(allAvg)
        tvMax.text = "%.1f".format(allMax)
        tvTime.text = "%02d:%02d".format(totalSecs / 60, totalSecs % 60)
        tvCount.text = "${sessions.size}회"

        // Comparison
        setupComparison(sessions, prevSessions)
        
        // Calendar
        setupMonthlyCalendar(sessions)

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

        tvCommentTitle.text = "AI 종합 코멘트"
        tvCommentContent.text = generateComprehensiveComment(sessions)
    }

    private fun setupComparison(sessions: List<Session>, prevSessions: List<Session>) {
        layoutComparison.findViewById<TextView>(R.id.tvComparisonTitle).text = "저번달과 비교"
        if (prevSessions.isEmpty()) return

        val currAvg = sessions.map { it.avgPressure }.average().toFloat()
        val prevAvg = prevSessions.map { it.avgPressure }.average().toFloat()
        updateCompItem(compAvgPressure, "평균 압력", currAvg, prevAvg, isPercentage = true, inverseColor = true)

        val currMax = sessions.maxOf { it.maxPressure }
        val prevMax = prevSessions.maxOf { it.maxPressure }
        updateCompItem(compMaxPressure, "최고 압력", currMax, prevMax, isPercentage = true, inverseColor = true)

        val currTime = sessions.sumOf { it.durationSeconds } / 60
        val prevTime = prevSessions.sumOf { it.durationSeconds } / 60
        updateCompItem(compMeditationTime, "명상 시간", currTime.toFloat(), prevTime.toFloat(), isPercentage = false, unit = "분")

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
        ivArrow.rotation = if (isIncr) 270f else 90f
        
        val colorGreen = Color.parseColor("#8B9E7B")
        val colorRed = Color.parseColor("#D32F2F")
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

    private fun setupMonthlyCalendar(sessions: List<Session>) {
        calendarGrid.removeAllViews()
        val cal = currentMonth.clone() as Calendar
        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1 // 0=Sun
        
        val sessionsByDay = sessions.groupBy { it.date.takeLast(2).toInt() }

        // Total 42 cells for 6 weeks
        for (i in 0 until 42) {
            val day = i - firstDayOfWeek + 1
            val cell = LayoutInflater.from(requireContext()).inflate(R.layout.item_calendar_cell_simple, calendarGrid, false)
            val tvDay = cell.findViewById<TextView>(R.id.tvDay)
            val ivIndicator = cell.findViewById<ImageView>(R.id.ivIndicator)

            if (day in 1..daysInMonth) {
                tvDay.text = day.toString()
                val daySessions = sessionsByDay[day]
                if (daySessions != null) {
                    val avg = daySessions.map { it.avgPressure }.average()
                    val color = when {
                        avg < 30 -> Color.parseColor("#8B9E7B") // 안정
                        avg < 50 -> Color.parseColor("#EDD055") // 보통
                        else -> Color.parseColor("#D32F2F")     // 긴장
                    }
                    ivIndicator.setColorFilter(color)
                    ivIndicator.visibility = View.VISIBLE
                } else {
                    ivIndicator.setColorFilter(Color.parseColor("#AAAAAA"))
                    ivIndicator.visibility = View.VISIBLE // Or GONE if requested "회색: 기록 없음"
                }
            } else {
                cell.visibility = View.INVISIBLE
            }
            calendarGrid.addView(cell)
        }
    }

    private fun generateComprehensiveComment(sessions: List<Session>): String {
        if (sessions.isEmpty()) return "명상 데이터가 부족합니다."
        
        val allAvg = sessions.map { it.avgPressure }.average().toFloat()
        val totalTime = sessions.sumOf { it.durationSeconds } / 60
        val totalCount = sessions.size
        
        val sb = StringBuilder()
        
        // 1. 이번 달 전체 긴장도 변화 & 평균적으로 안정되었는지
        val stability = if (allAvg < 35) "전반적으로 매우 안정된 상태를 보여주셨습니다."
                       else if (allAvg < 55) "전반적으로 명상의 균형을 잘 잡아가고 계십니다."
                       else "긴장도가 다소 높았지만, 꾸준한 노력을 기울이셨습니다."
        sb.append("이번 달 명상 분석 결과, $stability ")

        // 2. 명상 시간 및 횟수 변화
        sb.append("총 ${totalCount}회의 명상을 통해 약 ${totalTime}분간 오롯이 자신에게 집중하는 시간을 가지셨네요. ")

        // 3. 월 초 / 월 중 / 월 후반의 변화 추이 (간단 분석)
        val early = sessions.filter { it.date.takeLast(2).toInt() <= 10 }.map { it.avgPressure }.average()
        val late = sessions.filter { it.date.takeLast(2).toInt() >= 20 }.map { it.avgPressure }.average()
        
        if (!early.isNaN() && !late.isNaN()) {
            if (late < early) {
                sb.append("특히 월 초보다 후반으로 갈수록 긴장도가 점차 낮아지며 더욱 안정적인 흐름이 나타났습니다. ")
            } else {
                sb.append("월 초부터 후반까지 꾸준히 안정된 리듬을 유지하려 노력하신 모습이 돋보입니다. ")
            }
        }

        // 4. 명상 습관의 특징
        sb.append("매일의 기록이 쌓여 더욱 성숙한 명상 습관이 형성되고 있습니다. ")

        // 5. 응원 메시지
        sb.append("\n\n다음 달에도 지금처럼 편안한 마음으로 명상을 이어가며 당신의 내면을 따뜻하게 돌보시길 응원합니다!")
        
        return sb.toString()
    }
}

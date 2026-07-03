package com.newritage.app.ui.main.analysis

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.newritage.app.data.AppDatabase
import com.newritage.app.databinding.FragmentWeeklyAnalysisBinding
import com.newritage.app.stats.StatsCalculator
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WeeklyAnalysisFragment : Fragment() {

    private var _binding: FragmentWeeklyAnalysisBinding? = null
    private val binding get() = _binding!!
    private var currentWeekStart = Calendar.getInstance().also {
        it.set(Calendar.DAY_OF_WEEK, it.firstDayOfWeek)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeeklyAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { parentFragmentManager.popBackStack() }

        binding.btnPrevWeek.setOnClickListener {
            currentWeekStart.add(Calendar.WEEK_OF_YEAR, -1)
            loadData()
        }
        binding.btnNextWeek.setOnClickListener {
            currentWeekStart.add(Calendar.WEEK_OF_YEAR, 1)
            loadData()
        }
        setupChart()
        loadData()
    }

    private fun setupChart() {
        binding.lineChart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = Color.parseColor("#5A6B5A")
            xAxis.setDrawGridLines(false)
            axisLeft.textColor = Color.parseColor("#5A6B5A")
            axisRight.isEnabled = false
        }
    }

    private fun loadData() {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val weekEnd = currentWeekStart.clone() as Calendar
        weekEnd.add(Calendar.DAY_OF_WEEK, 6)

        val startStr = sdf.format(currentWeekStart.time)
        val endStr = sdf.format(weekEnd.time)

        binding.tvDateLabel.text = "$startStr ~ $endStr"

        lifecycleScope.launch {
            val db = AppDatabase.getInstance(requireContext())
            val dao = db.sessionDao()

            // ① 이번 주 세션 목록 (총 시간/횟수 계산용)
            val sessions = dao.getSessionsInRange(startStr, endStr)

            // ② 이번 주 원시 데이터 전체 (통계 + 일별 그래프용)
            val readings = dao.getReadingsInRange(startStr, endStr)

            if (sessions.isNotEmpty() && readings.isNotEmpty()) {

                // ③ 한 주치 원시 데이터를 합쳐서 통계 계산 (세션 평균의 평균 X)
                val overallStats = StatsCalculator().calculate(readings.map { it.overall })

                binding.tvAvgPressure1.text = String.format("%.1f", overallStats.avg)
                binding.tvAvgPressure2.text = String.format("%.1f", overallStats.min)
                binding.tvAvgPressure3.text = String.format("%.1f", overallStats.max)

                // ④ 총 명상 시간 / 횟수는 세션 기준 합산
                val totalTime = sessions.sumOf { it.durationSeconds }
                val min = totalTime / 60
                val sec = totalTime % 60
                binding.tvMedTime.text = String.format("%02d:%02d", min, sec)
                binding.tvMedCount.text = sessions.size.toString()

                // ⑤ 일별 평균 압력 그래프 (날짜별로 묶어서 하루 평균 하나씩)
                val dayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val groupedByDay = readings.groupBy { dayFormat.format(it.timestamp) }
                    .toSortedMap()

                val entries = groupedByDay.entries.mapIndexed { index, (_, dayReadings) ->
                    val dayAvg = dayReadings.map { it.overall }.average().toFloat()
                    Entry(index.toFloat(), dayAvg)
                }

                val dataSet = LineDataSet(entries, "주간 압력").apply {
                    color = Color.parseColor("#8B9E7B")
                    setDrawCircles(true)
                    circleRadius = 4f
                    setCircleColor(Color.parseColor("#8B9E7B"))
                    lineWidth = 2f
                    setDrawFilled(true)
                    fillColor = Color.parseColor("#8B9E7B")
                    fillAlpha = 40
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                }
                binding.lineChart.data = LineData(dataSet)
                binding.lineChart.invalidate()

                // TODO: [수빈] 저번 주 대비 비교 로직 (AI 코멘트 또는 단순 증감 계산) 연동 예정
                // 비교하려면 저번 주 range: startStr/endStr에서 -7일 한 값으로 getReadingsInRange 한번 더 호출해서
                // overallStats.avg끼리 비교하면 됨
                binding.tvWeeklyComparison.text = "저번 주 비교를 준비 중입니다."

            } else {
                binding.tvAvgPressure1.text = "--.-"
                binding.tvAvgPressure2.text = "--.-"
                binding.tvAvgPressure3.text = "--.-"
                binding.tvMedTime.text = "--:--"
                binding.tvMedCount.text = "-"
                binding.tvWeeklyComparison.text = "-"
                binding.lineChart.clear()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
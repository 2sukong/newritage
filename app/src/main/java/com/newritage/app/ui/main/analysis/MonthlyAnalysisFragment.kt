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
import com.newritage.app.databinding.FragmentMonthlyAnalysisBinding
import com.newritage.app.stats.StatsCalculator
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MonthlyAnalysisFragment : Fragment() {

    private var _binding: FragmentMonthlyAnalysisBinding? = null
    private val binding get() = _binding!!
    private var currentMonth = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthlyAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnPrevMonth.setOnClickListener {
            currentMonth.add(Calendar.MONTH, -1)
            loadData()
        }

        binding.btnNextMonth.setOnClickListener {
            currentMonth.add(Calendar.MONTH, 1)
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
        val yearMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(currentMonth.time)
        binding.tvDateLabel.text = SimpleDateFormat("yyyy.MM", Locale.getDefault()).format(currentMonth.time)

        lifecycleScope.launch {
            val db = AppDatabase.getInstance(requireContext())
            val dao = db.sessionDao()

            // ① 이번 달의 모든 세션 (총 시간, 총 횟수 계산용)
            val sessions = dao.getSessionsByMonth("$yearMonth%")

            // ② 이번 달의 모든 원시 데이터 (통계 + 일별 그래프용)
            val readings = dao.getReadingsByMonth("$yearMonth%")

            if (sessions.isNotEmpty() && readings.isNotEmpty()) {

                // ③ 한 달치 원시 데이터를 합쳐서 통계 계산
                //    (세션별 평균을 다시 평균 내지 않음 — 일간 때와 같은 원칙)
                val overallStats = StatsCalculator().calculate(readings.map { it.overall })

                binding.tvAvgPressure1.text = String.format("%.1f", overallStats.avg)
                binding.tvAvgPressure2.text = String.format("%.1f", overallStats.max)
                binding.tvAvgPressure3.text = String.format("%.1f", overallStats.min)

                // ④ 총 명상 시간 / 횟수는 세션 기준 그대로 합산
                val totalTime = sessions.sumOf { it.durationSeconds }
                val min = totalTime / 60
                val sec = totalTime % 60
                binding.tvMedTime.text = String.format("%02d:%02d", min, sec)
                binding.tvMedCount.text = sessions.size.toString()

                // ⑤ 일별 평균 압력 그래프
                //    readings를 timestamp 기준으로 "날짜"별로 묶어서 그날의 평균 하나씩 계산
                val dayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val groupedByDay = readings.groupBy { dayFormat.format(it.timestamp) }
                    .toSortedMap()  // 날짜 오름차순 정렬

                val entries = groupedByDay.entries.mapIndexed { index, (_, dayReadings) ->
                    val dayAvg = dayReadings.map { it.overall }.average().toFloat()
                    Entry(index.toFloat(), dayAvg)
                }

                val dataSet = LineDataSet(entries, "월간 압력").apply {
                    color = Color.parseColor("#8B9E7B")
                    setDrawCircles(false)
                    lineWidth = 2f
                    setDrawFilled(true)
                    fillColor = Color.parseColor("#8B9E7B")
                    fillAlpha = 40
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                }
                binding.lineChart.data = LineData(dataSet)
                binding.lineChart.invalidate()

                // TODO: [수빈]이 AI 연동 후 실제 이번 달 코멘트로 교체 예정
                // 활용 가능 데이터: overallStats(avg/min/max), sessions.size, totalTime, groupedByDay(일별 평균 추이)
                binding.tvMonthlyComment.text = "이번 달 분석 코멘트를 준비 중입니다."

            } else {
                binding.tvAvgPressure1.text = "--.-"
                binding.tvAvgPressure2.text = "--.-"
                binding.tvAvgPressure3.text = "--.-"
                binding.tvMedTime.text = "--:--"
                binding.tvMedCount.text = "-"
                binding.tvMonthlyComment.text = "측정된 데이터가 없어 코멘트를 생성할 수 없습니다."
                binding.lineChart.clear()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
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
import com.newritage.app.R
import com.newritage.app.data.AppDatabase
import com.newritage.app.databinding.FragmentDailyAnalysisBinding
import com.newritage.app.stats.StatsCalculator
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DailyAnalysisFragment : Fragment() {

    private var _binding: FragmentDailyAnalysisBinding? = null
    private val binding get() = _binding!!
    private var currentDate = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { parentFragmentManager.popBackStack() }

        binding.btnPrevDay.setOnClickListener {
            currentDate.add(Calendar.DAY_OF_MONTH, -1)
            loadData()
        }
        binding.btnNextDay.setOnClickListener {
            currentDate.add(Calendar.DAY_OF_MONTH, 1)
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
            // 일간 분석용 X축 가이드 (0분, 5분, 10분 형태로 나오도록 격자 조정)
            xAxis.setDrawGridLines(false)
            axisLeft.textColor = Color.parseColor("#5A6B5A")
            axisRight.isEnabled = false
            setGridBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun loadData() {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateStr = sdf.format(currentDate.time)
        val displayStr = SimpleDateFormat("yyyy.MM.dd (E)", Locale.KOREAN).format(currentDate.time)
        binding.tvDateLabel.text = displayStr

        lifecycleScope.launch {
            val db = AppDatabase.getInstance(requireContext())
            val dao = db.sessionDao()

            val sessions = dao.getSessionsByDate(dateStr)
            val readings = dao.getReadingsByDate(dateStr)

            if (sessions.isNotEmpty() && readings.isNotEmpty()) {

                val stats = StatsCalculator().calculateDailyReport(readings)

                val totalDurationSeconds = sessions.sumOf { it.durationSeconds }
                val min = totalDurationSeconds / 60
                val sec = totalDurationSeconds % 60
                val totalVibrationCount = sessions.sumOf { it.vibrationCount }

                binding.tvAvgPressure1.text = String.format("%.1f", stats["overall"]!!.avg)
                binding.tvAvgPressure2.text = String.format("%.1f", stats["overall"]!!.min)
                binding.tvAvgPressure3.text = String.format("%.1f", stats["overall"]!!.max)
                binding.tvMedTime.text = String.format("%02d:%02d", min, sec)

                binding.tvSensorADetail.text =
                    "최고 ${stats["thumb"]!!.max} / 최저 ${stats["thumb"]!!.min} / 평균 ${String.format("%.1f", stats["thumb"]!!.avg)} / 중앙 ${stats["thumb"]!!.median} kPa"
                binding.tvSensorBDetail.text =
                    "최고 ${stats["indexMiddle"]!!.max} / 최저 ${stats["indexMiddle"]!!.min} / 평균 ${String.format("%.1f", stats["indexMiddle"]!!.avg)} / 중앙 ${stats["indexMiddle"]!!.median} kPa"
                binding.tvSensorCDetail.text =
                    "최고 ${stats["palm"]!!.max} / 최저 ${stats["palm"]!!.min} / 평균 ${String.format("%.1f", stats["palm"]!!.avg)} / 중앙 ${stats["palm"]!!.median} kPa"

                val entries = readings.mapIndexed { index, r -> Entry(index.toFloat(), r.overall) }
                val dataSet = LineDataSet(entries, "압력").apply {
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

                // TODO: [수빈]이 AI 연동 후 아래 줄을 실제 코멘트 생성 로직으로 교체
                // 활용 가능 데이터: stats["overall"/"thumb"/"indexMiddle"/"palm"], sessions.size,
                //                    totalDurationSeconds, totalVibrationCount
                binding.tvDailyComment.text = "분석 코멘트를 준비 중입니다."

            } else {
                binding.tvAvgPressure1.text = "--.-"
                binding.tvAvgPressure2.text = "--.-"
                binding.tvAvgPressure3.text = "--.-"
                binding.tvMedTime.text = "--:--"
                binding.tvSensorADetail.text = "데이터가 없습니다."
                binding.tvSensorBDetail.text = "데이터가 없습니다."
                binding.tvSensorCDetail.text = "데이터가 없습니다."
                binding.tvDailyComment.text = "측정된 데이터가 없어 코멘트를 생성할 수 없습니다."
                binding.lineChart.clear()
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
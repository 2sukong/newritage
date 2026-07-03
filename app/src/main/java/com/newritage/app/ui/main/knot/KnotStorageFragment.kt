package com.newritage.app.ui.main.knot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.newritage.app.R
import com.newritage.app.data.AppDatabase
import com.newritage.app.databinding.FragmentKnotStorageBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class KnotStorageFragment : Fragment() {

    companion object {
        // TODO(임시): 실제 매듭 기록 데이터가 아직 없어 3D 뷰어 확인용으로 지난 며칠에 끼워 넣는 샘플. 실제 데이터 연동 후 제거.
        private val SAMPLE_KNOT_MODELS = listOf(
            "models/m_butterfly.glb",
            "models/m_byeong.glb",
            "models/m_gaji.glb"
        )
    }

    private var _binding: FragmentKnotStorageBinding? = null
    private val binding get() = _binding!!
    private var currentCalendar = Calendar.getInstance()
    private val selectedKnotState = mutableStateOf<SelectedKnot?>(null)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKnotStorageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.knotModelPreview.setContent {
            KnotModelPreviewPanel(selected = selectedKnotState.value)
        }
        binding.btnPrevMonth.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, -1)
            loadCalendar()
        }
        binding.btnNextMonth.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, 1)
            loadCalendar()
        }
        loadCalendar()
    }

    private fun loadCalendar() {
        val yearMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(currentCalendar.time)
        binding.tvMonthLabel.text =
            SimpleDateFormat("yyyy년 MM월", Locale.getDefault()).format(currentCalendar.time)

        lifecycleScope.launch {
            val db = AppDatabase.getInstance(requireContext())
            val sessions = db.sessionDao().getSessionsByMonth(yearMonth)
            val dateSet = sessions.map { it.date }.toSet()
            renderCalendar(yearMonth, dateSet)
        }
    }

    private fun renderCalendar(yearMonth: String, dateSet: Set<String>) {
        val grid = binding.calendarGrid
        grid.removeAllViews()
        val daysInMonth = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (day in 1..daysInMonth) {
            val dateStr = "$yearMonth-${String.format("%02d", day)}"
            val sampleModelPath = sampleModelPathForDate(dateStr)
            val hasKnot = dateSet.contains(dateStr) || sampleModelPath != null

            val cellView = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_calendar_cell_knot, grid, false)

            cellView.findViewById<TextView>(R.id.tvDay).text = day.toString()
            cellView.findViewById<View>(R.id.knotIndicator).visibility =
                if (hasKnot) View.VISIBLE else View.INVISIBLE
            if (hasKnot) {
                cellView.setOnClickListener {
                    val modelPath = sampleModelPath ?: "models/knot.glb"
                    selectedKnotState.value = SelectedKnot(dateStr, modelPath)
                }
            }
            grid.addView(cellView)
        }
    }

    // TODO(임시): SAMPLE_KNOT_MODELS와 함께 제거될 샘플 데이터 매핑 함수.
    private fun sampleModelPathForDate(dateStr: String): String? {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        SAMPLE_KNOT_MODELS.forEachIndexed { index, modelPath ->
            val sampleDay = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -(index + 1)) }
            if (formatter.format(sampleDay.time) == dateStr) {
                return modelPath
            }
        }
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

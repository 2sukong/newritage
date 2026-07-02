package com.newritage.app.ui.main.record

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.newritage.app.R
import com.newritage.app.data.AppDatabase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// Kept for backward compat; HomeFragment is now the main landing page
class RecordFragment : Fragment() {
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.fragment_record, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val dao = AppDatabase.getDatabase(requireContext()).sessionDao()
        lifecycleScope.launch {
            val sessions = dao.getSessionsByDate(today)
            val tvNoRecord = view.findViewById<TextView>(R.id.tvNoRecord)
            val tvStats = view.findViewById<TextView>(R.id.tvStats)
            if (sessions.isEmpty()) {
                tvNoRecord?.visibility = View.VISIBLE
                tvStats?.visibility = View.GONE
            } else {
                tvNoRecord?.visibility = View.GONE
                tvStats?.visibility = View.VISIBLE
                val totalSecs = sessions.sumOf { it.durationSeconds }
                tvStats?.text = "오늘 명상 ${sessions.size}회 / 총 ${totalSecs/60}분"
            }
        }
    }
}

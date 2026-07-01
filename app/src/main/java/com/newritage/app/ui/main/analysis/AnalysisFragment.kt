package com.newritage.app.ui.main.analysis

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.newritage.app.R

class AnalysisFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_analysis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<LinearLayout>(R.id.rowDaily).setOnClickListener { openSub(DailyAnalysisFragment()) }
        view.findViewById<LinearLayout>(R.id.rowWeekly).setOnClickListener { openSub(WeeklyAnalysisFragment()) }
        view.findViewById<LinearLayout>(R.id.rowMonthly).setOnClickListener { openSub(MonthlyAnalysisFragment()) }
    }

    private fun openSub(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}

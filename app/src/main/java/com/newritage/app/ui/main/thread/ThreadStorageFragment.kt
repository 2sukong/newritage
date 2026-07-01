package com.newritage.app.ui.main.thread

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.newritage.app.R
import com.newritage.app.data.AppDatabase
import com.newritage.app.data.Session
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ThreadStorageFragment : Fragment() {

    private lateinit var tvMonthYear: TextView
    private lateinit var gridLayout: GridLayout
    private lateinit var btnPrev: ImageButton
    private lateinit var btnNext: ImageButton

    private var currentMonth = Calendar.getInstance()
    private val dao by lazy { AppDatabase.getDatabase(requireContext()).sessionDao() }
    private val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    private val dispSdf = SimpleDateFormat("yyyy년 M월", Locale.getDefault())

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.fragment_thread_storage, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvMonthYear = view.findViewById(R.id.tvMonthYear)
        gridLayout = view.findViewById(R.id.calendarGrid)
        btnPrev = view.findViewById(R.id.btnPrevMonth)
        btnNext = view.findViewById(R.id.btnNextMonth)

        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnPrev.setOnClickListener { currentMonth.add(Calendar.MONTH, -1); loadMonth() }
        btnNext.setOnClickListener { currentMonth.add(Calendar.MONTH, 1); loadMonth() }

        loadMonth()
    }

    private fun loadMonth() {
        val monthStr = sdf.format(currentMonth.time)
        tvMonthYear.text = dispSdf.format(currentMonth.time)
        lifecycleScope.launch {
            val sessions = dao.getSessionsByMonth("$monthStr%")
            val byDate = sessions.filter { it.hasThread }.associateBy { it.date }
            buildCalendar(byDate)
        }
    }

    private fun buildCalendar(byDate: Map<String, Session>) {
        gridLayout.removeAllViews()
        val cal = (currentMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }
        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val dateSdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Grid padding/spacing adjustment for 4 columns
        val displayMetrics = resources.displayMetrics
        val gridWidth = displayMetrics.widthPixels - (48 * displayMetrics.density).toInt() // margins
        val cellSize = gridWidth / 4

        for (day in 1..daysInMonth) {
            cal.set(Calendar.DAY_OF_MONTH, day)
            val dateStr = dateSdf.format(cal.time)
            val session = byDate[dateStr]

            val cell = LayoutInflater.from(requireContext()).inflate(R.layout.item_calendar_cell, gridLayout, false)
            cell.layoutParams = GridLayout.LayoutParams().apply {
                width = cellSize
                height = cellSize
            }
            
            val tvDay = cell.findViewById<TextView>(R.id.tvDay)
            val threadSwatch = cell.findViewById<View>(R.id.threadSwatch)

            tvDay.text = "${day}일"
            if (session != null && session.threadColor.isNotEmpty()) {
                try {
                    val color = Color.parseColor(session.threadColor)
                    val bg = android.graphics.drawable.GradientDrawable().apply {
                        shape = android.graphics.drawable.GradientDrawable.RECTANGLE
                        cornerRadius = 8f * displayMetrics.density
                        setColor(Color.WHITE)
                        setStroke((2 * displayMetrics.density).toInt(), color)
                    }
                    threadSwatch.background = bg
                } catch (e: Exception) {
                    threadSwatch.setBackgroundResource(R.drawable.bg_thread_square)
                }
                threadSwatch.visibility = View.VISIBLE
                cell.setOnClickListener { showThreadDetail(session) }
            } else {
                // Empty slot style
                val bg = android.graphics.drawable.GradientDrawable().apply {
                    shape = android.graphics.drawable.GradientDrawable.RECTANGLE
                    cornerRadius = 8f * displayMetrics.density
                    setStroke((1 * displayMetrics.density).toInt(), Color.parseColor("#E0E0E0"))
                }
                threadSwatch.background = bg
                threadSwatch.visibility = View.VISIBLE
                tvDay.alpha = 0.5f
            }
            gridLayout.addView(cell)
        }
    }

    private fun showThreadDetail(session: Session) {
        val fragment = ThreadDetailFragment.newInstance(session.date)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}

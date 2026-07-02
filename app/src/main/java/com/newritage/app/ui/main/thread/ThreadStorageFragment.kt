package com.newritage.app.ui.main.thread

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.newritage.app.R
import com.newritage.app.data.AppDatabase
import com.newritage.app.data.Session
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ThreadStorageFragment : Fragment() {

    private lateinit var tvCurrentMonth: TextView
    private lateinit var btnPrevMonth: ImageView
    private lateinit var btnNextMonth: ImageView
    
    private var currentCalendar = Calendar.getInstance()
    private val dao by lazy { AppDatabase.getDatabase(requireContext()).sessionDao() }
    
    private val monthFormat = SimpleDateFormat("yyyy년 M월", Locale.getDefault())
    private val dbMonthFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.fragment_thread_storage_new, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupListeners()
        updateCalendar()
    }

    private fun initViews(view: View) {
        tvCurrentMonth = view.findViewById(R.id.tvCurrentMonth)
        btnPrevMonth = view.findViewById(R.id.btnPrevMonth)
        btnNextMonth = view.findViewById(R.id.btnNextMonth)

        view.findViewById<ImageButton>(R.id.back).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, com.newritage.app.ui.main.home.HomeFragment())
                .commit()
        }
    }

    private fun setupListeners() {
        btnPrevMonth.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, -1)
            updateCalendar()
        }
        btnNextMonth.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, 1)
            updateCalendar()
        }
    }

    private fun updateCalendar() {
        tvCurrentMonth.text = monthFormat.format(currentCalendar.time)
        val monthPrefix = dbMonthFormat.format(currentCalendar.time) + "%"

        lifecycleScope.launch {
            val sessions = dao.getSessionsByMonth(monthPrefix)
            val sessionMap = sessions.filter { it.hasThread }.associateBy { 
                // Extract day from yyyy-MM-dd
                it.date.split("-").last().toInt()
            }
            renderGrid(sessionMap)
        }
    }

    private fun renderGrid(sessionMap: Map<Int, Session>) {
        val maxDays = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (day in 1..31) {
            val containerId = resources.getIdentifier("container$day", "id", requireContext().packageName)
            val cellId = resources.getIdentifier("cell$day", "id", requireContext().packageName)
            val textId = resources.getIdentifier("tvDay$day", "id", requireContext().packageName)

            val container = view?.findViewById<LinearLayout>(containerId)
            val cell = view?.findViewById<View>(cellId)
            val textView = view?.findViewById<TextView>(textId)

            if (day > maxDays) {
                container?.visibility = View.INVISIBLE
            } else {
                container?.visibility = View.VISIBLE
                textView?.text = "${day}일"
                
                val session = sessionMap[day]
                if (session != null && session.threadColor.isNotEmpty()) {
                    try {
                        val color = Color.parseColor(session.threadColor)
                        val drawable = GradientDrawable().apply {
                            shape = GradientDrawable.RECTANGLE
                            cornerRadius = 8f * resources.displayMetrics.density
                            setColor(Color.WHITE)
                            setStroke((2 * resources.displayMetrics.density).toInt(), color)
                        }
                        cell?.background = drawable
                    } catch (e: Exception) {
                        cell?.setBackgroundResource(R.drawable.bg_calendar_cell_frame)
                    }
                } else {
                    // Empty state
                    cell?.setBackgroundResource(R.drawable.bg_calendar_cell_frame)
                }
            }
        }
    }
}

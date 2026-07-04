package com.newritage.app.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.newritage.app.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val measureCircleView = view.findViewById<MeasureCircleView>(R.id.measureCircleView)
        measureCircleView.setOnClickListener {
            // Start measurement logic could go here
            measureCircleView.setStatusText(getString(R.string.feedback_measuring))
        }
    }
}

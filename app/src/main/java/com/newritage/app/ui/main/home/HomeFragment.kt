package com.newritage.app.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.newritage.app.R
import com.newritage.app.ui.measurement.MeasurementActivity

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutHelpOverlay = view.findViewById<View>(R.id.layoutHelpOverlay)

        // 도움말 버튼 클릭 시 오버레이 표시
        view.findViewById<View>(R.id.btnHelp).setOnClickListener {
            layoutHelpOverlay.visibility = View.VISIBLE
        }

        // 오버레이 클릭 시 닫기
        layoutHelpOverlay.setOnClickListener {
            layoutHelpOverlay.visibility = View.GONE
        }
        
        // 원형 이미지 클릭 시 MeasurementActivity로 이동
        view.findViewById<View>(R.id.imgMeasureStart).setOnClickListener {
            val intent = Intent(requireContext(), MeasurementActivity::class.java)
            // 액티비티 진입 시 바로 측정을 시작하도록 설정
            intent.putExtra("auto_start", true)
            startActivity(intent)
        }
    }
}

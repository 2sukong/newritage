package com.newritage.app.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.newritage.app.R
import com.newritage.app.ui.main.MainActivity

class IntroFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<MaterialButton>(R.id.btnStart).setOnClickListener {
            // 메인 액티비티의 메서드를 호출하여 홈(준비 페이지)으로 이동
            (activity as? MainActivity)?.loadFragment(HomeFragment(), showNav = true)
        }
    }
}

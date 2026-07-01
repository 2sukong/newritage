package com.newritage.app.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.newritage.app.R
import com.newritage.app.ui.main.analysis.AnalysisFragment
import com.newritage.app.ui.main.home.HomeFragment
import com.newritage.app.ui.main.home.IntroFragment
import com.newritage.app.ui.main.knot.KnotStorageFragment
import com.newritage.app.ui.main.thread.ThreadStorageFragment

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNavigation)
        
        if (savedInstanceState == null) {
            // 앱 실행 시 첫 화면은 IntroFragment (하단바 숨김)
            loadFragment(IntroFragment(), showNav = false)
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { loadFragment(HomeFragment(), showNav = true); true }
                R.id.nav_thread -> { loadFragment(ThreadStorageFragment(), showNav = true); true }
                R.id.nav_knot -> { loadFragment(KnotStorageFragment(), showNav = true); true }
                R.id.nav_analysis -> { loadFragment(AnalysisFragment(), showNav = true); true }
                R.id.nav_settings -> true
                else -> false
            }
        }
    }

    fun loadFragment(fragment: Fragment, showNav: Boolean) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
            
        bottomNav.visibility = if (showNav) View.VISIBLE else View.GONE
    }

    fun navigateToHome() {
        bottomNav.selectedItemId = R.id.nav_home
    }
}

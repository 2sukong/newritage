package com.newritage.app.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.newritage.app.R
import com.newritage.app.data.UserPreferences
import com.newritage.app.databinding.ActivityDebugSettingsBinding
import com.newritage.app.ui.onboarding.SplashActivity

/**
 * 설정 탭 맨 아래 "디버그" 항목에서 진입하는 개발/시연용 옵션 화면.
 * 토글 3종을 [UserPreferences]에 저장하며, 각 토글의 실제 동작은 이를 읽는 곳
 * (MainActivity 날짜바/측정 시작, 실·매듭 보관함 표시)에서 처리한다.
 */
class DebugSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDebugSettingsBinding
    private lateinit var prefs: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDebugSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = UserPreferences(this)

        binding.btnBack.setOnClickListener { finishWithSlideBack() }

        binding.switchDebugDateBar.isChecked = prefs.debugDateBarVisible
        binding.switchDebugDateBar.setOnCheckedChangeListener { _, checked ->
            prefs.debugDateBarVisible = checked
        }

        binding.switchDebugShowAll.isChecked = prefs.debugShowAllCollection
        binding.switchDebugShowAll.setOnCheckedChangeListener { _, checked ->
            prefs.debugShowAllCollection = checked
        }

        binding.switchDebugRequireBle.isChecked = prefs.requireBleConnectionToStart
        binding.switchDebugRequireBle.setOnCheckedChangeListener { _, checked ->
            prefs.requireBleConnectionToStart = checked
        }

        binding.rowDebugRestartOnboarding.setOnClickListener { confirmRestartOnboarding() }
    }

    /** 온보딩→로그인→기준 압력 측정을 다시 거치도록 초기화한다. 세션·실·매듭 기록은 유지된다. */
    private fun confirmRestartOnboarding() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.debug_restart_onboarding_confirm_title)
            .setMessage(R.string.debug_restart_onboarding_confirm_message)
            .setPositiveButton(R.string.debug_restart_onboarding_confirm_action) { _, _ ->
                prefs.resetOnboardingAndBaseline()
                val intent = Intent(this, SplashActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun finishWithSlideBack() {
        finish()
        @Suppress("DEPRECATION")
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        @Suppress("DEPRECATION")
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}

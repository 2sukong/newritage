package com.newritage.app.ble

/** 진동 설정 화면(용도) */
enum class VibrationType {
    TIMER, TENSION
}

/** 진동 카테고리. 표시 순서 = 선언 순서. */
enum class VibrationCategory(val label: String) {
    SINGLE_CLICK("단발 클릭"),
    REPEAT("연타"),
    SUSTAINED_ALERT("지속·알림"),
    PULSE("맥동"),
    HUM_TRANSITION("허밍·전환"),
    RAMP_UP("상승 램프"),
    RAMP_DOWN("하강 램프")
}

/**
 * 선택 가능한 진동 패턴. 기기의 DRV2605L 라이브러리 효과 번호(1~123)를
 * [count]회, [intervalMs](ms) 간격으로 BleManager.sendVibration()에 그대로 전달한다.
 * 폰 자체 진동(Vibrator)은 사용하지 않는다 — 판단·설정은 앱, 실행은 기기라는 원칙에 따름.
 *
 * [screens]는 이 패턴을 선택 목록에 노출할 화면(용도)의 집합이다. 상승/하강 램프 계열처럼
 * 사용자가 직접 고르지 않고 [BreathingCues] 같은 고정 큐로만 쓰이는 패턴은 비워둔다.
 */
data class VibrationPattern(
    val id: String,
    val name: String,
    val category: VibrationCategory,
    val effect: Int,
    val count: Int = 1,
    val intervalMs: Int = 0,
    val screens: Set<VibrationType> = emptySet()
)

/** 확정된 37종 진동 목록. 카테고리 블록 순서 = 화면에 노출되는 섹션 순서. */
object VibrationPatterns {

    private val TIMER_ONLY = setOf(VibrationType.TIMER)
    private val TENSION_ONLY = setOf(VibrationType.TENSION)
    private val TIMER_AND_TENSION = setOf(VibrationType.TIMER, VibrationType.TENSION)

    val ALL = listOf(
        // 단발 클릭 — 타이머/긴장 알림 공용
        VibrationPattern("e1", "강한 클릭", VibrationCategory.SINGLE_CLICK, effect = 1, screens = TIMER_AND_TENSION),
        VibrationPattern("e21", "중간 클릭", VibrationCategory.SINGLE_CLICK, effect = 21, screens = TIMER_AND_TENSION),
        VibrationPattern("e4", "날카로운 클릭", VibrationCategory.SINGLE_CLICK, effect = 4, screens = TIMER_AND_TENSION),
        VibrationPattern("e24", "예리한 틱", VibrationCategory.SINGLE_CLICK, effect = 24, screens = TIMER_AND_TENSION),
        VibrationPattern("e7", "부드러운 툭", VibrationCategory.SINGLE_CLICK, effect = 7, screens = TIMER_AND_TENSION),
        VibrationPattern("e13", "소프트 퍼즈", VibrationCategory.SINGLE_CLICK, effect = 13, screens = TIMER_AND_TENSION),

        // 연타 — 타이머 종료 전용
        VibrationPattern("e10", "이중 탭", VibrationCategory.REPEAT, effect = 10, screens = TIMER_ONLY),
        VibrationPattern("e12", "삼중 탭", VibrationCategory.REPEAT, effect = 12, screens = TIMER_ONLY),
        VibrationPattern("e27", "짧은 이중 탭(강)", VibrationCategory.REPEAT, effect = 27, screens = TIMER_ONLY),
        VibrationPattern("e31", "짧은 이중 탭(중)", VibrationCategory.REPEAT, effect = 31, screens = TIMER_ONLY),
        VibrationPattern("e34", "짧은 이중 틱", VibrationCategory.REPEAT, effect = 34, screens = TIMER_ONLY),
        VibrationPattern("e37", "긴 이중 클릭(강)", VibrationCategory.REPEAT, effect = 37, screens = TIMER_ONLY),
        VibrationPattern("e41", "긴 이중 클릭(중)", VibrationCategory.REPEAT, effect = 41, screens = TIMER_ONLY),
        VibrationPattern("e44", "긴 이중 틱", VibrationCategory.REPEAT, effect = 44, screens = TIMER_ONLY),

        // 지속·알림 — 타이머 종료 전용. 단, "버즈"(47)는 약한 알림이라 긴장 알림에도 노출.
        VibrationPattern("e14", "강한 버즈", VibrationCategory.SUSTAINED_ALERT, effect = 14, screens = TIMER_ONLY),
        VibrationPattern("e47", "버즈", VibrationCategory.SUSTAINED_ALERT, effect = 47, screens = TIMER_AND_TENSION),
        VibrationPattern("e15", "알림(0.75초)", VibrationCategory.SUSTAINED_ALERT, effect = 15, screens = TIMER_ONLY),
        VibrationPattern("e16", "알림(1초)", VibrationCategory.SUSTAINED_ALERT, effect = 16, screens = TIMER_ONLY),
        VibrationPattern("e118", "긴 버즈(중단 가능)", VibrationCategory.SUSTAINED_ALERT, effect = 118, screens = TIMER_ONLY),

        // 맥동 — 긴장 알림 전용
        VibrationPattern("e52", "강한 맥동", VibrationCategory.PULSE, effect = 52, screens = TENSION_ONLY),
        VibrationPattern("e54", "은은한 맥동", VibrationCategory.PULSE, effect = 54, screens = TENSION_ONLY),
        VibrationPattern("e56", "또렷한 맥동", VibrationCategory.PULSE, effect = 56, screens = TENSION_ONLY),

        // 허밍·전환 — 긴장 알림 전용
        VibrationPattern("e58", "전환 클릭", VibrationCategory.HUM_TRANSITION, effect = 58, screens = TENSION_ONLY),
        VibrationPattern("e64", "스며드는 허밍", VibrationCategory.HUM_TRANSITION, effect = 64, screens = TENSION_ONLY),
        VibrationPattern("e119", "잔잔한 허밍", VibrationCategory.HUM_TRANSITION, effect = 119, screens = TENSION_ONLY),

        // 상승 램프 — 선택 목록 비노출. 호흡 가이드 큐(BreathingCues.INHALE 등) 전용 소재.
        VibrationPattern("e82", "차오름(길게, 부드럽게)", VibrationCategory.RAMP_UP, effect = 82),
        VibrationPattern("e84", "차오름(중간, 부드럽게)", VibrationCategory.RAMP_UP, effect = 84),
        VibrationPattern("e86", "차오름(짧게, 부드럽게)", VibrationCategory.RAMP_UP, effect = 86),
        VibrationPattern("e88", "차오름(길게, 또렷하게)", VibrationCategory.RAMP_UP, effect = 88),
        VibrationPattern("e90", "차오름(중간, 또렷하게)", VibrationCategory.RAMP_UP, effect = 90),
        VibrationPattern("e92", "차오름(짧게, 또렷하게)", VibrationCategory.RAMP_UP, effect = 92),

        // 하강 램프 — 선택 목록 비노출. 호흡 가이드 큐(BreathingCues.EXHALE 등) 전용 소재.
        VibrationPattern("e70", "잦아듦(길게, 부드럽게)", VibrationCategory.RAMP_DOWN, effect = 70),
        VibrationPattern("e72", "잦아듦(중간, 부드럽게)", VibrationCategory.RAMP_DOWN, effect = 72),
        VibrationPattern("e74", "잦아듦(짧게, 부드럽게)", VibrationCategory.RAMP_DOWN, effect = 74),
        VibrationPattern("e76", "잦아듦(길게, 또렷하게)", VibrationCategory.RAMP_DOWN, effect = 76),
        VibrationPattern("e78", "잦아듦(중간, 또렷하게)", VibrationCategory.RAMP_DOWN, effect = 78),
        VibrationPattern("e80", "잦아듦(짧게, 또렷하게)", VibrationCategory.RAMP_DOWN, effect = 80)
    )

    /** 특정 화면(용도)에 노출할 패턴만, 카테고리 선언 순서를 유지해 반환한다. */
    fun forScreen(type: VibrationType): List<VibrationPattern> = ALL.filter { type in it.screens }

    /** 타이머 진동 기본값: 이중 탭(effect 10) */
    val TIMER_DEFAULT_ID = "e10"

    /** 긴장 알림 진동 기본값: 부드러운 툭(effect 7) */
    val TENSION_DEFAULT_ID = "e7"
}

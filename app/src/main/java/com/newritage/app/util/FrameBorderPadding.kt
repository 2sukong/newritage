package com.newritage.app.util

import android.view.View

/**
 * calender_frame.webp는 9-patch가 아닌 일반 비트맵이라 배경으로 쓰이면 뷰 크기에 맞춰
 * 가로/세로 각각 다른 비율로 늘어난다(가로 테두리 두께 비율 ≈ 이미지 너비의 7.5%,
 * 세로 테두리 두께 비율 ≈ 이미지 높이의 4.5% — 실제 픽셀 측정값). 그래서 고정 dp
 * padding은 폭이 넓어지는 태블릿에서 테두리보다 얇아져 그리드 칸이 테두리에 걸쳐
 * 잘려 보인다. 실제 렌더링 크기에 비례한 padding을 매 레이아웃마다 계산해 적용한다.
 * 실/매듭 보관함 두 화면이 반드시 같은 비율을 써야 테두리~내용 여백이 서로 같다.
 *
 * padding은 [frameContainer](calender_frame 배경을 가진, layout_height=0dp+weight=1인 뷰)가
 * 아니라 그 안의 [contentView](match_parent/match_parent인 단순 자식)에 적용한다.
 * frameContainer 자신에 setPadding하면 weight로 크기가 정해지는 조상 체인까지 relayout이
 * 안정적으로 전파되지 않아 실제 자식 배치에 반영되지 않는 경우가 있었다 — 크기가 이미
 * 고정된 contentView에 적용하면 자신의 자식만 다시 배치하면 되므로 항상 반영된다.
 */
private const val FRAME_BORDER_RATIO_HORIZONTAL = 0.075f
private const val FRAME_BORDER_RATIO_VERTICAL = 0.045f

fun applyCalendarFrameBorderPadding(frameContainer: View, contentView: View) {
    frameContainer.addOnLayoutChangeListener { _, left, top, right, bottom, _, _, _, _ ->
        val width = right - left
        val height = bottom - top
        if (width <= 0 || height <= 0) return@addOnLayoutChangeListener

        val horizontal = (width * FRAME_BORDER_RATIO_HORIZONTAL).toInt()
        val vertical = (height * FRAME_BORDER_RATIO_VERTICAL).toInt()
        if (contentView.paddingLeft != horizontal || contentView.paddingTop != vertical) {
            contentView.setPadding(horizontal, vertical, horizontal, vertical)
        }
    }
}

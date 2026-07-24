package com.newritage.app.util

import android.graphics.Bitmap
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.core.view.drawToBitmap

/**
 * 팝업/바텀시트 뒤에 보이는 화면을 캡처해 frosted-glass 스타일 배경으로 씌우는 유틸.
 * RenderEffect 블러는 API 31(S) 이상에서만 실제로 렌더링되며, 그 미만에서는
 * 반투명 흰색 오버레이만 적용된다(요청된 "최대한 구현" 범위).
 */
object BackdropBlur {

    /**
     * [source]에서 [target]이 실제로 화면에 덮는 영역만큼만 잘라내 [target]에 세팅한다
     * (팝업/바텀시트 배경용). [target]의 화면상 y좌표(getLocationOnScreen)를 기준으로 자르므로,
     * target이 source의 하단에 딱 붙어 있지 않은 경우(예: 상태바 아래에서 시작하고 하단
     * 제스처 영역만큼은 못 미치는 풀스크린 다이얼로그)에도 항상 실제로 덮는 영역과 정확히
     * 일치하게 잘린다 — 예전엔 "source 전체 높이 - target 높이"로만 시작 y를 구해서
     * target이 source 하단에 붙어 있다고 가정했는데, 이 가정이 깨지면(예: HelpImageDialog의
     * helpDialogRoot가 상태바만큼 아래에서 시작하고 하단 제스처 여백만큼은 못 미침) 블러
     * 배경이 실제 화면보다 위로 밀려 보이는 어긋남이 생겼다.
     * [blurRadiusDp]는 dp 단위다 — 예전엔 raw px를 그대로 RenderEffect에 넘겨서, 태블릿처럼
     * 해상도/밀도가 높은 기기에서는 같은 px 값이 상대적으로 훨씬 약하게(거의 안 흐려진 것처럼)
     * 보이는 문제가 있었다. 여기서 기기 density를 곱해 화면 밀도와 무관하게 항상 같은 시각적
     * 흐림 정도를 내도록 한다.
     */
    fun applyBottomCropTo(source: View, target: ImageView, cropHeightPx: Int, blurRadiusDp: Float = 20f) {
        if (source.width == 0 || source.height == 0 || cropHeightPx <= 0) return
        val full = runCatching { source.drawToBitmap() }.getOrNull() ?: return

        val sourceLocation = IntArray(2).also { source.getLocationOnScreen(it) }
        val targetLocation = IntArray(2).also { target.getLocationOnScreen(it) }
        val startY = (targetLocation[1] - sourceLocation[1]).coerceIn(0, full.height - 1)
        val height = cropHeightPx.coerceAtMost(full.height - startY)
        val cropped = runCatching { Bitmap.createBitmap(full, 0, startY, full.width, height) }.getOrNull() ?: return
        target.setImageBitmap(cropped)
        applyBlur(target, blurRadiusDp * target.resources.displayMetrics.density)
    }

    private fun applyBlur(target: ImageView, blurRadiusPx: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            target.setRenderEffect(
                RenderEffect.createBlurEffect(blurRadiusPx, blurRadiusPx, Shader.TileMode.CLAMP)
            )
        }
    }
}

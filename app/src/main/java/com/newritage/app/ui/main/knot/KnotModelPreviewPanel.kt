package com.newritage.app.ui.main.knot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class SelectedKnot(val date: String, val modelAssetPath: String)

// 매듭 색상 커스터마이징 지점. 원하는 색으로 바꿔서 확인해보면 된다.
private val KNOT_TINT_COLOR = Color(0xFFDFE1DB)

/**
 * 매듭 보관함 화면에 임베드되는 3D 미리보기 패널.
 * 팝업 다이얼로그 대신 화면 일부(고정 높이 영역)에 계속 떠 있으며,
 * 캘린더에서 매듭이 있는 날짜를 탭할 때마다 [selected]가 바뀌어 내용이 갱신된다.
 */
@Composable
fun KnotModelPreviewPanel(
    selected: SelectedKnot?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (selected == null) {
            Text(
                text = "매듭이 있는 날짜를 탭해보세요",
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = selected.date,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                KnotModelViewer(
                    glbAssetPath = selected.modelAssetPath,
                    tintColor = KNOT_TINT_COLOR,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}

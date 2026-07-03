package com.newritage.app.ui.main.knot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.filament.MaterialInstance
import io.github.sceneview.Scene
import io.github.sceneview.material.setColor
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes

// glTF 표준 PBR 머티리얼의 색상 파라미터 이름 후보. Filament의 gltfio 임포터가 생성하는
// 머티리얼에 존재하는 이름만 실제로 적용되고, 나머지는 조용히 무시된다.
private val BASE_COLOR_PARAMETER_CANDIDATES = listOf("baseColorFactor", "baseColor")

private fun MaterialInstance.tryTintBaseColor(color: Color) {
    for (paramName in BASE_COLOR_PARAMETER_CANDIDATES) {
        try {
            setColor(paramName, color)
            return
        } catch (_: IllegalArgumentException) {
            // 이 머티리얼에 없는 파라미터 이름이면 다음 후보를 시도한다.
        }
    }
}

/**
 * assets/[glbAssetPath] 위치의 GLB 매듭 모델을 회전/줌 제스처와 함께 보여준다.
 *
 * @param tintColor null이면 모델에 저장된 원래 색(현재 샘플은 전부 핑크)을 그대로 쓰고,
 *   값을 주면 모든 머티리얼의 기본색을 이 색으로 덮어쓴다.
 * @param modelRotation 모델을 세워서 정면이 보이도록 하는 초기 회전값(오일러 각도, degree).
 *   현재 샘플 GLB가 전부 눕혀진 채로 내보내져 있어 기본값으로 X축 -90도를 적용했다.
 *   내보내기 도구/모델마다 다를 수 있으니 실제로 확인하며 조정해야 한다.
 * @param cameraDistance 정면에서 모델까지의 초기 카메라 거리.
 */
@Composable
fun KnotModelViewer(
    glbAssetPath: String,
    modifier: Modifier = Modifier,
    tintColor: Color? = null,
    backgroundColor: Color = Color(0xFFF4F5EF),
    modelRotation: Rotation = Rotation(x = -90f, y = 0f, z = 0f),
    cameraDistance: Float = 3f
) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val childNodes = rememberNodes()
    val cameraManipulator = rememberCameraManipulator(
        orbitHomePosition = Position(x = 0f, y = 0f, z = cameraDistance),
        targetPosition = Position(0f, 0f, 0f)
    )
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(glbAssetPath, tintColor, modelRotation) {
        val modelInstance = modelLoader.loadModelInstance(glbAssetPath)
        if (modelInstance != null) {
            if (tintColor != null) {
                modelInstance.materialInstances.forEach { materialInstance ->
                    materialInstance.tryTintBaseColor(tintColor)
                }
            }
            childNodes.add(
                ModelNode(
                    modelInstance = modelInstance,
                    autoAnimate = true,
                    scaleToUnits = 1f,
                    centerOrigin = Position(0f, 0f, 0f)
                ).apply {
                    rotation = modelRotation
                }
            )
        }
        isLoading = false
    }

    Box(modifier = modifier.background(backgroundColor)) {
        Scene(
            modifier = Modifier.fillMaxSize(),
            engine = engine,
            modelLoader = modelLoader,
            isOpaque = false,
            cameraManipulator = cameraManipulator,
            childNodes = childNodes
        )
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

package com.newritage.app.ui.main.knot

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
import io.github.sceneview.Scene
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes

/**
 * assets/[glbAssetPath] 위치의 GLB 매듭 모델을 회전/줌 제스처와 함께 보여준다.
 */
@Composable
fun KnotModelViewer(
    glbAssetPath: String,
    modifier: Modifier = Modifier
) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val childNodes = rememberNodes()
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(glbAssetPath) {
        val modelInstance = modelLoader.loadModelInstance(glbAssetPath)
        if (modelInstance != null) {
            childNodes.add(
                ModelNode(
                    modelInstance = modelInstance,
                    autoAnimate = true,
                    scaleToUnits = 1f
                )
            )
        }
        isLoading = false
    }

    Box(modifier = modifier) {
        Scene(
            modifier = Modifier.fillMaxSize(),
            engine = engine,
            modelLoader = modelLoader,
            childNodes = childNodes
        )
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

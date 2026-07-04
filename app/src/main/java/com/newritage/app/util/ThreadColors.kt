package com.newritage.app.util

object ThreadColors {

    data class ThreadColor(
        val hex: String,
        val nameKr: String,
        val level: Int
    )

    private val colors = listOf(
        ThreadColor("#7EB693", "차분한 초록", 1), // Low pressure
        ThreadColor("#8BA6C1", "잔잔한 파랑", 2), // Medium low
        ThreadColor("#D4B572", "따뜻한 노랑", 3), // Medium high
        ThreadColor("#C78B8B", "불안한 빨강", 4)  // High pressure
    )

    fun assignColor(pressure: Float): ThreadColor {
        return when {
            pressure < 30f -> colors[0]
            pressure < 50f -> colors[1]
            pressure < 70f -> colors[2]
            else -> colors[3]
        }
    }
}

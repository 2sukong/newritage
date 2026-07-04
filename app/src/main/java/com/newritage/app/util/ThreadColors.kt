package com.newritage.app.util

object ThreadColors {
    data class ThreadColor(val nameKr: String, val hex: String, val level: String)

    val LOW = listOf(
        ThreadColor("옥색",   "#8FAD9E", "낮음"),
        ThreadColor("비취색", "#5B9070", "낮음"),
        ThreadColor("벽청색", "#7590B0", "낮음"),
        ThreadColor("쪽색",   "#4A5880", "낮음")
    )

    val MEDIUM = listOf(
        ThreadColor("개나리색", "#EDD055", "보통"),
        ThreadColor("유채색",  "#D4A820", "보통"),
        ThreadColor("송화색",  "#C8C460", "보통"),
        ThreadColor("버들색",  "#8AA050", "보통")
    )

    val HIGH = listOf(
        ThreadColor("동백색", "#C07070", "높음"),
        ThreadColor("석류색", "#A04558", "높음"),
        ThreadColor("주홍색", "#CC6040", "높음"),
        ThreadColor("단풍색", "#C07535", "높음")
    )

    val ALL = LOW + MEDIUM + HIGH

    /** Placeholder: returns a random color. Real algorithm TBD. */
    fun assignColor(avgPressure: Float): ThreadColor {
        return ALL.random()
    }

    fun findByHex(hex: String): ThreadColor? = ALL.firstOrNull { it.hex.equals(hex, true) }
}

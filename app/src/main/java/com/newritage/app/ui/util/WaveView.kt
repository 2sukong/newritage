package com.newritage.app.ui.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.sin

class WaveView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF5A7F6F.toInt()
        style = Paint.Style.FILL
        alpha = 40
    }

    private var pressure: Float = 0f
    private var phase = 0f

    fun setPressure(value: Float) {
        this.pressure = value
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val maxRadius = Math.min(width, height) / 2f

        // Draw multiple circles with different phases to simulate waves
        for (i in 1..3) {
            val radius = (maxRadius * (pressure / 100f) * (1 + 0.1f * sin(phase + i))).coerceIn(0f, maxRadius)
            canvas.drawCircle(centerX, centerY, radius, paint)
        }
        
        phase += 0.05f
        if (pressure > 0) {
            postInvalidateOnAnimation()
        }
    }
}

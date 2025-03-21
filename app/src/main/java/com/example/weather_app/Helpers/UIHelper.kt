package com.example.weather_app.utils

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Typeface
import android.widget.TextView
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin

fun applyGradientToTemperatureText(tempTextView: TextView) {
    val angle = 90
    val radians = toRadians(angle.toDouble())

    val width = tempTextView.paint.measureText(tempTextView.text.toString())
    val height = tempTextView.textSize

    val endX = (cos(radians) * width).toFloat()
    val endY = (sin(radians) * height).toFloat()

    val shader = LinearGradient(
        0f, 0f, endX, endY, intArrayOf(
            0xFFFFFFFF.toInt(), 0xFFe8e8e8.toInt(), 0xFFb1b1b1.toInt(), 0xFF707070.toInt()
        ),
        null, Shader.TileMode.CLAMP
    )

    tempTextView.paint.shader = shader
    tempTextView.typeface = Typeface.DEFAULT_BOLD
}
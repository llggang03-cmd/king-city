package com.kingcity.game.ui.game

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate

fun DrawScope.drawTopDownCar(
    centerX: Float,
    centerY: Float,
    length: Float,
    width: Float,
    bodyColor: Color,
    accentColor: Color,
    angleRadians: Float,
    isPolice: Boolean = false
) {
    val degrees = Math.toDegrees(angleRadians.toDouble()).toFloat()
    rotate(degrees = degrees, pivot = Offset(centerX, centerY)) {
        val left = centerX - length / 2f
        val top = centerY - width / 2f

        drawRoundRect(
            color = Color.Black.copy(alpha = 0.25f),
            topLeft = Offset(left + 3f, top + 3f),
            size = Size(length, width),
            cornerRadius = CornerRadius(width * 0.3f, width * 0.3f)
        )
        drawRoundRect(
            color = bodyColor,
            topLeft = Offset(left, top),
            size = Size(length, width),
            cornerRadius = CornerRadius(width * 0.3f, width * 0.3f)
        )
        drawRoundRect(
            color = accentColor,
            topLeft = Offset(left + length * 0.28f, top + width * 0.12f),
            size = Size(length * 0.42f, width * 0.76f),
            cornerRadius = CornerRadius(width * 0.18f, width * 0.18f)
        )
        drawCircle(color = Color(0xFFFFF6C4), radius = width * 0.12f, center = Offset(left + length * 0.94f, top + width * 0.22f))
        drawCircle(color = Color(0xFFFFF6C4), radius = width * 0.12f, center = Offset(left + length * 0.94f, top + width * 0.78f))

        if (isPolice) {
            drawRect(
                color = Color(0xFF1565C0),
                topLeft = Offset(left + length * 0.42f, top - width * 0.1f),
                size = Size(length * 0.16f, width * 0.18f)
            )
            drawRect(
                color = Color(0xFFE53935),
                topLeft = Offset(left + length * 0.42f, top + width * 1.02f - width * 0.08f),
                size = Size(length * 0.16f, width * 0.18f)
            )
        }
    }
}

package com.kingcity.game.ui.game

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

fun DrawScope.drawCharacterAvatar(
    centerX: Float,
    centerY: Float,
    scale: Float,
    shirtColor: Color,
    accentColor: Color
) {
    val headR = 24f * scale
    val headCenter = Offset(centerX, centerY - 30f * scale)

    drawRoundRect(
        color = shirtColor,
        topLeft = Offset(centerX - 34f * scale, centerY - 4f * scale),
        size = Size(68f * scale, 50f * scale),
        cornerRadius = CornerRadius(14f * scale, 14f * scale)
    )
    drawRoundRect(
        color = accentColor,
        topLeft = Offset(centerX - 34f * scale, centerY - 4f * scale),
        size = Size(68f * scale, 14f * scale),
        cornerRadius = CornerRadius(10f * scale, 10f * scale)
    )
    drawCircle(color = Color(0xFFFFC72C), radius = 5f * scale, center = Offset(centerX, centerY + 14f * scale))

    drawCircle(color = Color(0xFFE0A96D), radius = headR, center = headCenter)
    drawRoundRect(
        color = Color(0xFF2B2320),
        topLeft = Offset(headCenter.x - headR, headCenter.y - headR - 2f * scale),
        size = Size(headR * 2f, headR * 0.9f),
        cornerRadius = CornerRadius(headR * 0.6f, headR * 0.6f)
    )
    drawRoundRect(
        color = Color(0xFF1A1A1A),
        topLeft = Offset(headCenter.x - headR * 0.75f, headCenter.y - headR * 0.15f),
        size = Size(headR * 1.5f, headR * 0.45f),
        cornerRadius = CornerRadius(headR * 0.2f, headR * 0.2f)
    )
}

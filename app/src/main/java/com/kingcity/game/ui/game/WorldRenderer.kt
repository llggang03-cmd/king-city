package com.kingcity.game.ui.game

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import com.kingcity.game.data.CarDef
import com.kingcity.game.data.CharacterDef
import com.kingcity.game.game.CityMap
import com.kingcity.game.game.GameEngine
import com.kingcity.game.game.LandmarkType
import com.kingcity.game.ui.theme.NeonGold
import com.kingcity.game.ui.theme.RoadAsphalt
import com.kingcity.game.ui.theme.RoadLine
import com.kingcity.game.ui.theme.SandTan
import com.kingcity.game.ui.theme.WaterBlue
import com.kingcity.game.ui.theme.WaterBlueDark

private val buildingColors = listOf(
    Color(0xFF3A2E6B), Color(0xFF4A2E5C), Color(0xFF2E3A6B), Color(0xFF5C3A2E)
)

private val labelPaint = Paint().apply {
    color = android.graphics.Color.WHITE
    textSize = 30f
    isAntiAlias = true
    typeface = Typeface.DEFAULT_BOLD
    setShadowLayer(6f, 0f, 0f, android.graphics.Color.BLACK)
}

fun DrawScope.drawWorld(engine: GameEngine, cityMap: CityMap, playerCar: CarDef, playerCharacter: CharacterDef) {
    val w = size.width
    val h = size.height

    val camX = engine.playerX - w / 2f
    val camY = engine.playerY - h / 2f

    drawRect(color = SandTan, topLeft = Offset(0f, 0f), size = Size(w, h))

    val waterScreenX = cityMap.waterStartX - camX
    if (waterScreenX < w) {
        drawRect(
            brush = Brush.horizontalGradient(listOf(WaterBlue, WaterBlueDark)),
            topLeft = Offset(waterScreenX.coerceAtLeast(0f), 0f),
            size = Size(w - waterScreenX.coerceAtLeast(0f), h)
        )
    }

    for (bridge in cityMap.bridges) {
        val sx = bridge.x - camX
        val syTop = bridge.yTop - camY
        drawRect(color = RoadAsphalt, topLeft = Offset(sx, syTop), size = Size(bridge.width, bridge.yBottom - bridge.yTop))
    }

    var rx = (camX / cityMap.roadSpacing).toInt() * cityMap.roadSpacing
    while (rx < camX + w + cityMap.roadSpacing) {
        val screenX = rx - camX
        drawRect(color = RoadAsphalt, topLeft = Offset(screenX, 0f), size = Size(cityMap.roadWidth, h))
        rx += cityMap.roadSpacing
    }
    var ry = (camY / cityMap.roadSpacing).toInt() * cityMap.roadSpacing
    while (ry < camY + h + cityMap.roadSpacing) {
        val screenY = ry - camY
        drawRect(color = RoadAsphalt, topLeft = Offset(0f, screenY), size = Size(w, cityMap.roadWidth))
        ry += cityMap.roadSpacing
    }

    for (b in cityMap.buildings) {
        val sx = b.x - camX
        val sy = b.y - camY
        if (sx + b.w < 0 || sx > w || sy + b.h < 0 || sy > h) continue
        drawRect(color = buildingColors[b.colorIndex], topLeft = Offset(sx, sy), size = Size(b.w, b.h))
        drawRect(color = Color.Black.copy(alpha = 0.25f), topLeft = Offset(sx, sy), size = Size(b.w, b.h), style = Stroke(width = 3f))
        var wy = sy + 14f
        while (wy < sy + b.h - 10f) {
            var wx = sx + 14f
            while (wx < sx + b.w - 10f) {
                drawRect(color = Color(0xFFFFE082).copy(alpha = 0.55f), topLeft = Offset(wx, wy), size = Size(8f, 8f))
                wx += 22f
            }
            wy += 22f
        }
    }

    for (lm in cityMap.landmarks) {
        val sx = lm.x - camX
        val sy = lm.y - camY
        if (sx + lm.size < 0 || sx - lm.size > w || sy + lm.size < 0 || sy - lm.size > h) continue
        drawLandmark(lm.type, sx, sy, lm.size)
    }

    for ((tx, ty) in cityMap.palmTrees) {
        val sx = tx - camX
        val sy = ty - camY
        if (sx < -20f || sx > w + 20f || sy < -20f || sy > h + 20f) continue
        drawRect(color = Color(0xFF6D4C2E), topLeft = Offset(sx - 3f, sy), size = Size(6f, 26f))
        drawCircle(color = Color(0xFF2E7D32), radius = 16f, center = Offset(sx, sy - 6f))
    }

    var dashX = (camX / cityMap.roadSpacing).toInt() * cityMap.roadSpacing
    while (dashX < camX + w + cityMap.roadSpacing) {
        var dy = -((camY.toInt() % 60))
        while (dy < h) {
            drawRect(color = RoadLine.copy(alpha = 0.6f), topLeft = Offset(dashX - camX + cityMap.roadWidth / 2f - 2f, dy.toFloat()), size = Size(4f, 20f))
            dy += 60
        }
        dashX += cityMap.roadSpacing
    }

    for (atm in engine.atms) {
        val sx = atm.x - camX
        val sy = atm.y - camY
        if (sx < -20f || sx > w + 20f || sy < -20f || sy > h + 20f) continue
        val color = if (atm.cooldown > 0f) Color(0xFF5A5470) else Color(0xFF2E7D32)
        drawRect(color = color, topLeft = Offset(sx - 10f, sy - 14f), size = Size(20f, 28f))
        drawCircle(color = Color.White, radius = 5f, center = Offset(sx, sy - 2f))
    }

    for (civ in engine.civilians) {
        val sx = civ.x - camX
        val sy = civ.y - camY
        if (sx < -20f || sx > w + 20f || sy < -20f || sy > h + 20f) continue
        drawCircle(color = if (civ.fleeing) Color(0xFFFFCA28) else Color(0xFF90A4AE), radius = 10f, center = Offset(sx, sy))
    }

    for (b in engine.bullets) {
        drawCircle(color = Color(0xFFFFF176), radius = 5f, center = Offset(b.x - camX, b.y - camY))
    }

    for (police in engine.policeCars) {
        val sx = police.x - camX
        val sy = police.y - camY
        if (sx < -60f || sx > w + 60f || sy < -60f || sy > h + 60f) continue
        drawTopDownCar(
            centerX = sx, centerY = sy, length = 46f, width = 24f,
            bodyColor = Color(0xFF1E1E24), accentColor = Color(0xFF424242),
            angleRadians = police.angle, isPolice = true
        )
    }

    if (!engine.isInVehicle) {
        val sx = engine.carParkedX - camX
        val sy = engine.carParkedY - camY
        drawTopDownCar(
            centerX = sx, centerY = sy, length = 46f, width = 24f,
            bodyColor = Color(playerCar.bodyColor), accentColor = Color(playerCar.accentColor),
            angleRadians = engine.carParkedAngle
        )
    }

    if (engine.isInVehicle) {
        drawTopDownCar(
            centerX = w / 2f, centerY = h / 2f, length = 46f, width = 24f,
            bodyColor = Color(playerCar.bodyColor), accentColor = Color(playerCar.accentColor),
            angleRadians = engine.playerAngle
        )
    } else {
        drawCharacterAvatar(
            centerX = w / 2f, centerY = h / 2f, scale = 0.7f,
            shirtColor = Color(playerCharacter.primaryColor), accentColor = Color(playerCharacter.accentColor)
        )
    }

    for (zone in cityMap.def.zoneLabels) {
        val wx = cityMap.worldWidth * zone.xFraction
        val wy = cityMap.worldHeight * zone.yFraction
        val sx = wx - camX
        val sy = wy - camY
        if (sx < -100f || sx > w + 100f || sy < -40f || sy > h + 40f) continue
        drawContext.canvas.nativeCanvas.drawText(zone.name, sx, sy, labelPaint)
    }
}

private fun DrawScope.drawLandmark(type: LandmarkType, cx: Float, cy: Float, s: Float) {
    when (type) {
        LandmarkType.HOSPITAL -> {
            drawRect(color = Color(0xFFF5F5F5), topLeft = Offset(cx - s / 2f, cy - s / 2f), size = Size(s, s * 0.7f))
            drawRect(color = Color(0xFFE53935), topLeft = Offset(cx - 6f, cy - s * 0.25f), size = Size(12f, 34f))
            drawRect(color = Color(0xFFE53935), topLeft = Offset(cx - 17f, cy - s * 0.15f), size = Size(34f, 12f))
        }
        LandmarkType.POLICE_STATION -> {
            drawRect(color = Color(0xFF1E3A5F), topLeft = Offset(cx - s / 2f, cy - s / 2f), size = Size(s, s * 0.7f))
            drawCircle(color = Color(0xFF64B5F6), radius = 16f, center = Offset(cx, cy - s * 0.15f))
        }
        LandmarkType.CLUB -> {
            drawRect(color = Color(0xFF4A148C), topLeft = Offset(cx - s / 2f, cy - s / 2f), size = Size(s, s * 0.7f))
            drawCircle(color = Color(0xFFFF2D95), radius = 14f, center = Offset(cx - 20f, cy - 10f))
            drawCircle(color = Color(0xFF00E5FF), radius = 14f, center = Offset(cx + 20f, cy - 10f))
        }
        LandmarkType.CRICKET_STADIUM, LandmarkType.HOCKEY_STADIUM -> {
            drawCircle(color = Color(0xFF2E7D32), radius = s / 2f, center = Offset(cx, cy))
            drawCircle(color = Color(0xFF1B5E20), radius = s / 2f, center = Offset(cx, cy), style = Stroke(width = 8f))
            drawRect(color = Color(0xFFD7CCC8), topLeft = Offset(cx - s * 0.18f, cy - s * 0.06f), size = Size(s * 0.36f, s * 0.12f))
        }
        LandmarkType.HOME -> {
            drawRect(color = NeonGold, topLeft = Offset(cx - s * 0.4f, cy - s * 0.2f), size = Size(s * 0.8f, s * 0.5f))
            drawRect(color = Color(0xFF8D6E63), topLeft = Offset(cx - s * 0.15f, cy + s * 0.05f), size = Size(s * 0.3f, s * 0.25f))
        }
        LandmarkType.LANDMARK -> {
            drawRect(color = NeonGold.copy(alpha = 0.85f), topLeft = Offset(cx - s / 2f, cy - s / 2f), size = Size(s, s * 0.7f))
            drawRect(color = Color(0xFF6A1B9A), topLeft = Offset(cx - s / 2f, cy - s / 2f - s * 0.25f), size = Size(s, s * 0.25f))
        }
    }
}

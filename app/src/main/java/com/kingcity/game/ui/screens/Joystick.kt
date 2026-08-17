package com.kingcity.game.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt

@Composable
fun Joystick(onDirectionChanged: (Float, Float) -> Unit) {
    val outerDp = 110.dp
    val density = LocalDensity.current
    val radiusPx = with(density) { (outerDp / 2).toPx() }
    var knobOffset by remember { mutableStateOf(Offset.Zero) }

    Canvas(
        modifier = Modifier
            .size(outerDp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val proposed = knobOffset + dragAmount
                        val dist = sqrt(proposed.x * proposed.x + proposed.y * proposed.y)
                        knobOffset = if (dist > radiusPx) proposed * (radiusPx / dist) else proposed
                        onDirectionChanged(knobOffset.x / radiusPx, knobOffset.y / radiusPx)
                    },
                    onDragEnd = {
                        knobOffset = Offset.Zero
                        onDirectionChanged(0f, 0f)
                    },
                    onDragCancel = {
                        knobOffset = Offset.Zero
                        onDirectionChanged(0f, 0f)
                    }
                )
            }
    ) {
        drawCircle(color = Color.White.copy(alpha = 0.18f), radius = radiusPx, center = center)
        drawCircle(color = Color.White.copy(alpha = 0.35f), radius = radiusPx, center = center, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f))
        drawCircle(color = Color.White.copy(alpha = 0.75f), radius = 26f, center = center + knobOffset)
    }
}

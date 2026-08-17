package com.kingcity.game.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kingcity.game.ui.theme.NeonGold
import com.kingcity.game.ui.theme.NeonPink
import com.kingcity.game.ui.theme.NightPurpleBottom
import com.kingcity.game.ui.theme.NightPurpleTop
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val transition = rememberInfiniteTransition(label = "shine")
    val glow by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "glow"
    )

    LaunchedEffect(Unit) {
        delay(2200)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(NightPurpleTop, NightPurpleBottom))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("👑", fontSize = (48 * glow).sp)
            Spacer(Modifier.height(4.dp))
            Text(
                "KING CITY",
                color = NeonGold,
                fontWeight = FontWeight.Black,
                fontSize = 46.sp,
                modifier = Modifier.graphicsLayer(alpha = glow)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "RULE • DRIVE • SURVIVE",
                color = NeonPink,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

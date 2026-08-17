package com.kingcity.game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kingcity.game.ui.theme.NeonGold
import com.kingcity.game.ui.theme.NightPurpleTop
import kotlinx.coroutines.delay

@Composable
fun DevCreditScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1600)
        onFinished()
    }
    Box(
        modifier = Modifier.fillMaxSize().background(NightPurpleTop),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("A GAME BY", color = androidx.compose.ui.graphics.Color(0xFFB8AEDC), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text("SIPU", color = NeonGold, fontSize = 36.sp, fontWeight = FontWeight.Black)
        }
    }
}

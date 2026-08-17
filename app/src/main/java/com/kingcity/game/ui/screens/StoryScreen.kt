package com.kingcity.game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kingcity.game.ui.theme.NeonGold
import com.kingcity.game.ui.theme.NightPurpleBottom
import com.kingcity.game.ui.theme.NightPurpleTop

@Composable
fun StoryScreen(onContinue: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(NightPurpleTop, NightPurpleBottom)))
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("THE STORY", color = NeonGold, fontWeight = FontWeight.Black, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(20.dp))
            Text(
                "Once, he was just a boy on the street corner, asking\n" +
                    "strangers for a few coins to get by.\n\n" +
                    "One night, out of desperation, he robbed a bank.\n" +
                    "That single choice changed everything.\n\n" +
                    "Now every street, every siren, every shadow\n" +
                    "is part of his new life in King City.",
                color = Color(0xFFE0D9FF),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(28.dp))
            Button(
                onClick = onContinue,
                colors = ButtonDefaults.buttonColors(containerColor = NeonGold)
            ) { Text("Continue", color = Color.Black, fontWeight = FontWeight.Bold) }
        }
    }
}

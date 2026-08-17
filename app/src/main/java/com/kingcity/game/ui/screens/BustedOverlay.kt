package com.kingcity.game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kingcity.game.ui.theme.CardDark
import com.kingcity.game.ui.theme.NeonGold
import com.kingcity.game.ui.theme.WantedRed

@Composable
fun BustedOverlay(moneyLost: Int, onContinue: () -> Unit, onExit: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(CardDark, RoundedCornerShape(24.dp))
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("BUSTED!", fontWeight = FontWeight.Black, color = WantedRed, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text("The police caught you.", color = Color(0xFFB8AEDC))
            Spacer(Modifier.height(8.dp))
            Text("💰 Lost \$$moneyLost", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = onContinue,
                colors = ButtonDefaults.buttonColors(containerColor = NeonGold),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Continue", color = Color.Black) }
            Spacer(Modifier.height(10.dp))
            TextButton(onClick = onExit, modifier = Modifier.fillMaxWidth()) { Text("Exit to Lobby") }
        }
    }
}

package com.kingcity.game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kingcity.game.audio.SoundManager
import com.kingcity.game.ui.theme.NeonGold
import com.kingcity.game.ui.theme.NightPurpleBottom
import com.kingcity.game.ui.theme.NightPurpleTop

@Composable
fun LoginScreen(onProfileCreated: (name: String, provider: String) -> Unit, soundManager: SoundManager) {
    var chosenProvider by remember { mutableStateOf<String?>(null) }
    var name by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(NightPurpleTop, NightPurpleBottom))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("KING CITY", color = NeonGold, fontWeight = FontWeight.Black, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(28.dp))

            if (chosenProvider == null) {
                ProviderButton("Continue with Google", Color(0xFFEA4335)) { chosenProvider = "Google"; soundManager.playClick() }
                Spacer(Modifier.height(12.dp))
                ProviderButton("Continue with Facebook", Color(0xFF1877F2)) { chosenProvider = "Facebook"; soundManager.playClick() }
                Spacer(Modifier.height(12.dp))
                ProviderButton("Continue with VK", Color(0xFF0077FF)) { chosenProvider = "VK"; soundManager.playClick() }
            } else {
                Text("Signed in with $chosenProvider", color = Color(0xFFB8AEDC))
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { if (it.length <= 16) name = it },
                    label = { Text("Choose your name") },
                    singleLine = true
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        val finalName = name.ifBlank { "Player" }
                        soundManager.playUnlock()
                        onProfileCreated(finalName, chosenProvider ?: "Guest")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGold),
                    modifier = Modifier.width(240.dp)
                ) { Text("Enter King City", color = Color.Black, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
private fun ProviderButton(label: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(280.dp)
    ) {
        Text(label, color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}

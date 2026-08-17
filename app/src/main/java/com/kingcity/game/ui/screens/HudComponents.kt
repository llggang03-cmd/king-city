package com.kingcity.game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kingcity.game.ui.theme.CardDark
import com.kingcity.game.ui.theme.NeonGold
import com.kingcity.game.ui.theme.WantedRed

@Composable
fun StatChip(icon: String, value: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(CardDark.copy(alpha = 0.85f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$icon $value", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NeonGold)
    }
}

@Composable
fun WantedStars(level: Int) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(CardDark.copy(alpha = 0.85f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        repeat(5) { i ->
            Text(
                text = if (i < level) "★" else "☆",
                color = if (i < level) WantedRed else Color(0xFF5A5470),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

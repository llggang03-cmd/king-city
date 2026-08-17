package com.kingcity.game.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val KingCityColorScheme = darkColorScheme(
    primary = NeonGold,
    onPrimary = CardDarker,
    secondary = NeonPink,
    background = NightPurpleTop,
    surface = CardDark,
    onBackground = TextLight,
    onSurface = TextLight
)

@Composable
fun KingCityTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KingCityColorScheme,
        typography = KingCityTypography,
        content = content
    )
}

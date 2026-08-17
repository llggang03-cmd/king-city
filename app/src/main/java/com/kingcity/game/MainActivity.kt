package com.kingcity.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kingcity.game.audio.SoundManager
import com.kingcity.game.data.SaveManager
import com.kingcity.game.ui.screens.AppRoot
import com.kingcity.game.ui.theme.KingCityTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val saveManager = SaveManager(applicationContext)
        val soundManager = SoundManager()

        setContent {
            KingCityApp(saveManager = saveManager, soundManager = soundManager)
        }
    }
}

@Composable
private fun KingCityApp(saveManager: SaveManager, soundManager: SoundManager) {
    KingCityTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AppRoot(saveManager = saveManager, soundManager = soundManager)
        }
    }
}

package com.kingcity.game.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kingcity.game.audio.SoundManager
import com.kingcity.game.data.SaveManager
import com.kingcity.game.data.SaveState

private enum class Screen { SPLASH, DEV_CREDIT, STORY, LOGIN, LOBBY, GAME, GARAGE }

@Composable
fun AppRoot(saveManager: SaveManager, soundManager: SoundManager) {
    var saveState by remember { mutableStateOf(saveManager.load()) }
    var screen by remember { mutableStateOf(Screen.SPLASH) }

    fun updateSave(newState: SaveState) {
        saveState = newState
        saveManager.save(newState)
    }

    when (screen) {
        Screen.SPLASH -> SplashScreen(onFinished = { screen = Screen.DEV_CREDIT })
        Screen.DEV_CREDIT -> DevCreditScreen(
            onFinished = { screen = if (saveState.hasProfile) Screen.LOBBY else Screen.STORY }
        )
        Screen.STORY -> StoryScreen(onContinue = { screen = Screen.LOGIN })
        Screen.LOGIN -> LoginScreen(
            onProfileCreated = { name, provider ->
                updateSave(saveState.copy(playerName = name, loginProvider = provider, hasProfile = true))
                screen = Screen.LOBBY
            },
            soundManager = soundManager
        )
        Screen.LOBBY -> LobbyScreen(
            saveState = saveState,
            onSaveState = ::updateSave,
            onStart = { screen = Screen.GAME },
            onGarage = { screen = Screen.GARAGE },
            soundManager = soundManager
        )
        Screen.GAME -> GameScreen(
            saveState = saveState,
            onSaveState = ::updateSave,
            onExitToLobby = { screen = Screen.LOBBY },
            soundManager = soundManager
        )
        Screen.GARAGE -> GarageScreen(
            saveState = saveState,
            onSaveState = ::updateSave,
            onBack = { screen = Screen.LOBBY },
            soundManager = soundManager
        )
    }
}

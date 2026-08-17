package com.kingcity.game.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kingcity.game.audio.SoundManager
import com.kingcity.game.data.CarCatalog
import com.kingcity.game.data.CharacterCatalog
import com.kingcity.game.data.MapCatalog
import com.kingcity.game.data.SaveState
import com.kingcity.game.game.CityMap
import com.kingcity.game.game.GameEngine
import com.kingcity.game.game.InteractionType
import com.kingcity.game.game.WeaponType
import com.kingcity.game.ui.game.drawWorld
import com.kingcity.game.ui.theme.CardDark
import com.kingcity.game.ui.theme.NeonGold
import com.kingcity.game.ui.theme.NeonPink
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    saveState: SaveState,
    onSaveState: (SaveState) -> Unit,
    onExitToLobby: () -> Unit,
    soundManager: SoundManager
) {
    val playerCar = remember(saveState.selectedCarId) { CarCatalog.byId(saveState.selectedCarId) }
    val playerCharacter = remember(saveState.selectedCharacterId) { CharacterCatalog.byId(saveState.selectedCharacterId) }
    val cityMap = remember(saveState.selectedMapId) { CityMap(MapCatalog.byId(saveState.selectedMapId)) }

    val engine = remember(cityMap) {
        GameEngine(
            cityMap = cityMap,
            onCoinCollected = { soundManager.playCoin() },
            onShoot = { soundManager.playShoot() },
            onWantedChanged = { old, new -> if (new > old) soundManager.playSiren() },
            onBusted = { soundManager.playBusted() }
        )
    }

    var frameTick by remember { mutableStateOf(0) }
    var isPaused by remember { mutableStateOf(false) }
    var sessionMoneyBanked by remember { mutableStateOf(0) }

    LaunchedEffect(cityMap) { engine.reset() }

    LaunchedEffect(isPaused) {
        var lastNanos = 0L
        while (true) {
            withFrameNanos { nanos ->
                if (lastNanos != 0L) {
                    val dt = ((nanos - lastNanos) / 1_000_000_000f).coerceIn(0f, 0.05f)
                    if (!isPaused && !engine.isBusted) {
                        engine.update(dt)
                    }
                }
                lastNanos = nanos
                frameTick++
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            val delta = engine.money - sessionMoneyBanked
            if (delta != 0) {
                sessionMoneyBanked = engine.money
                onSaveState(saveState.copy(money = saveState.money + delta))
            }
        }
    }

    @Suppress("UNUSED_EXPRESSION")
    frameTick

    Box(modifier = Modifier.fillMaxSize().background(CardDark)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawWorld(engine, cityMap, playerCar, playerCharacter)
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 14.dp, start = 14.dp, end = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatChip("💰", "${saveState.money + (engine.money - sessionMoneyBanked)}")
            WantedStars(engine.wantedLevel)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f))
                    .clickable { if (!engine.isBusted) isPaused = !isPaused },
                contentAlignment = Alignment.Center
            ) { Text(if (isPaused) "▶" else "⏸", color = Color.White, fontSize = 16.sp) }
        }

        Box(modifier = Modifier.fillMaxSize().padding(20.dp), contentAlignment = Alignment.BottomStart) {
            Joystick(onDirectionChanged = { x, y -> engine.setJoystick(x, y) })
        }

        Box(modifier = Modifier.fillMaxSize().padding(20.dp), contentAlignment = Alignment.BottomEnd) {
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (engine.interaction != InteractionType.NONE) {
                    val label = when (engine.interaction) {
                        InteractionType.ENTER_VEHICLE -> "🚗 Enter"
                        InteractionType.EXIT_VEHICLE -> "🚪 Exit"
                        InteractionType.ROB_ATM -> "🏧 Rob"
                        InteractionType.ROB_CIVILIAN -> "👤 Hold Up"
                        InteractionType.NONE -> ""
                    }
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(NeonGold)
                            .clickable { engine.doInteraction() }
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) { Text(label, color = Color.Black, fontSize = 13.sp) }
                }

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(CardDark)
                        .clickable { if (!isPaused && !engine.isBusted) engine.switchWeapon() },
                    contentAlignment = Alignment.Center
                ) { Text(if (engine.weapon == WeaponType.PISTOL) "🔫" else "👊", fontSize = 22.sp) }

                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(NeonPink.copy(alpha = 0.85f))
                        .clickable { if (!isPaused && !engine.isBusted) engine.

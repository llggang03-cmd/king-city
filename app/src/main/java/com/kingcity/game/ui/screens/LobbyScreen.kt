package com.kingcity.game.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kingcity.game.audio.SoundManager
import com.kingcity.game.data.CharacterCatalog
import com.kingcity.game.data.MapCatalog
import com.kingcity.game.data.MapDef
import com.kingcity.game.data.SaveState
import com.kingcity.game.ui.game.drawCharacterAvatar
import com.kingcity.game.ui.theme.CardDark
import com.kingcity.game.ui.theme.NeonGold
import com.kingcity.game.ui.theme.NeonPink
import com.kingcity.game.ui.theme.NightPurpleBottom
import com.kingcity.game.ui.theme.NightPurpleTop

@Composable
fun LobbyScreen(
    saveState: SaveState,
    onSaveState: (SaveState) -> Unit,
    onStart: () -> Unit,
    onGarage: () -> Unit,
    soundManager: SoundManager
) {
    val selectedChar = CharacterCatalog.byId(saveState.selectedCharacterId)

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.horizontalGradient(listOf(NightPurpleTop, NightPurpleBottom)))
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.width(190.dp).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("👑 KING CITY", color = NeonGold, fontWeight = FontWeight.Black, fontSize = MaterialTheme.typography.titleLarge.fontSize)
            Spacer(Modifier.height(2.dp))
            Text(saveState.playerName, color = Color.White, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Canvas(modifier = Modifier.size(100.dp)) {
                drawCharacterAvatar(
                    centerX = size.width / 2f, centerY = size.height / 2f, scale = 1.2f,
                    shirtColor = Color(selectedChar.primaryColor), accentColor = Color(selectedChar.accentColor)
                )
            }
            Spacer(Modifier.height(6.dp))
            StatChip("💰", "${saveState.money}")
            Spacer(Modifier.height(6.dp))
            StatChip("💎", "${saveState.diamonds}")
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { soundManager.playClick(); onStart() },
                colors = ButtonDefaults.buttonColors(containerColor = NeonPink),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) { Text("START", fontWeight = FontWeight.Black, color = Color.White) }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { soundManager.playClick(); onGarage() },
                colors = ButtonDefaults.buttonColors(containerColor = CardDark),
                modifier = Modifier.fillMaxWidth()
            ) { Text("🚗 Garage", color = NeonGold) }
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.fillMaxSize()) {
            Text("Choose Your City", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(MapCatalog.maps) { map ->
                    MapTile(
                        map = map,
                        isUnlocked = saveState.unlockedMapIds.contains(map.id),
                        isSelected = saveState.selectedMapId == map.id,
                        canAfford = saveState.money >= map.unlockCost,
                        onClick = {
                            when {
                                saveState.unlockedMapIds.contains(map.id) -> {
                                    soundManager.playClick()
                                    onSaveState(saveState.copy(selectedMapId = map.id))
                                }
                                saveState.money >= map.unlockCost -> {
                                    soundManager.playUnlock()
                                    onSaveState(
                                        saveState.copy(
                                            money = saveState.money - map.unlockCost,
                                            unlockedMapIds = saveState.unlockedMapIds + map.id,
                                            selectedMapId = map.id
                                        )
                                    )
                                }
                                else -> soundManager.playClick()
                            }
                        }
                    )
                }
            }

            Spacer(Modifier.height(14.dp))
            Text("Characters", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(CharacterCatalog.characters) { character ->
                    CharacterTile(
                        name = character.name,
                        primaryColor = character.primaryColor,
                        accentColor = character.accentColor,
                        isUnlocked = saveState.unlockedCharacterIds.contains(character.id),
                        isSelected = saveState.selectedCharacterId == character.id,
                        canAfford = saveState.money >= character.unlockCost,
                        cost = character.unlockCost,
                        onClick = {
                            when {
                                saveState.unlockedCharacterIds.contains(character.id) -> {
                                    soundManager.playClick()
                                    onSaveState(saveState.copy(selectedCharacterId = character.id))
                                }
                                saveState.money >= character.unlockCost -> {
                                    soundManager.playUnlock()
                                    onSaveState(
                                        saveState.copy(
                                            money = saveState.money - character.unlockCost,
                                            unlockedCharacterIds = saveState.unlockedCharacterIds + character.id,
                                            selectedCharacterId = character.id
                                        )
                                    )
                                }
                                else -> soundManager.playClick()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MapTile(map: MapDef, isUnlocked: Boolean, isSelected: Boolean, canAfford: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .height(90.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color(0xFF3A2E6B) else CardDark)
            .border(width = if (isSelected) 2.dp else 0.dp, color = NeonGold, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(map.name, color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        when {
            isSelected -> Text("Selected", color = NeonGold)
            isUnlocked -> Text("Tap to select", color = Color(0xFFB8AEDC))
            canAfford -> Text("Unlock 💰${map.unlockCost}", color = NeonGold)
            else -> Text("Need 💰${map.unlockCost}", color = Color(0xFF8A80A8))
        }
    }
}

@Composable
private fun CharacterTile(
    name: String,
    primaryColor: Long,
    accentColor: Long,
    isUnlocked: Boolean,
    isSelected: Boolean,
    canAfford: Boolean,
    cost: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color(0xFF3A2E6B) else CardDark)
            .border(width = if (isSelected) 2.dp else 0.dp, color = NeonGold, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(modifier = Modifier.size(70.dp)) {
            drawCharacterAvatar(
                centerX = size.width / 2f, centerY = size.height / 2f, scale = 0.9f,
                shirtColor = Color(primaryColor), accentColor = Color(accentColor)
            )
        }
        Text(name, color = Color.White, fontWeight = FontWeight.SemiBold)
        when {
            isSelected -> Text("Selected", color = NeonGold, fontSize = MaterialTheme.typography.labelLarge.fontSize)
            isUnlocked -> Text("Tap", color = Color(0xFFB8AEDC), fontSize = MaterialTheme.typography.labelLarge.fontSize)
            canAfford -> Text("💰$cost", color = NeonGold, fontSize = MaterialTheme.typography.labelLarge.fontSize)
            else -> Text("Need 💰$cost", color = Color(0xFF8A80A8), fontSize = MaterialTheme.typography.labelLarge.fontSize)
        }
    }
}

package com.kingcity.game.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kingcity.game.audio.SoundManager
import com.kingcity.game.data.CarCatalog
import com.kingcity.game.data.CarDef
import com.kingcity.game.data.SaveState
import com.kingcity.game.ui.game.drawTopDownCar
import com.kingcity.game.ui.theme.CardDark
import com.kingcity.game.ui.theme.NeonGold
import com.kingcity.game.ui.theme.NightPurpleTop

@Composable
fun GarageScreen(
    saveState: SaveState,
    onSaveState: (SaveState) -> Unit,
    onBack: () -> Unit,
    soundManager: SoundManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NightPurpleTop)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(Modifier.width(8.dp))
            Text("Garage", color = Color.White, style = MaterialTheme.typography.headlineMedium)
        }
        Spacer(Modifier.height(4.dp))
        Text("💰 ${saveState.money}", color = NeonGold, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(CarCatalog.cars) { car ->
                CarTile(
                    car = car,
                    isUnlocked = saveState.unlockedCarIds.contains(car.id),
                    isSelected = saveState.selectedCarId == car.id,
                    canAfford = saveState.money >= car.unlockCost,
                    onClick = {
                        when {
                            saveState.unlockedCarIds.contains(car.id) -> {
                                soundManager.playClick()
                                onSaveState(saveState.copy(selectedCarId = car.id))
                            }
                            saveState.money >= car.unlockCost -> {
                                soundManager.playUnlock()
                                onSaveState(
                                    saveState.copy(
                                        money = saveState.money - car.unlockCost,
                                        unlockedCarIds = saveState.unlockedCarIds + car.id,
                                        selectedCarId = car.id
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

@Composable
private fun CarTile(
    car: CarDef,
    isUnlocked: Boolean,
    isSelected: Boolean,
    canAfford: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(if (isSelected) Color(0xFF3A2E6B) else CardDark)
            .border(width = if (isSelected) 2.dp else 0.dp, color = NeonGold, shape = RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(70.dp)) {
            drawTopDownCar(
                centerX = size.width / 2f, centerY = size.height / 2f,
                length = size.width * 0.6f, width = size.height * 0.5f,
                bodyColor = Color(car.bodyColor), accentColor = Color(car.accentColor),
                angleRadians = 0f
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(car.name, color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(2.dp))
        when {
            isSelected -> Text("Selected ✓", color = NeonGold, fontWeight = FontWeight.SemiBold)
            isUnlocked -> Text("Tap to select", color = Color(0xFFB8AEDC))
            canAfford -> Text("Unlock 💰${car.unlockCost}", color = NeonGold)
            else -> Text("Need 💰${car.unlockCost}", color = Color(0xFF8A80A8))
        }
    }
}

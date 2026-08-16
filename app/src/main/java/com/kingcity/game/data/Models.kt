package com.kingcity.game.data

data class CharacterDef(
    val id: Int,
    val name: String,
    val primaryColor: Long,
    val accentColor: Long,
    val unlockCost: Int
)

data class CarDef(
    val id: Int,
    val name: String,
    val bodyColor: Long,
    val accentColor: Long,
    val unlockCost: Int
)

data class ZoneLabel(val name: String, val xFraction: Float, val yFraction: Float)

data class MapDef(
    val id: Int,
    val name: String,
    val worldWidth: Float,
    val worldHeight: Float,
    val unlockCost: Int,
    val seed: Long,
    val zoneLabels: List<ZoneLabel>
)

data class SaveState(
    val playerName: String = "Player",
    val loginProvider: String = "",
    val hasProfile: Boolean = false,
    val money: Int = 500,
    val diamonds: Int = 20,
    val bestWantedSurvived: Int = 0,
    val unlockedCharacterIds: Set<Int> = setOf(0),
    val selectedCharacterId: Int = 0,
    val unlockedCarIds: Set<Int> = setOf(0),
    val selectedCarId: Int = 0,
    val unlockedMapIds: Set<Int> = setOf(0),
    val selectedMapId: Int = 0
)

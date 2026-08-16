package com.kingcity.game.game

import com.kingcity.game.data.MapDef
import kotlin.random.Random

enum class LandmarkType { HOSPITAL, POLICE_STATION, CLUB, CRICKET_STADIUM, HOCKEY_STADIUM, HOME, LANDMARK }

data class Building(val x: Float, val y: Float, val w: Float, val h: Float, val colorIndex: Int)
data class Landmark(val type: LandmarkType, val x: Float, val y: Float, val size: Float)
data class Bridge(val x: Float, val yTop: Float, val yBottom: Float, val width: Float)

class CityMap(val def: MapDef) {
    val worldWidth = def.worldWidth
    val worldHeight = def.worldHeight
    val waterStartX = worldWidth * 0.80f

    val roadSpacing = 380f
    val roadWidth = 64f

    val buildings: List<Building> by lazy { generateBuildings() }
    val palmTrees: List<Pair<Float, Float>> by lazy { generatePalmTrees() }
    val landmarks: List<Landmark> by lazy { generateLandmarks() }
    val bridges: List<Bridge> by lazy { generateBridges() }
    val atmPoints: List<Pair<Float, Float>> by lazy { generateAtms() }
    val civilianSpawnPoints: List<Pair<Float, Float>> by lazy { generateCivilianSpawns() }
    val homePoint: Pair<Float, Float> by lazy { worldWidth * 0.5f to worldHeight * 0.5f }

    fun isOnRoad(x: Float, y: Float): Boolean {
        val modX = x % roadSpacing
        val modY = y % roadSpacing
        return modX < roadWidth || modY < roadWidth
    }

    fun isWater(x: Float, y: Float): Boolean {
        if (x <= waterStartX) return false
        val onBridge = bridges.any { b -> x <= waterStartX + b.width && y >= b.yTop && y <= b.yBottom }
        return !onBridge
    }

    fun clampToDrivable(x: Float, y: Float): Pair<Float, Float> {
        var cx = x.coerceIn(40f, worldWidth - 40f)
        val cy = y.coerceIn(40f, worldHeight - 40f)
        if (isWater(cx, cy)) cx = waterStartX - 40f
        return cx to cy
    }

    fun randomRoadPoint(random: Random): Pair<Float, Float> {
        var tries = 0
        while (tries < 200) {
            tries++
            val x = random.nextFloat() * (waterStartX - 100f) + 50f
            val y = random.nextFloat() * (worldHeight - 100f) + 50f
            if (isOnRoad(x, y)) return x to y
        }
        return worldWidth * 0.5f to worldHeight * 0.5f
    }

    private fun generateBuildings(): List<Building> {
        val list = mutableListOf<Building>()
        var colorCycle = 0
        var by = roadWidth
        while (by < worldHeight - roadSpacing) {
            var bx = roadWidth
            while (bx < waterStartX - roadSpacing) {
                val padding = 24f
                val w = roadSpacing - roadWidth - padding * 2
                val h = roadSpacing - roadWidth - padding * 2
                if (w > 40f && h > 40f) {
                    list.add(Building(bx + padding, by + padding, w, h, colorCycle % 4))
                    colorCycle++
                }
                bx += roadSpacing
            }
            by += roadSpacing
        }
        return list
    }

    private fun generatePalmTrees(): List<Pair<Float, Float>> {
        val list = mutableListOf<Pair<Float, Float>>()
        val rnd = Random(def.seed)
        var y = 60f
        while (y < worldHeight - 60f) {
            val x = waterStartX - 30f - rnd.nextFloat() * 40f
            list.add(x to y)
            y += 90f + rnd.nextFloat() * 40f
        }
        return list
    }

    private fun generateLandmarks(): List<Landmark> {
        val rnd = Random(def.seed + 1)
        val list = mutableListOf<Landmark>()
        list.add(Landmark(LandmarkType.HOME, worldWidth * 0.5f, worldHeight * 0.5f, 160f))
        list.add(Landmark(LandmarkType.HOSPITAL, worldWidth * 0.20f, worldHeight * 0.22f, 150f))
        list.add(Landmark(LandmarkType.POLICE_STATION, worldWidth * 0.65f, worldHeight * 0.18f, 150f))
        list.add(Landmark(LandmarkType.CLUB, worldWidth * 0.30f, worldHeight * 0.72f, 140f))
        list.add(Landmark(LandmarkType.CRICKET_STADIUM, worldWidth * 0.62f, worldHeight * 0.60f, 220f))
        list.add(Landmark(LandmarkType.HOCKEY_STADIUM, worldWidth * 0.45f, worldHeight * 0.15f, 200f))
        list.add(Landmark(LandmarkType.LANDMARK, worldWidth * 0.40f, worldHeight * 0.42f, 220f))
        return list
    }

    private fun generateBridges(): List<Bridge> {
        val list = mutableListOf<Bridge>()
        list.add(Bridge(x = waterStartX, yTop = worldHeight * 0.30f, yBottom = worldHeight * 0.30f + roadWidth, width = 260f))
        list.add(Bridge(x = waterStartX, yTop = worldHeight * 0.68f, yBottom = worldHeight * 0.68f + roadWidth, width = 260f))
        return list
    }

    private fun generateAtms(): List<Pair<Float, Float>> {
        val rnd = Random(def.seed + 2)
        val list = mutableListOf<Pair<Float, Float>>()
        repeat(10) { list.add(randomRoadPoint(rnd)) }
        return list
    }

    private fun generateCivilianSpawns(): List<Pair<Float, Float>> {
        val rnd = Random(def.seed + 3)
        val list = mutableListOf<Pair<Float, Float>>()
        repeat(14) { list.add(randomRoadPoint(rnd)) }
        return list
    }
}

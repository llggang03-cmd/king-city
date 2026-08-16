package com.kingcity.game.game

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.random.Random

data class PoliceCar(var x: Float, var y: Float, var angle: Float = 0f, var alive: Boolean = true)
data class Bullet(var x: Float, var y: Float, val vx: Float, val vy: Float, var traveled: Float = 0f)
data class Atm(val x: Float, val y: Float, var cooldown: Float = 0f)
data class Civilian(var x: Float, var y: Float, var angle: Float, var wanderTimer: Float = 0f, var fleeing: Boolean = false)

enum class WeaponType { FISTS, PISTOL }
enum class InteractionType { NONE, ENTER_VEHICLE, EXIT_VEHICLE, ROB_ATM, ROB_CIVILIAN }

class GameEngine(
    private val cityMap: CityMap,
    private val onCoinCollected: () -> Unit = {},
    private val onShoot: () -> Unit = {},
    private val onWantedChanged: (Int, Int) -> Unit = { _, _ -> },
    private val onBusted: () -> Unit = {}
) {
    var playerX = 0f
        private set
    var playerY = 0f
        private set
    var playerAngle = 0f
        private set
    var playerSpeed = 0f
        private set
    var isInVehicle = true
        private set
    var weapon = WeaponType.PISTOL
        private set

    var carParkedX = 0f
        private set
    var carParkedY = 0f
        private set
    var carParkedAngle = 0f
        private set

    var money = 0
        private set
    var wantedLevel = 0
        private set
    var isBusted = false
        private set
    var elapsedSeconds = 0f
        private set
    var interaction = InteractionType.NONE
        private set

    val policeCars = mutableListOf<PoliceCar>()
    val bullets = mutableListOf<Bullet>()
    val atms = mutableListOf<Atm>()
    val civilians = mutableListOf<Civilian>()

    private var joystickX = 0f
    private var joystickY = 0f
    private var fireCooldown = 0f
    private var heatDecayTimer = 0f
    private val random = Random(System.currentTimeMillis())

    private val vehicleMaxSpeed = 480f
    private val footMaxSpeed = 160f
    private val turnRateRadPerSec = 3.4f
    private val policeSpeedFactor = 0.82f
    private val catchRadius = 46f
    private val bulletHitRadius = 40f
    private val meleeRange = 60f
    private val bulletSpeed = 900f
    private val bulletMaxRange = 620f
    private val interactRange = 70f
    private val vehicleReenterRange = 80f
    private val atmCooldownSeconds = 45f

    fun reset() {
        val (hx, hy) = cityMap.homePoint
        playerX = hx
        playerY = hy
        playerAngle = 0f
        playerSpeed = 0f
        isInVehicle = true
        weapon = WeaponType.PISTOL
        carParkedX = hx
        carParkedY = hy
        carParkedAngle = 0f
        money = 0
        wantedLevel = 0
        isBusted = false
        elapsedSeconds = 0f
        interaction = InteractionType.NONE
        policeCars.clear()
        bullets.clear()
        heatDecayTimer = 0f
        fireCooldown = 0f

        atms.clear()
        cityMap.atmPoints.forEach { (x, y) -> atms.add(Atm(x, y)) }

        civilians.clear()
        cityMap.civilianSpawnPoints.forEach { (x, y) -> civilians.add(Civilian(x, y, random.nextFloat() * 6.28f)) }
    }

    fun setJoystick(x: Float, y: Float) {
        joystickX = x.coerceIn(-1f, 1f)
        joystickY = y.coerceIn(-1f, 1f)
    }

    fun switchWeapon() {
        weapon = if (weapon == WeaponType.PISTOL) WeaponType.FISTS else WeaponType.PISTOL
    }

    fun fire() {
        if (isBusted || fireCooldown > 0f) return
        fireCooldown = if (weapon == WeaponType.PISTOL) 0.28f else 0.4f

        if (weapon == WeaponType.PISTOL) {
            val vx = cos(playerAngle) * bulletSpeed
            val vy = sin(playerAngle) * bulletSpeed
            bullets.add(Bullet(playerX, playerY, vx, vy))
        } else {
            for (police in policeCars) {
                if (!police.alive) continue
                if (hypot(playerX - police.x, playerY - police.y) < meleeRange) {
                    police.alive = false
                    money += 15
                    break
                }
            }
        }
        onShoot()
        raiseWanted()
        heatDecayTimer = 0f
    }

    fun doInteraction() {
        when (interaction) {
            InteractionType.ENTER_VEHICLE -> {
                isInVehicle = true
                playerAngle = carParkedAngle
            }
            InteractionType.EXIT_VEHICLE -> {
                carParkedX = playerX
                carParkedY = playerY
                carParkedAngle = playerAngle
                isInVehicle = false
            }
            InteractionType.ROB_ATM -> {
                val atm = atms.minByOrNull { hypot(playerX - it.x, playerY - it.y) }
                if (atm != null && atm.cooldown <= 0f) {
                    money += 40 + random.nextInt(50)
                    atm.cooldown = atmCooldownSeconds
                    onCoinCollected()
                    raiseWanted()
                }
            }
            InteractionType.ROB_CIVILIAN -> {
                val civ = civilians.filter { !it.fleeing }
                    .minByOrNull { hypot(playerX - it.x, playerY - it.y) }
                if (civ != null) {
                    money += 15 + random.nextInt(25)
                    civ.fleeing = true
                    onCoinCollected()
                    raiseWanted()
                }
            }
            InteractionType.NONE -> {}
        }
    }

    private fun raiseWanted() {
        val old = wantedLevel
        wantedLevel = (wantedLevel + 1).coerceAtMost(5)
        if (wantedLevel != old) onWantedChanged(old, wantedLevel)
        ensurePoliceCount()
    }

    private fun lowerWanted() {
        if (wantedLevel <= 0) return
        val old = wantedLevel
        wantedLevel -= 1
        onWantedChanged(old, wantedLevel)
        ensurePoliceCount()
    }

    private fun ensurePoliceCount() {
        while (policeCars.count { it.alive } < wantedLevel) {
            val angle = random.nextFloat() * (2f * Math.PI).toFloat()
            val dist = 700f + random.nextFloat() * 300f
            val sx = playerX + cos(angle) * dist
            val sy = playerY + sin(angle) * dist
            val (cx, cy) = cityMap.clampToDrivable(sx, sy)
            policeCars.add(PoliceCar(cx, cy))
        }
        while (policeCars.count { it.alive } > wantedLevel) {
            val idx = policeCars.indexOfFirst { it.alive }
            if (idx >= 0) policeCars.removeAt(idx) else break
        }
    }

    fun update(dt: Float) {
        if (isBusted) return
        elapsedSeconds += dt
        if (fireCooldown > 0f) fireCooldown -= dt

        updatePlayer(dt)
        updateBullets(dt)
        updatePolice(dt)
        updateAtms(dt)
        updateCivilians(dt)
        updateWantedDecay(dt)
        updateInteraction()
        checkBusted()
    }

    private fun updatePlayer(dt: Float) {
        val maxSpeed = if (isInVehicle) vehicleMaxSpeed else footMaxSpeed
        val mag = hypot(joystickX, joystickY)
        if (mag > 0.12f) {
            val targetAngle = atan2(joystickY, joystickX)
            var diff = targetAngle - playerAngle
            while (diff > Math.PI) diff -= (2 * Math.PI).toFloat()
            while (diff < -Math.PI) diff += (2 * Math.PI).toFloat()
            val maxTurn = turnRateRadPerSec * dt
            playerAngle += diff.coerceIn(-maxTurn, maxTurn)
            playerSpeed += (maxSpeed * mag.coerceAtMost(1f) - playerSpeed) * (dt * 3f).coerceIn(0f, 1f)
        } else {
            playerSpeed += (0f - playerSpeed) * (dt * 3f).coerceIn(0f, 1f)
        }

        val nx = playerX + cos(playerAngle) * playerSpeed * dt
        val ny = playerY + sin(playerAngle) * playerSpeed * dt
        val (cx, cy) = cityMap.clampToDrivable(nx, ny)
        playerX = cx
        playerY = cy
    }

    private fun updateBullets(dt: Float) {
        val iterator = bullets.iterator()
        while (iterator.hasNext()) {
            val b = iterator.next()
            b.x += b.vx * dt
            b.y += b.vy * dt
            b.traveled += hypot(b.vx * dt, b.vy * dt)

            var hit = false
            for (police in policeCars) {
                if (!police.alive) continue
                if (hypot(b.x - police.x, b.y - police.y) < bulletHitRadius) {
                    police.alive = false
                    money += 15
                    hit = true
                    break
                }
            }
            if (hit || b.traveled > bulletMaxRange) {
                iterator.remove()
            }
        }
        policeCars.removeAll { !it.alive }
    }

    private fun updatePolice(dt: Float) {
        val speed = vehicleMaxSpeed * policeSpeedFactor
        for (police in policeCars) {
            val dx = playerX - police.x
            val dy = playerY - police.y
            val dist = hypot(dx, dy)
            if (dist > 1f) {
                police.angle = atan2(dy, dx)
                val nx = police.x + cos(police.angle) * speed * dt
                val ny = police.y + sin(police.angle) * speed * dt
                val (cx, cy) = cityMap.clampToDrivable(nx, ny)
                police.x = cx
                police.y = cy
            }
        }
    }

    private fun updateAtms(dt: Float) {
        for (atm in atms) {
            if (atm.cooldown > 0f) atm.cooldown -= dt
        }
    }

    private fun updateCivilians(dt: Float) {
        for (civ in civilians) {
            civ.wanderTimer -= dt
            if (civ.wanderTimer <= 0f) {
                civ.wanderTimer = 1.5f + random.nextFloat() * 2f
                civ.angle = random.nextFloat() * 6.28f
            }
            val speed = if (civ.fleeing) 140f else 40f
            val nx = civ.x + cos(civ.angle) * speed * dt
            val ny = civ.y + sin(civ.angle) * speed * dt
            val (cx, cy) = cityMap.clampToDrivable(nx, ny)
            civ.x = cx
            civ.y = cy
            if (civ.fleeing && hypot(playerX - civ.x, playerY - civ.y) > 500f) {
                civ.fleeing = false
            }
        }
    }

    private fun updateWantedDecay(dt: Float) {
        val nearestPoliceDist = policeCars.minOfOrNull { hypot(playerX - it.x, playerY - it.y) } ?: Float.MAX_VALUE
        if (fireCooldown <= 0f && nearestPoliceDist > 380f) {
            heatDecayTimer += dt
            if (heatDecayTimer > 2.2f) {
                heatDecayTimer = 0f
                lowerWanted()
            }
        } else {
            heatDecayTimer = 0f
        }
    }

    private fun updateInteraction() {
        interaction = when {
            isInVehicle -> InteractionType.EXIT_VEHICLE
            !isInVehicle && hypot(playerX - carParkedX, playerY - carParkedY) < vehicleReenterRange -> InteractionType.ENTER_VEHICLE
            atms.any { it.cooldown <= 0f && hypot(playerX - it.x, playerY - it.y) < interactRange } -> InteractionType.ROB_ATM
            civilians.any { !it.fleeing && hypot(playerX - it.x, playerY - it.y) < interactRange } -> InteractionType.ROB_CIVILIAN
            else -> InteractionType.NONE
        }
    }

    private fun checkBusted() {
        for (police in policeCars) {
            if (hypot(playerX - police.x, playerY - police.y) < catchRadius) {
                isBusted = true
                onBusted()
                return
            }
        }
    }

    fun respawnAfterBusted(): Int {
        val lostMoney = (money * 0.25f).toInt()
        money = (money - lostMoney).coerceAtLeast(0)
        val (hx, hy) = cityMap.homePoint
        playerX = hx
        playerY = hy
        playerAngle = 0f
        playerSpeed = 0f
        isInVehicle = true
        carParkedX = hx
        carParkedY = hy
        wantedLevel = 0
        policeCars.clear()
        bullets.clear()
        isBusted = false
        heatDecayTimer = 0f
        return lostMoney
    }
}

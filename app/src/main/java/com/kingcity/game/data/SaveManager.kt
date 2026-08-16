package com.kingcity.game.data

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray

class SaveManager(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun load(): SaveState {
        return SaveState(
            playerName = prefs.getString(KEY_NAME, "Player") ?: "Player",
            loginProvider = prefs.getString(KEY_PROVIDER, "") ?: "",
            hasProfile = prefs.getBoolean(KEY_HAS_PROFILE, false),
            money = prefs.getInt(KEY_MONEY, 500),
            diamonds = prefs.getInt(KEY_DIAMONDS, 20),
            bestWantedSurvived = prefs.getInt(KEY_BEST_WANTED, 0),
            unlockedCharacterIds = stringToIntSet(prefs.getString(KEY_UNLOCKED_CHARS, "[0]")),
            selectedCharacterId = prefs.getInt(KEY_SELECTED_CHAR, 0),
            unlockedCarIds = stringToIntSet(prefs.getString(KEY_UNLOCKED_CARS, "[0]")),
            selectedCarId = prefs.getInt(KEY_SELECTED_CAR, 0),
            unlockedMapIds = stringToIntSet(prefs.getString(KEY_UNLOCKED_MAPS, "[0]")),
            selectedMapId = prefs.getInt(KEY_SELECTED_MAP, 0)
        )
    }

    fun save(state: SaveState) {
        prefs.edit()
            .putString(KEY_NAME, state.playerName)
            .putString(KEY_PROVIDER, state.loginProvider)
            .putBoolean(KEY_HAS_PROFILE, state.hasProfile)
            .putInt(KEY_MONEY, state.money)
            .putInt(KEY_DIAMONDS, state.diamonds)
            .putInt(KEY_BEST_WANTED, state.bestWantedSurvived)
            .putString(KEY_UNLOCKED_CHARS, intSetToString(state.unlockedCharacterIds))
            .putInt(KEY_SELECTED_CHAR, state.selectedCharacterId)
            .putString(KEY_UNLOCKED_CARS, intSetToString(state.unlockedCarIds))
            .putInt(KEY_SELECTED_CAR, state.selectedCarId)
            .putString(KEY_UNLOCKED_MAPS, intSetToString(state.unlockedMapIds))
            .putInt(KEY_SELECTED_MAP, state.selectedMapId)
            .apply()
    }

    private fun intSetToString(set: Set<Int>): String = JSONArray(set.toList()).toString()

    private fun stringToIntSet(raw: String?): Set<Int> {
        if (raw.isNullOrBlank()) return emptySet()
        return try {
            val arr = JSONArray(raw)
            (0 until arr.length()).map { arr.getInt(it) }.toSet()
        } catch (e: Exception) {
            emptySet()
        }
    }

    companion object {
        private const val PREFS_NAME = "king_city_prefs"
        private const val KEY_NAME = "player_name"
        private const val KEY_PROVIDER = "login_provider"
        private const val KEY_HAS_PROFILE = "has_profile"
        private const val KEY_MONEY = "money"
        private const val KEY_DIAMONDS = "diamonds"
        private const val KEY_BEST_WANTED = "best_wanted"
        private const val KEY_UNLOCKED_CHARS = "unlocked_chars"
        private const val KEY_SELECTED_CHAR = "selected_char"
        private const val KEY_UNLOCKED_CARS = "unlocked_cars"
        private const val KEY_SELECTED_CAR = "selected_car"
        private const val KEY_UNLOCKED_MAPS = "unlocked_maps"
        private const val KEY_SELECTED_MAP = "selected_map"
    }
}

package com.kingcity.game.data

object CharacterCatalog {
    val characters = listOf(
        CharacterDef(0, "Lucifer", 0xFF2E7D32, 0xFFA5D6A7, 0),
        CharacterDef(1, "Chiku", 0xFF4A148C, 0xFFCE93D8, 200),
        CharacterDef(2, "Sambit", 0xFFB71C1C, 0xFFEF9A9A, 350),
        CharacterDef(3, "Samesh", 0xFF37474F, 0xFFB0BEC5, 500),
        CharacterDef(4, "Rico", 0xFF0D47A1, 0xFF90CAF9, 650),
        CharacterDef(5, "Bosco", 0xFF1B5E20, 0xFF81C784, 800),
        CharacterDef(6, "Nawab", 0xFF263238, 0xFF78909C, 1000)
    )

    fun byId(id: Int): CharacterDef = characters.firstOrNull { it.id == id } ?: characters[0]
}

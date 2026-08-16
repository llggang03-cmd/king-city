package com.kingcity.game.data

object CarCatalog {
    val cars = listOf(
        CarDef(0, "Street Hatch", 0xFFE53935, 0xFFFFCDD2, 0),
        CarDef(1, "Neon Coupe", 0xFF1E88E5, 0xFFBBDEFB, 300),
        CarDef(2, "Gold Cruiser", 0xFFFDD835, 0xFFFFF9C4, 600),
        CarDef(3, "Royal Sedan", 0xFF8E24AA, 0xFFE1BEE7, 900),
        CarDef(4, "King's Bike", 0xFF212121, 0xFFFF7A33, 1200)
    )

    fun byId(id: Int): CarDef = cars.firstOrNull { it.id == id } ?: cars[0]
}

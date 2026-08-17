package com.kingcity.game.data

object CarCatalog {
    val cars = listOf(
        CarDef(0, "Street Hatch", 0xFFE53935, 0xFFFFCDD2, 0),
        CarDef(1, "Wasp Scooter", 0xFF8D6E63, 0xFFD7CCC8, 100),
        CarDef(2, "Falcon 250", 0xFF3949AB, 0xFFC5CAE9, 250),
        CarDef(3, "Regal Classic", 0xFF5D4037, 0xFFBCAAA4, 350),
        CarDef(4, "Thunder Cruiser", 0xFF212121, 0xFF757575, 450),
        CarDef(5, "Crestline", 0xFF37474F, 0xFFB0BEC5, 550),
        CarDef(6, "Neon Coupe", 0xFF1E88E5, 0xFFBBDEFB, 650),
        CarDef(7, "Baywood", 0xFF00695C, 0xFF80CBC4, 750),
        CarDef(8, "Trailblazer", 0xFF6D4C2E, 0xFFA1887F, 850),
        CarDef(9, "Dune Runner", 0xFFEF6C00, 0xFFFFCC80, 950),
        CarDef(10, "Ironhide", 0xFF424242, 0xFF9E9E9E, 1050),
        CarDef(11, "Gold Cruiser", 0xFFFDD835, 0xFFFFF9C4, 1200),
        CarDef(12, "Vortex GT", 0xFF8E24AA, 0xFFE1BEE7, 1400),
        CarDef(13, "Royal Sedan", 0xFF4527A0, 0xFFB39DDB, 1600),
        CarDef(14, "Nightshade", 0xFF1A1A1A, 0xFF00E5FF, 1800)
    )

    fun byId(id: Int): CarDef = cars.firstOrNull { it.id == id } ?: cars[0]
}

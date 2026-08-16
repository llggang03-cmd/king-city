package com.kingcity.game.data

object MapCatalog {
    val maps = listOf(
        MapDef(
            id = 0,
            name = "Odisha",
            worldWidth = 4200f,
            worldHeight = 3000f,
            unlockCost = 0,
            seed = 1001L,
            zoneLabels = listOf(
                ZoneLabel("Bhubaneswar", 0.30f, 0.35f),
                ZoneLabel("Cuttack", 0.42f, 0.20f),
                ZoneLabel("Puri", 0.22f, 0.68f),
                ZoneLabel("Dhenkanal (Nandankanan Zoo)", 0.55f, 0.30f),
                ZoneLabel("Angul", 0.68f, 0.42f),
                ZoneLabel("Talcher", 0.78f, 0.25f)
            )
        ),
        MapDef(
            id = 1,
            name = "Delhi",
            worldWidth = 2600f,
            worldHeight = 2000f,
            unlockCost = 800,
            seed = 2002L,
            zoneLabels = listOf(
                ZoneLabel("Connaught Place", 0.32f, 0.30f),
                ZoneLabel("Dwarka", 0.18f, 0.62f),
                ZoneLabel("Rohini", 0.30f, 0.14f),
                ZoneLabel("Saket", 0.55f, 0.55f)
            )
        ),
        MapDef(
            id = 2,
            name = "Mumbai",
            worldWidth = 2800f,
            worldHeight = 2100f,
            unlockCost = 1200,
            seed = 3003L,
            zoneLabels = listOf(
                ZoneLabel("Andheri", 0.24f, 0.24f),
                ZoneLabel("Bandra", 0.36f, 0.44f),
                ZoneLabel("Colaba", 0.52f, 0.72f),
                ZoneLabel("Worli", 0.44f, 0.58f)
            )
        )
    )

    fun byId(id: Int): MapDef = maps.firstOrNull { it.id == id } ?: maps[0]
}

package net.geekmc.turingcore.service.impl.basic

import net.geekmc.turingcore.data.json.JsonData
import net.minestom.server.entity.Player

val Player.data: JsonData
    get() = PlayerBasicDataService.service.getPlayerData(this)
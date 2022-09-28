package net.geekmc.turingcore.util

import net.geekmc.turingcore.service.impl.basic.PlayerBasicDataService.getData
import net.minestom.server.command.CommandSender
import net.minestom.server.command.ConsoleSender
import net.minestom.server.entity.Player

fun CommandSender.isOp(): Boolean {
    return when (this) {
        is Player -> getData()["op"] ?: false
        is ConsoleSender -> true
        else -> false
    }
}
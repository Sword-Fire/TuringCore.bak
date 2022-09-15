package net.geekmc.turingcore.permission

import net.geekmc.turingcore.data.PlayerBasicDataService.data
import net.minestom.server.command.CommandSender
import net.minestom.server.command.ConsoleSender
import net.minestom.server.entity.Player

fun CommandSender.isOp(): Boolean {
    if (this is Player)
        return data["op"] ?: false
    if (this is ConsoleSender)
        return true
    return false
}
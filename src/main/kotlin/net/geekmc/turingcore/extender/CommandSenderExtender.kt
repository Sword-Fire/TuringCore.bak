package net.geekmc.turingcore.extender

import net.geekmc.turingcore.color.send
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player

fun CommandSender.checkIsPlayer(): Boolean {
    if (this !is Player) {
        this.send("&r只有玩家能使用该命令。")
        return false
    }
    return true
}
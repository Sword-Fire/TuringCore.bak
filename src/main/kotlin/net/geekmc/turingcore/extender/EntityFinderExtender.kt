package net.geekmc.turingcore.extender

import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Player
import net.minestom.server.utils.entity.EntityFinder

fun EntityFinder.findPlayers(sender: CommandSender): List<Player> {
    return this.find(sender).filterIsInstance<Player>()
}
package net.geekmc.turingcore.command.basic

import net.geekmc.turingcore.command.args
import net.geekmc.turingcore.command.findPlayers
import net.geekmc.turingcore.command.opSyntax
import net.geekmc.turingcore.util.color.message
import net.geekmc.turingcore.util.foldToString
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import net.minestom.server.entity.Player
import world.cepi.kstom.command.kommand.Kommand

object CommandKill : Kommand({

    val targetArg = ArgumentEntity("target").onlyPlayers(true)

    opSyntax {
        if (sender !is Player) {
            sender.message("&r只有玩家能使用这个命令!")
            return@opSyntax
        }
        sender.message("&r命令用法不正确: /${context.input}")
    }

    opSyntax(targetArg) {
        val players = (!targetArg).findPlayers(sender)
        if (players.isEmpty()) {
            sender.message("&r找不到玩家: ${args.getRaw(targetArg)}")
            return@opSyntax
        }
        players.forEach {
            it.kill()
        }
        sender.message("&r已杀死玩家: ${players.foldToString()}")
    }
}, name = "kill")
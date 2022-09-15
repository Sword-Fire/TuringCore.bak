package net.geekmc.turingcore.command

import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.extender.args
import net.geekmc.turingcore.extender.findPlayers
import net.geekmc.turingcore.extender.foldToString
import net.geekmc.turingcore.extender.opSyntax
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import net.minestom.server.entity.Player
import world.cepi.kstom.command.kommand.Kommand

object KillCommand : Kommand({

    val targetArg = ArgumentEntity("target").onlyPlayers(true)

    opSyntax {
        if(sender !is Player) {
            sender.send("&r只有玩家能使用这个命令!")
            return@opSyntax
        }
        sender.send("&r命令用法不正确: /${context.input}")
    }

    opSyntax(targetArg) {

        val players = (!targetArg).findPlayers(sender)

        if (players.isEmpty()) {
            sender.send("&r找不到玩家: ${args.getRaw(targetArg)}")
            return@opSyntax
        }
        players.forEach {
            it.kill()
        }
        sender.send("&r已杀死玩家: ${players.foldToString()}")

    }

}, "kill")

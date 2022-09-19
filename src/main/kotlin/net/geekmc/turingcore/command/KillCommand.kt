package net.geekmc.turingcore.command

import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.command.kommand.OP
import net.geekmc.turingcore.command.kommand.PLAYER
import net.geekmc.turingcore.command.kommand.args
import net.geekmc.turingcore.command.kommand.format
import net.geekmc.turingcore.extender.findPlayers
import net.geekmc.turingcore.extender.foldToString
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import world.cepi.kstom.command.kommand.Kommand

object KillCommand : Kommand({

    val targetArg = ArgumentEntity("target").onlyPlayers(true)

    format(OP, PLAYER) {
        sender.send("&r命令用法不正确: /${context.input}")
    }

    format(targetArg) {

        val players = (!targetArg).findPlayers(sender)

        if (players.isEmpty()) {
            sender.send("&r找不到玩家: ${args.getRaw(targetArg)}")
            return@format
        }
        players.forEach {
            it.kill()
        }
        sender.send("&r已杀死玩家: ${players.foldToString()}")

    }

}, "kill")

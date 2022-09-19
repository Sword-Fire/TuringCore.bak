package net.geekmc.turingcore.command

import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.command.kommand.OP
import net.geekmc.turingcore.command.kommand.args
import net.geekmc.turingcore.command.kommand.format
import net.geekmc.turingcore.extender.findPlayers
import net.geekmc.turingcore.extender.foldToString
import net.geekmc.turingcore.framework.DebugLevel
import net.geekmc.turingcore.framework.debugLevel
import net.minestom.server.command.builder.arguments.ArgumentWord
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import world.cepi.kstom.command.kommand.Kommand


object DebugCommand : Kommand({

    val targetArg = ArgumentEntity("target").onlyPlayers(true)
    // Allow debug levels in both upper and lower case
    val levelArg = ArgumentWord("level").from(*kotlin.run {
        val list = mutableListOf<String>()
        list.addAll(DebugLevel.values().map { it.toString() })
        list.addAll(DebugLevel.values().map { it.toString().lowercase() })
        list.toTypedArray()
    })

    format(OP) {
        sender.send("&r命令用法不正确: /${context.input}")
    }

    format(OP, levelArg) {
        sender.debugLevel = enumValueOf((!levelArg).toString().uppercase())
        sender.send("&g你的调试等级已设为 &y${(!levelArg).toString().uppercase()}.")
    }

    format(OP, levelArg, targetArg) {

        val players = (!targetArg).findPlayers(sender)
        if (players.isEmpty()) {
            sender.send("&r找不到玩家: ${args.getRaw(targetArg)}")
            return@format
        }
        players.forEach {
            it.debugLevel = enumValueOf((!levelArg).toString().uppercase())
        }

        sender.send("&g已将调试等级 &y${(!levelArg).toString().uppercase()} &g赋与 &y${players.foldToString()}")
    }

}, "debug")


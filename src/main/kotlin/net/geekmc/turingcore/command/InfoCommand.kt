package net.geekmc.turingcore.command

import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.extender.addPermission
import net.geekmc.turingcore.extender.args
import net.geekmc.turingcore.extender.findPlayers
import net.geekmc.turingcore.extender.foldToString
import net.minestom.server.command.builder.arguments.ArgumentWord
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.command.kommand.Kommand


object PermissionCommand : Kommand({

    val add by literal
    val remove = ArgumentWord("remove").from("remove", "rem")
    val target = ArgumentEntity("target").onlyPlayers(true)
    val perm = ArgumentWord("perm")

    syntax {
        sender.send("&r命令用法不正确。")
    }

    syntax(add, target, perm) {

        val players = (!target).findPlayers(sender)
        if (players.isEmpty()) {
            sender.send("&r找不到玩家: ${args.getRaw(target)}")
            return@syntax
        }
        players.forEach {
            it.addPermission(!perm)
        }

        sender.send("&g已将权限 ${!perm} 赋与 ${players.foldToString()}")
    }

}, "perm", "p")


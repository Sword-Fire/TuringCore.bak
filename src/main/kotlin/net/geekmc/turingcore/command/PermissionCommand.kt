package net.geekmc.turingcore.command

import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.extender.*
import net.minestom.server.command.builder.arguments.ArgumentWord
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import net.minestom.server.utils.entity.EntityFinder
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.command.kommand.Kommand


object PermissionCommand : Kommand({

    val add by literal
    val remove = ArgumentWord("remove").from("remove", "rem")
    val target = ArgumentEntity("target").onlyPlayers(true).setDefaultValueToSelf()
    val perm = ArgumentWord("perm")

    syntax {
        sender.send("&r命令用法不正确: /${context.input}")
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

    syntax(remove, target, perm) {

        val players = (!target).findPlayers(sender)
        if (players.isEmpty()) {
            sender.send("&r找不到玩家: ${args.getRaw(target)}")
            return@syntax
        }
        players.forEach {
            it.removePermission(!perm)
        }

        sender.send("&g已将权限 ${!perm} 移除自 ${players.foldToString()}")

    }

}, "perm", "p")


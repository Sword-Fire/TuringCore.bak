package net.geekmc.turingcore.command

import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.command.kommand.OP
import net.geekmc.turingcore.command.kommand.args
import net.geekmc.turingcore.command.kommand.format
import net.geekmc.turingcore.extender.findPlayers
import net.geekmc.turingcore.extender.foldToString
import net.geekmc.turingcore.extender.setDefaultValueToSelf
import net.minestom.server.command.builder.arguments.ArgumentLiteral
import net.minestom.server.command.builder.arguments.ArgumentWord
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import net.minestom.server.entity.Player
import world.cepi.kstom.command.kommand.Kommand
import world.cepi.kstom.util.addPermission

object PermissionCommand : Kommand({

    val addArg = ArgumentLiteral("add")
    val removeArg = ArgumentWord("remove").from("remove", "rem")
    val listArg = ArgumentLiteral("list")
    val targetArg = ArgumentEntity("target").onlyPlayers(true).setDefaultValueToSelf()
    val permArg = ArgumentWord("perm")

    format(OP) {
        sender.send("&r命令用法不正确: /${context.input}")
        sender.send("&r输入 /perm help 来了解用法。")
    }

    /**
     * Add permission to multi players.
     */
    format(OP, addArg, permArg, targetArg) {

        val players = (!targetArg).findPlayers(sender)
        if (players.isEmpty()) {
            // 不能使用 !target == target.defaultValue
            if (sender !is Player && args.getRaw(targetArg) == "") sender.send("&r非玩家使用该命令时不能省略参数 target 。")
            else sender.send("&r找不到玩家: ${args.getRaw(targetArg)}")
            return@format
        }
        players.forEach {
            it.addPermission(!permArg)
        }

        sender.send("&g已将权限 ${!permArg} 赋与 ${players.foldToString()}")
    }

    /**
     * Remove permission from multi players.
     */
    format(removeArg, permArg, targetArg) {

        val players = (!targetArg).findPlayers(sender)
        if (players.isEmpty()) {
            if (sender !is Player && args.getRaw(targetArg) == "") sender.send("&r非玩家使用该命令时不能省略参数 target 。")
            else sender.send("&r找不到玩家: ${args.getRaw(targetArg)}")
            return@format
        }
        players.forEach {
            it.removePermission(!permArg)
        }
        sender.send("&y已将权限 ${!permArg} 移除自 ${players.foldToString()}")

    }

    /**
     * List permissions of one player.
     */
    format(OP, listArg, targetArg) {

        val target = (!targetArg).findFirstPlayer(sender)
        if (target == null) {
            if (sender !is Player && args.getRaw(targetArg) == "") sender.send("&r非玩家使用该命令时不能省略参数 target 。")
            else sender.send("&r找不到玩家: ${args.getRaw(targetArg)}")
            return@format
        }

        sender.send("&g玩家 ${target.username} 拥有以下权限:")
        for (p in target.allPermissions) {
            sender.send(" &y- ${p.permissionName}")
        }

    }

}, "perm", "p")


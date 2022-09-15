package net.geekmc.turingcore.command

import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.data.PlayerBasicDataService.data
import net.geekmc.turingcore.extender.args
import net.geekmc.turingcore.extender.foldToString
import net.geekmc.turingcore.extender.opSyntax
import net.geekmc.turingcore.permission.isOp
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.command.builder.arguments.ArgumentBoolean
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import org.jetbrains.annotations.Contract
import world.cepi.kstom.command.arguments.defaultValue
import world.cepi.kstom.command.kommand.KSyntax
import world.cepi.kstom.command.kommand.Kommand



object OpCommand : Kommand({

    val targetArg = ArgumentEntity("target").onlyPlayers(true).singleEntity(true)
    val booleanArg = ArgumentBoolean("boolean").defaultValue(true)

    opSyntax {
        sender.send("&r命令用法不正确: /${context.input}")
        sender.send("&r正确用法: /op <Player> [Boolean]")
    }

    opSyntax(targetArg, booleanArg) {

        val player = (!targetArg).findFirstPlayer(sender)

        if (player == null) {
            sender.send("&r找不到玩家: ${args.getRaw(targetArg)}")
            return@opSyntax
        }
        player.data["op"] = !booleanArg
        sender.send("&g已将玩家玩家 &y${player.username} &g的管理员权限设置为 &r${!booleanArg}")

    }

}, "op")

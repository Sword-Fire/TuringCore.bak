package net.geekmc.turingcore.command.basic

import net.geekmc.turingcore.command.opSyntax
import net.geekmc.turingcore.util.color.message
import net.geekmc.turingcore.util.foldToString
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import net.minestom.server.command.builder.arguments.relative.ArgumentRelativeVec3
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player
import world.cepi.kstom.command.kommand.Kommand

object CommandTeleport : Kommand({

    val vecArg = ArgumentRelativeVec3("vec")
    val targetArg = ArgumentEntity("target")

    playerCallbackFailMessage = {
        it.message("&r只有玩家能使用这个命令。")
    }

    opSyntax {
        sender.message("&r命令用法不正确: /${context.input}")
    }

    opSyntax(vecArg) {
        if (sender !is Player) {
            sender.message("&r只有玩家能使用这个命令。")
            return@opSyntax
        }
        val pos = Pos.fromPoint((!vecArg).from(player))
        player.teleport(pos)
        sender.message("&g已将你传送至 x:${pos.x} y:${pos.y} z:${pos.z}")
    }

    opSyntax(vecArg, targetArg) {
        val entities = (!targetArg).find(sender)
        val pos = Pos.fromPoint((!vecArg).from(player))
        entities.forEach {
            it.teleport(pos)
        }
        val info = entities.foldToString(", ") {
            when (it) {
                is Player -> it.username
                else -> it.entityType.toString()
            }
        }
        sender.message("&g已将 &y$info &g传送至 x:${pos.x} y:${pos.y} z:${pos.z}")
    }
}, name = "teleport", aliases = arrayOf("tp"))
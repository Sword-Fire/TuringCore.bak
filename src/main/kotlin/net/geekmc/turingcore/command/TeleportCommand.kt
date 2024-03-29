package net.geekmc.turingcore.command

import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.command.kommand.OP
import net.geekmc.turingcore.command.kommand.PLAYER
import net.geekmc.turingcore.command.kommand.format
import net.geekmc.turingcore.extender.foldToString
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import net.minestom.server.command.builder.arguments.relative.ArgumentRelativeVec3
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player
import world.cepi.kstom.command.kommand.Kommand


object TeleportCommand : Kommand({

    val vecArg = ArgumentRelativeVec3("vec")
    val targetArg = ArgumentEntity("target")

    format(OP) {
        sender.send("&r命令用法不正确: /${context.input}")
    }

    format(OP, PLAYER, vecArg) {

        if (sender !is Player) {
            sender.send("&r只有玩家能使用这个命令。")
            return@format
        }

        val pos = Pos.fromPoint((!vecArg).from(player))

        player.teleport(pos)
        sender.send("&g已将你传送至 x:${pos.x} y:${pos.y} z:${pos.z}")

    }

    format(OP, vecArg, targetArg) {

        val entities = (!targetArg).find(sender)
        val pos = Pos.fromPoint((!vecArg).from(player))

        entities.forEach {
            it.teleport(pos)
        }

        val entitiesInfo = entities.foldToString(", ") {
            if (it is Player) it.username else it.entityType.toString()
        }
        sender.send("&g已将 &y$entitiesInfo &g传送至 x:${pos.x} y:${pos.y} z:${pos.z}")

    }

}, "tp")


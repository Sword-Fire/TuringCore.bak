package net.geekmc.turingcore.command

import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.command.KTest.t
import net.geekmc.turingcore.extender.addPermission
import net.geekmc.turingcore.extender.args
import net.geekmc.turingcore.extender.findPlayers
import net.geekmc.turingcore.extender.foldToString
import net.geekmc.turingcore.motd.MotdService
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.CommandExecutor
import net.minestom.server.command.builder.arguments.ArgumentWord
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import net.minestom.server.entity.Player
import net.minestom.server.event.entity.EntityTickEvent
import net.minestom.server.potion.PotionEffect
import net.minestom.server.tag.Tag
import net.minestom.server.utils.entity.EntityFinder
import world.cepi.kstom.Manager
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.command.kommand.Kommand
import world.cepi.kstom.event.listen
import world.cepi.kstom.event.listenOnly
import javax.script.ScriptEngineManager

object TestCommand : Kommand({

    syntax {
        sender.send("默认执行器")
        sender.send(EntityFinder().setTargetSelector(EntityFinder.TargetSelector.SELF).find(sender).toString())
    }.onlyPlayers()

//    syntax(add, target, perm) {
//
//        val players = (!target).findPlayers(sender)
//        if (players.isEmpty()) {
//            sender.send("&r找不到玩家: ${args.getRaw(target)}")
//            return@syntax
//        }
//        players.forEach {
//            it.addPermission(!perm)
//        }
//
//        sender.send("&g已将权限 ${!perm} 赋与 ${players.foldToString()}")
//    }

}, "test", "t")
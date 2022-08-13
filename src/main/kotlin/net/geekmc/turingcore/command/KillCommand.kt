package net.geekmc.turingcore.command

import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.CommandExecutor
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import net.minestom.server.event.EventDispatcher
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.message.ChatPosition
import net.minestom.server.message.Messenger
import net.minestom.server.utils.entity.EntityFinder
import world.cepi.kstom.Manager

object KillCommand : Command("kill") {
    init {

        defaultExecutor =
            CommandExecutor { sender, _ ->
                sender.sendMessage("&c命令用法不正确。")
            }

        addSyntax({ sender: CommandSender, context: CommandContext ->

            if (sender !is Player) {
                sender.sendMessage("&c只有玩家能使用这个命令")
                return@addSyntax
            }
            sender.kill()

        })

        addSyntax({ sender: CommandSender, context: CommandContext ->

            val finder = context.get<EntityFinder>("player")
            val p = finder.findFirstPlayer(null, null)
            if (p == null) {
                sender.sendMessage("找不到玩家 ${context.getRaw("player")}.")
                return@addSyntax
            }

            p.kill()

        }, ArgumentType.Entity("player").onlyPlayers(true).singleEntity(true))
    }
}
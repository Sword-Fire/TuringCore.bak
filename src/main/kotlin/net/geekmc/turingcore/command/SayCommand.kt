package net.geekmc.turingcore.command

import net.geekmc.turinglib.color.send
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.CommandExecutor
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.event.EventDispatcher
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.message.ChatPosition
import net.minestom.server.message.Messenger
import net.minestom.server.utils.entity.EntityFinder

object SayCommand : Command("say") {
    init {

        defaultExecutor =
            CommandExecutor { sender, _ ->
                sender.send("&c命令用法不正确。")
            }

        addSyntax(
            { sender: CommandSender, context: CommandContext ->

                //获取player参数
                val finder = context.get<EntityFinder>("player")
                val p = finder.findFirstPlayer(null, null)
                if (p == null) { //
                    sender.send("Can not find the player ${context.getRaw("player")}.")
                    return@addSyntax // 找不到指定的玩家，直接返回
                }
                val playerChatEvent = PlayerChatEvent(
                    p,
                    MinecraftServer.getConnectionManager().onlinePlayers,
                    { Component.text("<" + p.username + "> " + context.get("word")) },
                    context.get("word")
                )
                EventDispatcher.callCancellable(playerChatEvent) {
                    val formatFunction = playerChatEvent.chatFormatFunction
                    val textObject: Component = formatFunction?.apply(playerChatEvent) ?: // Default format
                    playerChatEvent.defaultChatFormat.get()
                    val recipients = playerChatEvent.recipients
                    if (!recipients.isEmpty()) {
                        // delegate to the messenger to avoid sending messages we shouldn't be
                        Messenger.sendMessage(recipients, textObject, ChatPosition.CHAT, p.uuid)
                    }
                }
            }, ArgumentType.Entity("player").onlyPlayers(true).singleEntity(true), ArgumentType.Word("word")
        )
    }
}
package net.geekmc.turingcore.color

import net.minestom.server.command.CommandSender

/**
 * 向玩家发送信息，支持自定义MiniMessage简写
 */
fun CommandSender.send(message: String) {
    sendMessage(message.toComponent())
}
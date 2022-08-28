package net.geekmc.turingcore.color

import net.minestom.server.command.CommandSender

fun CommandSender.send(message:String) {
    sendMessage(message.toComponent())
}
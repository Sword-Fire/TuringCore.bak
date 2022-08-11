package net.geekmc.turingcore.command

import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext

object StopCommand :Command("stop"){

    init {
        setDefaultExecutor { sender, _ ->
            sender.sendMessage("&c正在关闭服务器...")
            MinecraftServer.stopCleanly()
        }
    }

}
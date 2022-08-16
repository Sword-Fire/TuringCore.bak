package net.geekmc.turingcore.command

import net.geekmc.turinglib.color.send
import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext

object StopCommand :Command("stop"){

    init {
        setDefaultExecutor { sender, _ ->
            sender.send("<red>正在关闭服务器...")
            MinecraftServer.stopCleanly()
        }
    }

}
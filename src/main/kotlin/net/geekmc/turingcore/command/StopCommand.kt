package net.geekmc.turingcore.command

import net.geekmc.turingcore.color.send
import net.minestom.server.MinecraftServer
import net.minestom.server.command.builder.Command

object StopCommand :Command("stop"){

    init {
        setDefaultExecutor { sender, _ ->
            sender.send("&r正在关闭服务器...")
            MinecraftServer.stopCleanly()
        }
    }

}
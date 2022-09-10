package net.geekmc.turingcore.command

import net.geekmc.turingcore.color.send
import net.minestom.server.MinecraftServer
import world.cepi.kstom.command.kommand.Kommand

object StopCommand : Kommand({
    syntax {
        sender.send("&r正在关闭服务器...")
        MinecraftServer.stopCleanly()
    }
}, "stop")
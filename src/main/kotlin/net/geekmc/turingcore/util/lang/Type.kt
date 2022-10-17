package net.geekmc.turingcore.util.lang

import net.minestom.server.command.CommandSender

interface Type {

    fun init(source: Map<String, Any>): Boolean

    fun send(sender: CommandSender, vararg args: Any)
}
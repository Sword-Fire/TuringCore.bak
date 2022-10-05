package net.geekmc.turingcore.util.lang

import net.geekmc.turingcore.data.yaml.YamlData
import net.minestom.server.command.CommandSender

interface Type {

    fun init(node: String, data: YamlData): Boolean

    fun send(sender: CommandSender, vararg args: Any)
}
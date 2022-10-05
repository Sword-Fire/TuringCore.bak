package net.geekmc.turingcore.util.lang

import net.geekmc.turingcore.data.yaml.YamlData
import net.geekmc.turingcore.util.color.message
import net.geekmc.turingcore.util.replaceWithOrder
import net.minestom.server.command.CommandSender

class TypeText : Type {

    lateinit var texts: MutableList<String>
        private set

    override fun init(node: String, data: YamlData): Boolean {
        texts = mutableListOf<String>().apply {
            if (data.get<String>(node)?.let { add(it) } != null) {
                return true
            }
            addAll(data.get<List<String>>(node) ?: return false)
        }
        return true
    }

    override fun send(sender: CommandSender, vararg args: Any) {
        texts.forEach { sender.message(it.replaceWithOrder(args)) }
    }
}
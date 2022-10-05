package net.geekmc.turingcore.util.lang

import net.geekmc.turingcore.data.yaml.YamlData
import net.minestom.server.command.CommandSender

class TypeActionBar : Type {

    lateinit var text: String
        private set

    var duration = 20L
        private set

    override fun init(node: String, data: YamlData): Boolean {
        val map = data.get<MutableMap<String, String>>(node) ?: return false
        if (map["type"]?.lowercase() != "actionbar") {
            return false
        }
        text = map["text"] ?: return false
        duration = map["duration"]?.toLongOrNull() ?: return false
        return true
    }

    override fun send(sender: CommandSender, vararg args: Any) {
        TODO("Not yet implemented")
    }
}
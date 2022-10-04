package net.geekmc.turingcore.util.lang

import net.geekmc.turingcore.TuringCore
import net.geekmc.turingcore.data.yaml.YamlData
import net.geekmc.turingcore.util.resolvePath
import net.geekmc.turingcore.util.saveResource

object LangUtil {

    private const val PATH = "Languages.yml"

    private val messageMap = mutableMapOf<String, String>()

    fun init() {
        TuringCore.INSTANCE.saveResource(PATH)
        val data = YamlData(TuringCore.INSTANCE.resolvePath(PATH))
        data.getKeys(deep = false).forEach {
            val message = data.get<String>(it) ?: return@forEach
            messageMap[it] = message
        }
    }
}
package net.geekmc.turingcore.util.color

import net.geekmc.turingcore.TuringCore
import net.geekmc.turingcore.data.yaml.YamlData
import net.geekmc.turingcore.util.resolvePath
import net.geekmc.turingcore.util.saveResource
import net.geekmc.turingcore.util.warn
import net.kyori.adventure.text.minimessage.MiniMessage
import java.util.*

object ColorUtil {

    private const val PATH = "CustomColors.yml"

    val miniMessage = MiniMessage.miniMessage()

    /**
     * 存储自定义简写，并按照简写的长度从大到小排序。
     */
    val colorMap: TreeMap<String, String>
        get() = TreeMap<String, String> { x, y ->
            if (x.length != y.length) {
                -x.length.compareTo(y.length) // * - 1
            } else {
                x.compareTo(y)
            }
        }

    fun init() {
        TuringCore.INSTANCE.saveResource(PATH)
        val data = YamlData(TuringCore.INSTANCE.resolvePath(PATH))
        val colors = data.get<List<String>>("colors", emptyList())
        colors.forEach {
            val split = it.split("@")
            if (split.size != 2) {
                warn("无法解析颜色格式: $it")
                return@forEach
            }
            colorMap += split[0] to "<${split[1]}>"
        }
    }
}
package net.geekmc.turingcore.color

import net.geekmc.turingcore.TuringCore
import net.geekmc.turingcore.data.yaml.YamlData
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.geekmc.turingcore.extender.resolvePath
import java.util.TreeMap

fun String.toComponent(): Component {
    return ColorUtil.castStringToComponent(this)
}

object ColorUtil {

    private val miniMessage = MiniMessage.miniMessage()

    // Custom key -> MiniMessage tag as string
    // &a -> <#111111> for example
    // Sorted by length from largest to smallest.
    private val colorMap = TreeMap<String, String>() { x, y ->
        if (x.length != y.length) return@TreeMap -x.length.compareTo(y.length) // *-1
        return@TreeMap x.compareTo(y)
    }


    fun init() {

        val data = YamlData(TuringCore.INSTANCE.resolvePath("CustomColors.yml"))

        val colors: List<String> = data.get("colors", listOf())
        for (str in colors) {
            val split = str.split("@")
            if (split.size != 2) {
                TuringCore.INSTANCE.logger.warn("无法解析颜色格式: $str")
                continue
            }
            colorMap[split[0]] = "<${split[1]}>"
        }

    }

    fun castStringToComponent(string: String): Component {
        // 防止&&被替换
        var str = string
        str = str.replace("&&", "{§}")
        for ((k, v) in colorMap) {
            str = str.replace(k, v)
        }
        str = str.replace("{§}", "&")
        return miniMessage.deserialize(str)
    }

}
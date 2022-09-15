package net.geekmc.turingcore.color

import net.geekmc.turing.Turing.saveResource
import net.geekmc.turingcore.TuringCore
import net.geekmc.turingcore.data.yaml.YamlData
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.geekmc.turingcore.extender.resolvePath
import net.geekmc.turingcore.extender.saveResource
import java.util.TreeMap

fun String.toComponent(): Component {
    return ColorUtil.castStringToComponent(this)
}

/**
 * 用于把String转换成Component的工具，内部使用MiniMessage实现。
 *
 * 支持自定义简写，如&r -> <red>。
 *
 */
object ColorUtil {

    private val miniMessage = MiniMessage.miniMessage()
    /**
     * 存储自定义简写，并按照简写的长度从大到小排序。
     *
     * 例如 &a -> <#111111>
     */
    private val colorMap = TreeMap<String, String> { x, y ->
        if (x.length != y.length) return@TreeMap -x.length.compareTo(y.length) // *-1
        return@TreeMap x.compareTo(y)
    }

     private const val CONFIG = "CustomColors.yml"

    /**
     * 从配置文件中读取自定义简写。
     */
    fun init() {

        TuringCore.INSTANCE.saveResource("CustomColors.yml")
        val data = YamlData(TuringCore.INSTANCE.resolvePath(CONFIG))

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

    /**
     * 把String转换成Component。
     *
     * String中的&&将被替换成&。
     */
    fun castStringToComponent(string: String): Component {
        var str = string
        str = str.replace("&&", "{§}")
        for ((k, v) in colorMap) {
            str = str.replace(k, v)
        }
        str = str.replace("{§}", "&")
        return miniMessage.deserialize(str)
    }

}
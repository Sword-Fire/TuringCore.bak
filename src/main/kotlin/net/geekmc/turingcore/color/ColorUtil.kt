package net.geekmc.turingcore.color

import net.geekmc.turingcore.TuringCore
import net.geekmc.turingcore.taml.Taml
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.geekmc.turingcore.extender.resolvePath

fun String.toComponent(): Component {
    return ColorUtil.castStringToComponent(this)
}

object ColorUtil {

    val miniMessage = MiniMessage.miniMessage()

    // Custom key -> MiniMessage tag
    // &a -> <#111111> for example
    val colorMap = mutableMapOf<String, String>()

    data class Test(var x:Int=0,var y:Int=0)

    fun init() {

        val taml = Taml(TuringCore.INSTANCE.resolvePath("CustomColors.yml"))
        val colors: List<String> = taml.get("colors", listOf())
        for (str in colors) {
            val split = str.split("@")
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
package net.geekmc.turingcore.data.json

import java.nio.file.Path
import kotlin.io.path.*

/**
 * JsonData 用于以序列化方式存储数据。
 *
 * 你可以认为[JsonData]是一个简化版的Map，键值为
 *
 * String -> 可序列化对象
 *
 * ```
 * 可序列化对象包括任何带有@Serializable注解的对象，例如Int,ArrayList<Int>等;
 * 和所有在 TuringJson 中具有对应序列化器的对象，以及包含它们的具有kotlin原生序列化支持的容器对象，例如ItemStack,Set<ItemStack>。
 * ```
 * 使用[JsonData.load]可以获取一个JsonData。
 *
 * 每个JsonData都和一个文件绑定，这使得你能使用[JsonData.save]方法来保存数据。
 *
 * 使用样例:
 *
 *(实际上,你应当直接使用 PlayerDataService 来存储玩家数据)
 * ```
 * // on player join
 * val data = JsonData.load("playerdata/${player.name}.json")
 * player.money = data["money"]?:0
 * player.itemInMainHand = data.get<ItemStack>("mainHandItem", ItemStack(Material.AIR))
 *
 * // on player disconnect
 * data["money"] = player.money
 * data["mainHandItem"] = player.itemInMainHand
 * data.save()
 * ```
 */
class JsonData private constructor(private val file: Path) : SerializableData(
    kotlin.run {
        if (file.exists()) {
            file.readText()
        } else EMPTY_FILE_STRING
    }
) {

    fun save() {
        if (!file.exists()) {
            file.parent.createDirectories()
            file.createFile()
        }
        file.writeText(super.saveToString())
    }

    companion object {
        fun load(file: Path) = JsonData(file)
    }

}
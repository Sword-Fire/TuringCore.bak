package net.geekmc.turingcore.data.json

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import net.geekmc.turingcore.data.TuringJson
import net.geekmc.turingcore.framework.Logger


open class SerializableData constructor(stringInput: String) {

    // SerializableData 对象创建时，所有数据都是未被反序列化的，存储在 serializableData
    // 读取数据时，查找优先级为：writeData > readData > serializedData
    // 如果 readData 中未找到数据，则从 serializedData 中反序列化数据，并存储在 readData 中，作为缓存提高性能
    // 写入数据会写入到 writeData 中
    val writeData = LinkedHashMap<String, @Serializable Any>()
    val writeDataFunc = LinkedHashMap<String, () -> String>()
    val readData = LinkedHashMap<String, @Serializable Any>()
    val serializedData: LinkedHashMap<String, String>

    init {
        serializedData = TuringJson.decodeFromString(stringInput)
    }

    /**
     * 泛型参数必须是非抽象类，例如 ArrayList<Int>，而不是 List<Int>
     */
    inline operator fun <reified T : @Serializable Any> get(key: String): T? {

        if (writeData.contains(key)) {
            Logger.trace("get $key from writeData")
            return writeData[key] as T
        }
        if (readData.contains(key)) {
            Logger.trace("get $key from readData")
            return readData[key] as T
        }
        if (serializedData.contains(key)) {
            Logger.trace("get $key from serializedData")
            val value: T = TuringJson.decodeFromString(serializedData[key]!!)
            readData[key] = value
            return value
        }

        Logger.trace("get $key from null")
        return null
    }


    inline operator fun <reified T : @Serializable Any> get(key: String, default: T): T = get(key) ?: default

    /**
     * 泛型参数必须和 get 时的泛型参数一致
     */
    inline operator fun <reified T : @Serializable Any> set(key: String, value: T) {

        writeData[key] = value
        writeDataFunc[key] = { TuringJson.encodeToString(value) }

    }

    fun saveToString(): String {

        for ((k, v) in writeData) {
            serializedData[k] = writeDataFunc[k]!!()
        }

        return TuringJson.encodeToString(serializedData)

    }

    companion object {
        const val EMPTY_FILE_STRING = "{}"
        fun load(string: String) = SerializableData(string)
        fun empty() = SerializableData(EMPTY_FILE_STRING)
    }

}
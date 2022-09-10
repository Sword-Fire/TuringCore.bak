package net.geekmc.turingcore.taml

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor
import org.yaml.snakeyaml.representer.Representer
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Path
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists

// 代表和一个Yaml文件关联的、存储在内存中的数据。
class Data(path: Path, yaml: Yaml = defaultYaml) {

    companion object {

        private val defaultYaml: Yaml
        private val defaultDumperOptions: DumperOptions

        init {
            // 默认的Yaml风格
            defaultDumperOptions = DumperOptions()
            defaultDumperOptions.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
            defaultYaml = Yaml(defaultDumperOptions)
        }
    }

    private val path: Path
    private val rootObject: MutableMap<Any?, Any?>
    private var yaml: Yaml

    init {
        this.path = path
        this.yaml = yaml
        rootObject = if (!path.exists()) {
            LinkedHashMap()
        } else {
            yaml.load(FileReader(path.absolutePathString())) ?: LinkedHashMap()
        }
    }

    constructor(path: Path, clazz: Class<*>) : this(
        path,
        Yaml(CustomClassLoaderConstructor(clazz.classLoader), Representer(), defaultDumperOptions)
    )

    constructor(path: Path, cLoader: ClassLoader) : this(
        path,
        Yaml(CustomClassLoaderConstructor(cLoader), Representer(), defaultDumperOptions)
    )

    operator fun <T> get(keyStr: String): T? {
        val keys = keyStr.split(".")
        var obj: Map<Any?, Any?> = rootObject
        val iter = keys.iterator()
        while (iter.hasNext()) {

            var key: Any? = iter.next()

            if (obj[key] == null) {
                // 转换失败，返回null
                try {
                    key = key.toString().toInt()
                } catch (e: java.lang.NumberFormatException) {
                    return null
                }
                // String转成Integer作为key还是找不到，返回null
                if (obj[key] == null) {
                    return null
                }
            }
            if (!iter.hasNext()) {
                @Suppress("UNCHECKED_CAST")
                return obj[key] as? T ?: return null
            } else {
                @Suppress("UNCHECKED_CAST")
                obj = obj[key] as? Map<Any?, Any?> ?: return null
            }

        }
        return null
    }

    fun <T> get(keyStr: String, default: T): T {
        return get(keyStr) ?: default
    }

    operator fun <T> set(keyStr: String, value: T): Boolean {
        val keys = keyStr.split(".")

        var obj: MutableMap<Any?, Any?> = rootObject
        val iter = keys.iterator()

        while (iter.hasNext()) {
            val key = iter.next()
            if (iter.hasNext()) {

                // 尝试找已经存在的Map,不存在则创建一个
                @Suppress("UNCHECKED_CAST")
                obj = obj[key] as? MutableMap<Any?, Any?> ?: let {
                    obj[key] = LinkedHashMap<Any?, Any?>()
                    obj[key] as MutableMap<Any?, Any?>
                }

            } else {
                obj[key] = value
                return true
            }
        }
        return false
    }

    fun <T> appendList(keyStr: String, value: T): Boolean {
        val list: MutableList<T> = this[keyStr] ?: let {
            val ret = LinkedList<T>()
            this[keyStr] = ret
            ret
        }
        list.add(value)
        return true
    }

    fun save() {
        yaml.dump(rootObject, FileWriter(path.absolutePathString()))
    }

}
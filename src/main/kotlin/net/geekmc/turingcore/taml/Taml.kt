package net.geekmc.turingcore.taml

import org.apache.tools.ant.taskdefs.Classloader
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

class Taml(val path: Path,clazz: Class<*>? = null, yaml: Yaml? = null) {

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

    private val rootObject: MutableMap<Any?, Any?>
    var usedYaml: Yaml
        private set

    init {


        // yaml 作为构造参数比 clazz 有更高优先级。
        usedYaml = yaml
            ?: if (clazz != null) Yaml(
                CustomClassLoaderConstructor(clazz.classLoader),
                Representer(),
                defaultDumperOptions
            ) else defaultYaml

        rootObject = if (!path.exists()) {
            LinkedHashMap()
        } else {
            usedYaml.load(FileReader(path.absolutePathString())) ?: LinkedHashMap()
        }

    }

    // Taml(String, Yaml)
    constructor(pathStr: String, yaml: Yaml = defaultYaml, clazz: Class<*>? = null) : this(
        Path.of(pathStr),
        clazz,
        yaml
    )

    operator fun <T> get(keyString: String): T? {
        val keys = keyString.split(".")
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

    operator fun <T> get(keyStr: String, default: T): T {
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
        usedYaml.dump(rootObject, FileWriter(path.absolutePathString()))
    }

}
package net.geekmc.turingcore.util

import net.minestom.server.extensions.Extension
import java.io.File
import java.nio.file.Path

/**
 * Save the resource to the given path.Will NOT replace the existing file.
 */
fun Extension.saveResource(resource: String) {
    val targetPath = dataDirectory.resolve(resource)
    if (targetPath.toFile().exists()) {
        return
    }
    savePackagedResource(resource)
}

//问题：此处可能给个无文件
fun Extension.resolvePath(path: String): Path {
    return dataDirectory.resolve(path)
}

//fun Extension.resolveFile(file: String): File {
//    return dataDirectory.resolve(file).toFile()
//}
package net.geekmc.turingcore.extender

import net.minestom.server.extensions.Extension
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

/**
 * Resolve the given path to the directory of extension.
 */
fun Extension.resolvePath(path: String): Path {
    return dataDirectory.resolve(path)
}

//fun Extension.resolveFile(file: String): File {
//    return dataDirectory.resolve(file).toFile()
//}
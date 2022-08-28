package net.geekmc.turingcore.util

import net.minestom.server.extensions.Extension
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

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
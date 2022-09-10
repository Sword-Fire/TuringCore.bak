package net.geekmc.turingcore.framework

import net.kyori.adventure.text.Component
import net.minestom.server.extensions.Extension
import world.cepi.kstom.command.arguments.ArgumentByte

object TuringFramework {

    fun init(){
        Logger.init()
    }

    // prefix package path -> FrameworkSettings of ex
    val frameworkRegistries: HashSet<FrameworkRegistry> = HashSet()

    fun registerExtension(packagePath: String, extension: Extension): FrameworkRegistry {

        val frameworkRegistry = FrameworkRegistry(packagePath, extension)
        frameworkRegistries.add(frameworkRegistry)

        return frameworkRegistry
    }

}

class FrameworkRegistry(val packagePath: String, var extension: Extension) {

    var consolePrefix: String = ""
    var playerPrefix: Component = Component.text("")

    fun remove() {
        TuringFramework.frameworkRegistries.remove(this)
    }

}
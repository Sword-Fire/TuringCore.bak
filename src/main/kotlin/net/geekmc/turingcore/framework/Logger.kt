package net.geekmc.turingcore.framework

import net.geekmc.turingcore.TuringCore
import world.cepi.kstom.Manager

//TODO: 一个给玩家的info debug等拓展函数
object Logger {

    fun init() {
        Manager.command.consoleSender.debugLevel = DebugLevel.INFO
    }

    /**
     * Get the registry of the extension whose function called Logger.info() or something similar.
     */
    fun getRegistry(): FrameworkRegistry? {

        for (frameworkRegistry in TuringFramework.frameworkRegistries) {
            //[0]为Thread方法
            //[1]为getIndex方法
            //[2]为info之类的方法
            //[3]为调用Logger的方法
            if (Thread.currentThread().stackTrace[3].className.startsWith(frameworkRegistry.packagePath)) {
                return frameworkRegistry
            }
        }

        return null

    }

    fun log(msg: String, msgLevel: DebugLevel) {
        val registry = getRegistry()
        if (registry == null) {
            TuringCore.INSTANCE.logger.warn("Failed to get the registry when try to log message $msg")
            return
        }

        if (Manager.command.consoleSender.debugLevel.toInt() < msgLevel.toInt()) return
        registry.extension.logger.run {
            when (msgLevel) {
                DebugLevel.OFF -> Unit
                DebugLevel.ERROR -> error(registry.consolePrefix + msg)
                DebugLevel.WARN -> warn(registry.consolePrefix + msg)
                DebugLevel.INFO -> info(registry.consolePrefix + msg)
                DebugLevel.DEBUG -> info("<DEBUG>" + registry.consolePrefix + msg)
                DebugLevel.TRACE -> info("<TRACE>" + registry.consolePrefix + msg)
            }
        }

    }

    fun error(msg: String) {
        log(msg, DebugLevel.ERROR)
    }

    fun warn(msg: String) {
        log(msg, DebugLevel.WARN)
    }

    fun info(msg: String) {
        log(msg, DebugLevel.INFO)
    }

    fun debug(msg: String) {
        log(msg, DebugLevel.DEBUG)
    }

    fun trace(msg: String) {
        log(msg, DebugLevel.TRACE)
    }
}
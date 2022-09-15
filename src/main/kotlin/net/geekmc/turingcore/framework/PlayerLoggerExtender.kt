package net.geekmc.turingcore.framework

import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.builtins.serializer
import net.geekmc.turingcore.color.send
import net.minestom.server.entity.Player

private fun Player.log(msg: String, msgLevel: DebugLevel) {

    if (this.debugLevel == DebugLevel.OFF) return
    if (this.debugLevel.toInt() < msgLevel.toInt()) return

    val prefix = when (msgLevel) {
        DebugLevel.OFF -> ""
        DebugLevel.ERROR -> "&dr<ERROR>"
        DebugLevel.WARN -> "&r<WARN>"
        DebugLevel.INFO -> "&w<INFO>"
        DebugLevel.DEBUG -> "&y<DEBUG>"
        DebugLevel.TRACE -> "&g<TRACE>"
    }

    this.send("$prefix&re$msg")

}

fun Player.sendError(msg: String) {
    log(msg, DebugLevel.ERROR)
}

fun Player.sendWarn(msg: String) {
    log(msg, DebugLevel.WARN)
}

fun Player.sendInfo(msg: String) {
    log(msg, DebugLevel.INFO)
}

fun Player.sendDebug(msg: String) {
    log(msg, DebugLevel.DEBUG)
}

fun Player.sendTrace(msg: String) {
    log(msg, DebugLevel.TRACE)
}
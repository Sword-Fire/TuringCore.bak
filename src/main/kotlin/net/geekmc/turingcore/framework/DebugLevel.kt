package net.geekmc.turingcore.framework

import net.minestom.server.command.CommandSender
import net.minestom.server.tag.Tag

private val debugTag = Tag.String("Turing.DebugLevel")

var CommandSender.debugLevel: DebugLevel

    get() = DebugLevel.valueOfOrNull(this.getTag(debugTag)?:"") ?: DebugLevel.INFO
    set(value) = this.setTag(debugTag, value.toString())


enum class DebugLevel {
    OFF, ERROR, WARN, INFO, DEBUG, TRACE;

    companion object {
        fun valueOfOrNull(value: String): DebugLevel? {
            return values().find { it.name == value }
        }
    }

    fun toInt(): Int {
        return when (this) {
            OFF -> 1;
            ERROR -> 2
            WARN -> 3
            INFO -> 4
            DEBUG -> 5
            TRACE -> 6
        }
    }
}
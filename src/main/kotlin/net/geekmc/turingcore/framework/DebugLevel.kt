package net.geekmc.turingcore.framework

import net.geekmc.turingcore.extender.nullableEnumValueOf
import net.minestom.server.command.CommandSender
import net.minestom.server.tag.Tag

private val debugTag = Tag.String("Turing.DebugLevel")

var CommandSender.debugLevel: DebugLevel
    get() = nullableEnumValueOf<DebugLevel>(this.getTag(debugTag) ?: "") ?: DebugLevel.INFO
    set(value) = this.setTag(debugTag, value.toString())


enum class DebugLevel {
    OFF, ERROR, WARN, INFO, DEBUG, TRACE;

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

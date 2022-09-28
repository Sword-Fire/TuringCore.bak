package net.geekmc.turingcore.util

import net.geekmc.turingcore.TuringCore
import world.cepi.kstom.util.log

fun info(vararg message: Any) {
    message.forEach { TuringCore.INSTANCE.log.info(it.toString()) }
}

fun trace(vararg message: Any) {
    message.forEach { TuringCore.INSTANCE.logger.trace(it.toString()) }
}

fun debug(vararg message: Any) {
    message.forEach { TuringCore.INSTANCE.logger.debug(it.toString()) }
}

fun warn(vararg message: Any) {
    message.forEach { TuringCore.INSTANCE.logger.warn(it.toString()) }
}
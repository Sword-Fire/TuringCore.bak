package net.geekmc.turingcore.service

import net.geekmc.turingcore.util.GLOBAL_EVENT
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode

/**
 * 代表一种没有使用任何Minestom事件的服务。
 */
abstract class IndependentService : Service() {

    fun start() {
        if (isActive) {
            throw IllegalStateException("Service is already started!")
        }
        isActive = true
        onEnable()
    }

}
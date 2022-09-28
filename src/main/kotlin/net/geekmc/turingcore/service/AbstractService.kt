package net.geekmc.turingcore.service

abstract class AbstractService {

    var isActive = false
        private set

    fun active() {
        if (isActive) {
            return
        }
        isActive = true
        onEnable()
    }

    fun unActive() {
        if (!isActive) {
            return
        }
        isActive = false
        onDisable()
    }

    protected abstract fun onEnable()

    protected abstract fun onDisable()
}
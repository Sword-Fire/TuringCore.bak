package net.geekmc.turingcore.util.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import net.minestom.server.MinecraftServer
import net.minestom.server.ServerProcess
import net.minestom.server.timer.ExecutionType
import kotlin.coroutines.CoroutineContext

@Suppress("UnstableApiUsage", "SpellCheckingInspection")
val Dispatchers.minestomSync: CoroutineDispatcher get() = AsyncCoroutineDispatcher(MinecraftServer.process())

@Suppress("UnstableApiUsage")
internal class SyncCoroutineDispatcher(private val serverProcess: ServerProcess) : CoroutineDispatcher() {

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (!serverProcess.isAlive) {
            return
        }
        serverProcess.scheduler().scheduleNextProcess(block, ExecutionType.SYNC)
    }
}
package net.geekmc.turingcore.skin

import kotlinx.coroutines.*
import net.geekmc.turinglib.coroutine.MinestomAsync
import net.geekmc.turinglib.coroutine.MinestomSync
import net.minestom.server.entity.PlayerSkin
import net.minestom.server.event.player.PlayerSpawnEvent
import world.cepi.kstom.Manager
import world.cepi.kstom.event.listen

object OfflineSkinService {

    fun enable() {

        Manager.globalEvent.listen<PlayerSpawnEvent> {
            handler {
                if (!isFirstSpawn) return@handler

                val scope = CoroutineScope(Dispatchers.MinestomSync)
                scope.launch {

                    val skin = withContext(Dispatchers.IO) { PlayerSkin.fromUsername(player.username) }
                    player.skin = skin

                }

            }
        }
    }

}
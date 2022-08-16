package net.geekmc.turingcore.skin

import kotlinx.coroutines.*
import net.geekmc.turingcore.TuringCore
import net.geekmc.turinglib.coroutine.MinestomSync
import net.geekmc.turinglib.taml.Taml
import net.geekmc.turinglib.util.GlobalEvent
import net.minestom.server.entity.PlayerSkin
import net.minestom.server.event.player.PlayerSpawnEvent
import world.cepi.kstom.event.listen

object OfflineSkinService {

    lateinit var skinData: Taml
        private set

    fun init() {
        skinData = Taml(TuringCore.INSTANCE.getResource("data/SkinData.yml")!!)

        GlobalEvent.listen<PlayerSpawnEvent> {
            handler {
                if (!isFirstSpawn) return@handler

                // Look up by name,not uuid

//                var skin: PlayerSkin? = skinData["name_map.${player.name}"]
//                if (skin == null) {
//
//                }

                val scope = CoroutineScope(Dispatchers.MinestomSync)
                scope.launch {

                    val skin = withContext(Dispatchers.IO) { PlayerSkin.fromUsername(player.username) }
                    player.skin = skin

                }

            }
        }
    }

    fun close() {

    }

}
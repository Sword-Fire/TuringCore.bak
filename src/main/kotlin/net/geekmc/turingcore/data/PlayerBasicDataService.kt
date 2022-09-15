package net.geekmc.turingcore.data

import net.geekmc.turingcore.data.json.SerializableData
import net.geekmc.turingcore.extender.GlobalEvent
import net.geekmc.turingcore.extender.Pos
import net.geekmc.turingcore.permission.isOp
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.permission.Permission
import world.cepi.kstom.event.listenOnly
import java.nio.file.Path

object PlayerBasicDataService {

    lateinit var service: PlayerDataService

    val Player.data: SerializableData
        get() = service.getPlayerData(this)

    fun start() {

        service = PlayerDataService.register("turingcore.basic") {
            Path.of("playerdata/${it.username}.json")
        }

        // TODO: Potion effects,etc

        service.onLogin {

            // health
            player.data.get<Float>("health")?.also { player.health = it }

            // op
            if(player.data.get<Boolean>("op") == null) {
                player.data["op"] = false
            }

            // gamemode
            player.gameMode = player.data["gamemode"] ?: GameMode.ADVENTURE

            // permissions
            val perms = player.data.get<ArrayList<Permission>>("permissions", arrayListOf())
            for (perm in perms) {
                player.addPermission(perm)
            }

            // inventory items
            for (i in 0 until player.inventory.size) {
                player.inventory.setItemStack(i, player.data["inventory.$i"] ?: continue)
            }

        }

        service.onDisconnect {

            // health
            player.data["health"] = player.health

            // gamemode
            player.data["gamemode"] = player.gameMode

            // position
            player.data["position"] = player.position

            // permissions
            val perms = arrayListOf<Permission>().apply { addAll(player.allPermissions) }
            player.data["permissions"] = perms

            // inventory items
            for (i in 0 until player.inventory.size) {
                val item = player.inventory.getItemStack(i)
                if (item.isAir) continue
                player.data["inventory.$i"] = item
            }

        }

        // position
        GlobalEvent.listenOnly<PlayerSpawnEvent> {
            if (isFirstSpawn) {
                player.teleport(player.data["position"] ?: Pos(0, 45, 0))
            }
        }

    }

}
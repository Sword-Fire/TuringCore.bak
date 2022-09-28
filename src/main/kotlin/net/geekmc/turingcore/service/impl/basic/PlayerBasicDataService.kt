package net.geekmc.turingcore.service.impl.basic

import net.geekmc.turingcore.data.json.JsonData
import net.geekmc.turingcore.service.AbstractService
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.permission.Permission
import java.nio.file.Path

object PlayerBasicDataService : AbstractService() {

    private lateinit var service: PlayerDataService

    @Suppress("SpellCheckingInspection")
    override fun onEnable() {
        // 注册服务。
        service = PlayerDataService.register("turingcore.basic") {
            Path.of("PlayerData/${it.username}.json")
        }
        registerLoginEvent()
        registerDisconnectEvent()
    }

    override fun onDisable() {}

    fun Player.getData(): JsonData {
        return service.getPlayerData(this)
    }

    // 在玩家进入事件触发时从关联 Json 文件获取信息，执行相关操作。
    private fun registerLoginEvent() {
        service.onLogin {
            // 血量。
            player.getData().get<Float>("health")?.apply { player.health = this }
            // 坐标。
            player.getData().get<Pos>("position")?.also { player.teleport(it) }
            // 管理员判断，这里的代码我没看懂。
            if (player.getData().get<Boolean>("isOp") == null) {
                player.getData()["isOp"] = false
            }
            // 游戏模式。
            player.gameMode = player.getData()["gameMode"] ?: GameMode.ADVENTURE
            // 权限。
            player.getData().get<ArrayList<Permission>>("permissions", arrayListOf()).forEach {
                player.addPermission(it)
            }
            // 背包物品。
            for (slot in 0 until player.inventory.size) {
                player.inventory.setItemStack(slot, player.getData()["inventory.$slot"] ?: continue)
            }
        }
    }

    // 在玩家离线事件触发时将相关信息写入关联 Json 文件。
    private fun registerDisconnectEvent() {
        service.onDisconnect {
            player.getData()["health"] = player.health
            player.getData()["position"] = player.position
            player.getData()["gameMode"] = player.gameMode
            player.getData()["permissions"] = arrayListOf<Permission>().apply {
                addAll(player.allPermissions)
            }
            for (i in 0 until player.inventory.size) {
                val item = player.inventory.getItemStack(i)
                // 即使材质为空气也应该写入至文件，不然有刷物品的风险。
                player.getData()["inventory.$i"] = item
            }
        }
    }
}
package net.geekmc.turingcore.service.impl.player.impl

import net.geekmc.turingcore.service.AbstractService
import net.geekmc.turingcore.service.impl.player.PlayerDataService
import net.geekmc.turingcore.service.impl.player.data
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.permission.Permission
import java.nio.file.Path

object PlayerBasicDataService : AbstractService() {

    lateinit var service: PlayerDataService
        private set

    @Suppress("SpellCheckingInspection")
    override fun onEnable() {
        // 注册服务。
        service = PlayerDataService.register("turingcore.basic") {
            Path.of("PlayerData/${it.username}.json")
        }
        // TODO: 补充药水效果等代码。
        // 在玩家进入事件触发时从关联 Json 文件获取信息，执行相关操作。
        service.onLogin {
            read(player)
        }
        // 在玩家离线事件触发时将相关信息写入关联 Json 文件。
        service.onDisconnect {
            save(player)
        }
    }

    override fun onDisable() {}

    fun read(player: Player) {
        // 血量。
        player.data.get<Float>("health")?.let { player.health = it }
        // 坐标。
        player.data.get<Pos>("position")?.let { player.teleport(it) }
        // 管理员判断，这里的代码我没看懂。
        if (player.data.get<Boolean>("isOp") == null) {
            player.data["isOp"] = false
        }
        // 游戏模式。
        player.gameMode = player.data["gameMode"] ?: GameMode.ADVENTURE
        // 权限。
        player.data.get<ArrayList<Permission>>("permissions", arrayListOf()).forEach {
            player.addPermission(it)
        }
        // 背包物品。
        for (slot in 0 until player.inventory.size) {
            player.inventory.setItemStack(slot, player.data["inventory.$slot"] ?: continue)
        }
    }

    fun save(player: Player) {
        player.data["health"] = player.health
        player.data["position"] = player.position
        player.data["gameMode"] = player.gameMode
        player.data["permissions"] = arrayListOf<Permission>().apply {
            addAll(player.allPermissions)
        }
        for (i in 0 until player.inventory.size) {
            val item = player.inventory.getItemStack(i)
            // 即使材质为空气也应该写入至文件，不然有刷物品的风险。
            player.data["inventory.$i"] = item
        }
    }
}
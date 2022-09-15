package net.geekmc.turingcore.data

import net.geekmc.turingcore.data.json.JsonData
import net.geekmc.turingcore.extender.GlobalEvent
import net.minestom.server.entity.Player
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerLoginEvent
import java.nio.file.Path

// 一个PlayerData对应着一种数据，例如所有玩家的职业插件数据。
/**
    [PlayerDataService] 提供获取/保存一种数据的服务，例如所有玩家的职业插件数据。每个玩家的数据与一个文件相关，你需要传入[pathProducer]来提供文件路径。

    你可以通过 [PlayerDataService.register] 注册一个 [PlayerDataService]，并通过 [PlayerDataService.get] 获取一个 [PlayerData]。
 */
class PlayerDataService private constructor(val id: String, val pathProducer: (Player) -> Path) {

    // player username -> data
    private val map = mutableMapOf<String, JsonData>()

    private val loginListener = EventListener.of(PlayerLoginEvent::class.java) {
        it.run {
            if (player.isPlayerDataLoaded()) {
                throw IllegalStateException("In PlayerDataService $id ,data of player $player is already loaded!")
            }
            player.loadPlayerData()
        }
        for (block in runWhenLogin) {
            it.block()
        }
    }

    private val disconnectListener = EventListener.of(PlayerDisconnectEvent::class.java) {
        for (block in runWhenDisconnect) {
            it.block()
        }
        it.run {
            player.savePlayerData()
            player.unloadPlayerData()
        }
    }

    private val runWhenLogin = mutableListOf<PlayerLoginEvent.() -> Unit>()
    private val runWhenDisconnect = mutableListOf<PlayerDisconnectEvent.() -> Unit>()

    init {

        GlobalEvent.addListener(loginListener)
        GlobalEvent.addListener(disconnectListener)

    }

    fun onLogin(block: PlayerLoginEvent.() -> Unit) {
        runWhenLogin.add(block)
    }

    fun onDisconnect(block: PlayerDisconnectEvent.() -> Unit) {
        runWhenDisconnect.add(block)
    }

    fun getPlayerData(p: Player): JsonData {
        if (!p.isPlayerDataLoaded()) p.loadPlayerData()
        return map[p.username]!!
    }

    private fun Player.isPlayerDataLoaded(): Boolean {
        return map.containsKey(this.username)
    }

    // 玩家进服或是读取数据时，都会检查是否加载过数据，没有则加载。
    // 因为读取任意数据可能发生在监听器监听到玩家进服之前。
    private fun Player.loadPlayerData() {

        if (map.containsKey(username)) throw IllegalStateException("Player Data $id of player $username is already loaded!")
        map[username] = JsonData.load(pathProducer(this))

    }

    private fun Player.savePlayerData() {
        map[username]!!.save()
    }

    private fun Player.unloadPlayerData() = map.remove(this.username)

    fun close() {
        GlobalEvent.removeListener(loginListener)
        GlobalEvent.removeListener(disconnectListener)
    }

    companion object {
        fun register(id: String, pathProducer: (Player) -> Path) = PlayerDataService(id, pathProducer)
    }

}

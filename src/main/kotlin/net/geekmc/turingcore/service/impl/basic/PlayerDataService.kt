package net.geekmc.turingcore.service.impl.basic

import net.geekmc.turingcore.data.json.JsonData
import net.geekmc.turingcore.service.AbstractService
import net.geekmc.turingcore.util.GLOBAL_EVENT
import net.minestom.server.entity.Player
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerLoginEvent
import java.nio.file.Path

class PlayerDataService private constructor(private val id: String, val producer: (Player) -> Path) : AbstractService() {

    companion object {

        fun register(id: String, producer: (Player) -> Path) = PlayerDataService(id, producer).apply {
            active()
        }
    }

    private val playerNameToDataMap = mutableMapOf<String, JsonData>()

    private val loginFunctions = mutableListOf<PlayerLoginEvent.() -> Unit>()
    private val disconnectFunctions = mutableListOf<PlayerDisconnectEvent.() -> Unit>()

    private val loginListener = EventListener.of(PlayerLoginEvent::class.java) {
        it.run {
            if (player.isDataLoaded()) {
                throw IllegalStateException("(PlayerDataService) Player data cannot be loaded more than once. (id: $id)")
            }
            player.loadData()
        }
        for (block in loginFunctions) {
            it.block()
        }
    }

    private val disconnectListener = EventListener.of(PlayerDisconnectEvent::class.java) {
        for (block in disconnectFunctions) {
            it.block()
        }
        it.run {
            player.saveData()
            player.unloadData()
        }
    }

    override fun onEnable() {
        GLOBAL_EVENT.addListener(loginListener)
        GLOBAL_EVENT.addListener(disconnectListener)
    }

    override fun onDisable() {
        GLOBAL_EVENT.removeListener(loginListener)
        GLOBAL_EVENT.removeListener(disconnectListener)
    }

    fun getPlayerData(player: Player): JsonData {
        if (!player.isDataLoaded()) {
            player.loadData()
        }
        return playerNameToDataMap[player.username]!!
    }

    fun onLogin(block: PlayerLoginEvent.() -> Unit) {
        loginFunctions += block
    }

    fun onDisconnect(block: PlayerDisconnectEvent.() -> Unit) {
        disconnectFunctions += block
    }

    private fun Player.isDataLoaded(): Boolean {
        return playerNameToDataMap.containsKey(username)
    }

    private fun Player.loadData() {
        if (playerNameToDataMap.containsKey(username)) {
            throw IllegalStateException("(PlayerDataService) Player data cannot be loaded more than once. (id: $id)")
        }
        playerNameToDataMap[username] = JsonData.load(producer(this))
    }

    private fun Player.unloadData() {
        playerNameToDataMap.remove(username)
    }

    private fun Player.saveData() {
        playerNameToDataMap[username]!!.save()
    }
}
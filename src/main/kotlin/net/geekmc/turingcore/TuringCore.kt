package net.geekmc.turingcore

import net.geekmc.turingcore.block.GrassBlockHandler
import net.geekmc.turingcore.command.basic.CommandGamemode
import net.geekmc.turingcore.command.basic.CommandKill
import net.geekmc.turingcore.command.basic.CommandTeleport
import net.geekmc.turingcore.command.debug.CommandInfo
import net.geekmc.turingcore.command.management.CommandOp
import net.geekmc.turingcore.command.management.CommandPermission
import net.geekmc.turingcore.command.management.CommandSave
import net.geekmc.turingcore.command.management.CommandStop
import net.geekmc.turingcore.framework.TuringFrameWork
import net.geekmc.turingcore.service.impl.basic.PlayerBasicDataService
import net.geekmc.turingcore.service.impl.instance.InstanceService
import net.geekmc.turingcore.service.impl.motd.MotdService
import net.geekmc.turingcore.service.impl.skin.SkinService
import net.geekmc.turingcore.util.color.ColorUtil
import net.geekmc.turingcore.util.color.message
import net.geekmc.turingcore.util.color.toComponent
import net.geekmc.turingcore.util.globalEvent
import net.geekmc.turingcore.util.info
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.extensions.Extension
import net.minestom.server.utils.callback.CommandCallback
import world.cepi.kstom.Manager
import world.cepi.kstom.command.kommand.Kommand
import world.cepi.kstom.event.listenOnly
import world.cepi.kstom.util.register
import kotlin.time.ExperimentalTime

class TuringCore : Extension() {

    companion object {

        lateinit var INSTANCE: TuringCore
            private set
    }

    override fun preInitialize() {
        super.preInitialize()
        INSTANCE = this
    }

    @Suppress("SpellCheckingInspection")
    override fun initialize() {
        info("TuringCore initializing...")
        // ColorUtil 在这里的优先级最高。
        ColorUtil.init()
        // 注册框架。
        registerFrameWork()
        // 皮肤服务。（基于玩家名）
        SkinService.active()
        // Motd 服务。
        MotdService.active()
        // 玩家基础信息服务。
        PlayerBasicDataService.active()
        // 世界服务。
        InstanceService.apply {
            active()
            createInstanceContainer(MAIN_INSTANCE_ID)
            val world = getInstance(MAIN_INSTANCE_ID)
            globalEvent.listenOnly<PlayerLoginEvent> {
                setSpawningInstance(world)
                player.respawnPoint = Pos(0.0, 40.0, 0.0)
                player.sendMessage("Welcome to TuringServer, ${player.username}!")
            }
            globalEvent.listenOnly<PlayerLoginEvent> {
                setSpawningInstance(world)
                player.respawnPoint = Pos(0.0, 40.0, 0.0)
                player.sendMessage("Welcome to server, ${player.username} !")
            }
        }
        // 注册指令。
        registerCommands()
        // 注册方块。
        registerBlockHandlers()
        // 临时监听器。
        globalEvent.listenOnly<PlayerChatEvent> {
            setChatFormat {

                "${player.displayName ?: player.username}: $message".toComponent()
            }
        }
        info("TuringCore initialized.")
    }

    override fun terminate() {}

    private fun registerFrameWork() {
        val registry = TuringFrameWork.registerExtension("net.geekmc.turingcore", this)
        registry.consolePrefix = "[TuringCore] "
        registry.playerPrefix = "&f[&gTuringCore&f] ".toComponent()
    }

    private fun registerBlockHandlers() {
        GrassBlockHandler.register("minecraft:grass_block")
    }

    @OptIn(ExperimentalTime::class)
    private fun registerCommands() {
        Manager.command.unknownCommandCallback = CommandCallback { sender, command ->
            sender.message("&r未知指令: /$command")
        }
        arrayListOf<Kommand>().apply {
            this += CommandGamemode
            this += CommandKill
            this += CommandTeleport
            this += CommandInfo
            this += CommandOp
            this += CommandPermission
            this += CommandSave
            this += CommandStop
        }.forEach {
            it.register()
        }
    }
}
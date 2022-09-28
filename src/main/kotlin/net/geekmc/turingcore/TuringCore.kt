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
import net.geekmc.turingcore.service.impl.player.impl.PlayerBasicDataService
import net.geekmc.turingcore.service.impl.instance.InstanceService
import net.geekmc.turingcore.service.impl.motd.MotdService
import net.geekmc.turingcore.service.impl.skin.SkinService
import net.geekmc.turingcore.util.color.ColorUtil
import net.geekmc.turingcore.util.color.message
import net.geekmc.turingcore.util.color.toComponent
import net.geekmc.turingcore.util.GLOBAL_EVENT
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
            GLOBAL_EVENT.listenOnly<PlayerLoginEvent> {
                setSpawningInstance(world)
                player.respawnPoint = Pos(0.0, 40.0, 0.0)
                player.sendMessage("Welcome to TuringServer, ${player.username}!")
            }
            GLOBAL_EVENT.listenOnly<PlayerLoginEvent> {
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
        GLOBAL_EVENT.listenOnly<PlayerChatEvent> {
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


    /**
     * 注册 BlockHandler 的意义是在读取地图时，
     * 会用方块的 NID 作为键值查找是否有对应的 BlockHandlerSupplier 提供 BlockHandler。
     * 并且保存世界时，只有 BlockHandler 里注册过的 Tag 会被存到地图里。（真实性存疑）
     */
    private fun registerBlockHandlers() {
        // TODO: 写一个用自己 NID 注册的扩展方法。
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
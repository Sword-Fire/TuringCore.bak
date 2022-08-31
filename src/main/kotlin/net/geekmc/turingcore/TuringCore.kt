package net.geekmc.turingcore

import net.geekmc.turingcore.color.ColorUtil
import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.color.toComponent
import net.geekmc.turingcore.command.*
import net.geekmc.turingcore.extender.GlobalEvent
import net.geekmc.turingcore.extender.saveResource
import net.geekmc.turingcore.framework.Logger
import net.geekmc.turingcore.framework.TuringFramework
import net.geekmc.turingcore.instance.InstanceService
import net.geekmc.turingcore.motd.MotdService
import net.geekmc.turingcore.skin.SkinService
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.extensions.Extension
import net.minestom.server.utils.callback.CommandCallback
import world.cepi.kstom.Manager
import world.cepi.kstom.command.register
import world.cepi.kstom.event.listenOnly

class TuringCore : Extension() {

    companion object {
        lateinit var INSTANCE: TuringCore
            private set
    }

    override fun preInitialize() {
        super.preInitialize()
        INSTANCE = this
    }

    override fun initialize() {

        logger.info("TuringCore initialized.")

        // Init logger for all extensions
        TuringFramework.init()
        val registry = TuringFramework.registerExtension("net.geekmc.turingcore", this)
        registry.consolePrefix = "[TuringCore]"
        registry.playerPrefix = "&f[&gTuringCore&f]".toComponent()

        // enable color util, very high priority
        saveResource("CustomColors.yml")
        ColorUtil.init()

        // enable offline skin
        SkinService.init()

        // enable Motd
        MotdService.init()

        // set player's spawn world
        InstanceService.initialize()
        InstanceService.createInstanceContainer(InstanceService.MAIN_INSTANCE)
        val world = InstanceService.getInstance(InstanceService.MAIN_INSTANCE)

        GlobalEvent.listenOnly<PlayerLoginEvent> {
            setSpawningInstance(world)
            player.respawnPoint = Pos(0.0, 40.0, 0.0)
            player.sendMessage("Welcome to server, ${player.username} !")
        }

        // register commands

        Manager.command.unknownCommandCallback = CommandCallback { sender, command ->
            sender.send("&r未知命令: $command")
        }

        SayCommand.register()
        StopCommand.register()
        TestCommand.register()
        SaveCommand.register()
        GamemodeCommand.register()
        KillCommand.register()
        PermissionCommand.register()
        InfoCommand.register()

        // register block handlers
        // register Handler的意义是，读取地图时，会用方块的原版NID做key查找是否有对应的BlockHandlerSupplier提供BlockHandler
        // 并且，保存世界时，只有BlockHandler里注册过的Tag会被存到地图（等等，这条好像是错的）
//        Block.values().forEach {
//            if (it.name().endsWith("sign")) {
//                println("registered handler ${it.name()}")
//                SignHandler.register(it.name())
//            }
//        }
//        SignHandler.register("minecraft:sign")

        // set chat format
        GlobalEvent.listenOnly<PlayerChatEvent> {
            setChatFormat {
                "${player.displayName ?: player.username}: $message".toComponent()
            }
        }

    }

    override fun terminate() {
        SkinService.close()
        logger.info("TuringCore terminated.")
    }

}
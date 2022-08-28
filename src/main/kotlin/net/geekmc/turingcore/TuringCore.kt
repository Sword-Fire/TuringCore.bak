package net.geekmc.turingcore

import net.geekmc.turingcore.blockhandler.SignHandler
import net.geekmc.turingcore.color.ColorUtil
import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.color.toComponent
import net.geekmc.turingcore.command.*
import net.geekmc.turingcore.extender.GlobalEvent
import net.geekmc.turingcore.extender.saveResource
import net.geekmc.turingcore.instance.InstanceService
import net.geekmc.turingcore.motd.MotdService
import net.geekmc.turingcore.skin.SkinService
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.extensions.Extension
import net.minestom.server.instance.block.Block
import net.minestom.server.utils.callback.CommandCallback
import world.cepi.kstom.Manager
import world.cepi.kstom.command.register
import world.cepi.kstom.event.listenOnly
import world.cepi.kstom.util.register
import java.util.concurrent.ThreadLocalRandom


//TODO 依赖反转
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

        // enable color util
        // high priority
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
        Block.values().forEach {
            if (it.name().endsWith("sign")) {
                SignHandler.register(it.name())
            }
        }
        SignHandler.register("minecraft:sign")

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
package net.geekmc.turingcore

import net.geekmc.turingcore.blockhandler.SignHandler
import net.geekmc.turingcore.command.*
import net.geekmc.turingcore.motd.MotdService
import net.geekmc.turingcore.skin.OfflineSkinService
import net.geekmc.turinglib.TuringLib
import net.geekmc.turinglib.color.toComponent
import net.geekmc.turinglib.instance.InstanceService
import net.geekmc.turinglib.util.GlobalEvent
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.extensions.Extension
import net.minestom.server.instance.block.Block
import world.cepi.kstom.command.register
import world.cepi.kstom.event.listenOnly
import world.cepi.kstom.util.register

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

        // enable Motd
        MotdService.init()

        // enable offline skin
        OfflineSkinService.init()

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
        SayCommand.register()
        StopCommand.register()
        TestCommand.register()
        SaveCommand.register()
        GamemodeCommand.register()
        KillCommand.register()

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
        logger.info("TuringCore terminated.")
    }

}
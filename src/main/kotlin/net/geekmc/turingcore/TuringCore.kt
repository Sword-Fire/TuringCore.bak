package net.geekmc.turingcore

import kotlinx.coroutines.GlobalScope
import net.geekmc.turingcore.blockhandler.SignHandler
import net.geekmc.turingcore.command.*
import net.geekmc.turingcore.motd.MotdService
import net.geekmc.turinglib.instance.InstanceService
import net.geekmc.turinglib.util.GlobalEvent
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.extensions.Extension
import net.minestom.server.instance.block.Block
import world.cepi.kstom.Manager
import world.cepi.kstom.command.register
import world.cepi.kstom.event.listenOnly
import world.cepi.kstom.util.register

class TuringCore : Extension() {

    override fun initialize() {

        logger.info("TuringCore initialized.")

        // enable Motd
        MotdService.enableMotd()

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

    }

    override fun terminate() {
        logger.info("TuringCore terminated.")
    }

}
package net.geekmc.turingcore

import net.geekmc.turing.command.SaveCommand
import net.geekmc.turing.command.SayCommand
import net.geekmc.turing.instance.InstanceService
import net.geekmc.turingcore.command.GamemodeCommand
import net.geekmc.turingcore.command.StopCommand
import net.geekmc.turingcore.command.TestCommand
import net.geekmc.turingcore.motd.MotdService
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.extensions.Extension
import world.cepi.kstom.Manager

class TuringCore : Extension() {

    override fun initialize() {

        logger.info("TuringCore initialized.")

        // enable Motd
        MotdService.enableMotd()
        MinecraftServer.setBrandName("GeekMC")

        // set player's spawn world
        InstanceService.initialize()
        InstanceService.createInstanceContainer(InstanceService.MAIN_INSTANCE)
        val world = InstanceService.getInstance(InstanceService.MAIN_INSTANCE)
        Manager.globalEvent.addListener(PlayerLoginEvent::class.java) {
            val p = it.player

            it.setSpawningInstance(world)
            p.respawnPoint = Pos(0.0, 40.0, 0.0)
            p.sendMessage("Welcome to server, " + p.username + " !")
        }

        // register commands
        val manager = Manager.command
        manager.register(SayCommand)
        manager.register(StopCommand)
        manager.register(TestCommand)
        manager.register(SaveCommand)
        manager.register(GamemodeCommand)

    }

    override fun terminate() {
        logger.info("TuringCore terminated.")
    }

}
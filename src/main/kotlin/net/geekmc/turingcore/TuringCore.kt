package net.geekmc.turingcore

import net.geekmc.turingcore.blockhandler.SignHandler
import net.geekmc.turingcore.color.ColorUtil
import net.geekmc.turingcore.color.toComponent
import net.geekmc.turingcore.command.*
import net.geekmc.turingcore.instance.InstanceService
import net.geekmc.turingcore.motd.MotdService
import net.geekmc.turingcore.skin.PlayerSkinBean
import net.geekmc.turingcore.skin.SkinService
import net.geekmc.turingcore.taml.Taml
import net.geekmc.turingcore.util.GlobalEvent
import net.geekmc.turingcore.util.saveResource
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.PlayerSkin
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.extensions.Extension
import net.minestom.server.instance.block.Block
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor
import world.cepi.kstom.command.register
import world.cepi.kstom.event.listenOnly
import world.cepi.kstom.util.register
import java.io.FileInputStream
import java.io.FileWriter
import kotlin.io.path.Path


//TODO 改名为TuringCoreExtension
//然后val TuringCore = TuringCoreExtension.INSTANCE
class TuringCore : Extension() {

    data class Test(var x: Int = 0, var y: Int = 0)

    companion object {
        lateinit var INSTANCE: TuringCore
            private set
    }

    override fun preInitialize() {
        super.preInitialize()
        INSTANCE = this
    }

    override fun initialize() {

        val taml = Taml("D:/test.yml")
        taml.appendList("hello.abc",10)
        taml.save()

        logger.info("TuringCore initialized.")

        // enable Motd
        MotdService.init()

        // enable offline skin
        SkinService.init()

        // enable color util
        saveResource("CustomColors.yml")
        ColorUtil.init()

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


//        val skin = PlayerSkin.fromUsername("Anzide")
//        val taml = Taml(Path("D:/test.yml"))
//        taml["b"] = Test(1, 2)
//
//        taml.save()

//        set()
//
//        get()

    }

//    fun get() {
//        val yaml = Yaml(CustomClassLoaderConstructor(TuringCore::class.java.classLoader))
//        val map: MutableMap<Any?, Any?> = yaml.load(FileInputStream("D:/test.yml"))
//        val t = map["t"] as Test
//        println("${t.x} ${t.y}")
//    }
//
//    fun set() {
//        val yaml = Yaml(CustomClassLoaderConstructor(TuringCore::class.java.classLoader))
//        val map: MutableMap<Any?, Any?> = yaml.load(FileInputStream("D:/test.yml"))
//        map["t"] = Test(1, 2)
//        yaml.dump(map, FileWriter("D:/test.yml"))
//    }

    override fun terminate() {
        SkinService.close()
        logger.info("TuringCore terminated.")
    }

}
package net.geekmc.turingcore

import net.geekmc.turingcore.blockhandler.GrassBlockHandler
import net.geekmc.turingcore.color.ColorUtil
import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.color.toComponent
import net.geekmc.turingcore.command.*
import net.geekmc.turingcore.data.PlayerBasicDataService
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
import world.cepi.kstom.util.register

class TuringCore : Extension() {


    override fun preInitialize() {
        super.preInitialize()
        INSTANCE = this

//        val item = item<WrittenBookMeta.Builder, WrittenBookMeta>(material = Material.WRITTEN_BOOK, amount = 5) {
//            // Use setters for managing your item properties
//            displayName = Component.text("Hey!")
//            damage = 5
//            unbreakable = true
//
//            lore {
//                +"Hello"
//                +"<red>Minestom!</red>"
//            }
//
//            title("My first book")
//            author("Notch")
//            pages(Component.text("This is the first page"))
//
//            // Store data with kotlinx.serialization & the tag API. (Works on all taggable objects)
//            this["complexData"] = ComplexClass(5, 4, 2, true, InterestingClass("hey", 'h'))
//            this["complexListData"] = CollectionClass(5, 9, 3, listOf(4, 3))
//        }.withAmount(7).and {
//            displayName(Component.text("Hay!"))
//        }

    }

    override fun initialize() {

        logger.info("TuringCore initializing...") //ColorUtil not usable here

        // Init ColorUtil for all extensions, the highest priority
        ColorUtil.init()

        // Init Turing Framework for all extensions
        TuringFramework.init()
        registerFramework()

        // Init offline skin
        SkinService.start()

        // Enable Motd
        MotdService.start()

        PlayerBasicDataService.start()

        // Set player's spawn world
        InstanceService.initialize()
        InstanceService.createInstanceContainer(InstanceService.MAIN_INSTANCE)
        val world = InstanceService.getInstance(InstanceService.MAIN_INSTANCE)

        GlobalEvent.listenOnly<PlayerLoginEvent> {
            setSpawningInstance(world)
            player.respawnPoint = Pos(0.0, 40.0, 0.0)
            player.sendMessage("Welcome to server, ${player.username} !")
        }

        registerCommands()
        registerBlockHandlers()

        // set chat format
        GlobalEvent.listenOnly<PlayerChatEvent> {
            setChatFormat {
                "${player.displayName ?: player.username}: $message".toComponent()
            }
        }

        Logger.info("TuringCore initialized.")

    }

    override fun terminate() {
        SkinService.close()
        Logger.info("TuringCore terminated.")
    }

    private fun registerBlockHandlers() {

        //TODO 写个拓展方法，用自己的NID注册
        GrassBlockHandler.register("minecraft:grass_block")
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
    }

    private fun registerFramework() {
        val registry = TuringFramework.registerExtension("net.geekmc.turingcore", this)
        registry.consolePrefix = "[TuringCore] "
        registry.playerPrefix = "&f[&gTuringCore&f] ".toComponent()
    }

    private fun registerCommands() {

        // Set unknown command fallback

        Manager.command.unknownCommandCallback = CommandCallback { sender, command ->
            sender.send("&r未知命令: /$command")
        }

        SayCommand.register()
        StopCommand.register()
        TestCommand.register()
        SaveCommand.register()
        GamemodeCommand.register()
        KillCommand.register()
        PermissionCommand.register()
        InfoCommand.register()
        DebugCommand.register()
        TeleportCommand.register()
        OpCommand.register()
    }

    companion object {
        lateinit var INSTANCE: TuringCore
            private set
    }

}
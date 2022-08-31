package net.geekmc.turingcore.command

import net.geekmc.turingcore.blockhandler.SignHandler
import net.geekmc.turingcore.color.toComponent
import net.geekmc.turingcore.framework.DebugLevel
import net.geekmc.turingcore.framework.Logger
import net.geekmc.turingcore.framework.debugLevel
import net.minestom.server.command.builder.arguments.ArgumentLiteral
import net.minestom.server.instance.block.Block
import net.minestom.server.tag.Tag
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.parser.SNBTParser
import world.cepi.kstom.Manager
import world.cepi.kstom.command.kommand.Kommand
import java.io.StringReader

object TestCommand : Kommand({

    val arg1 = ArgumentLiteral("1")
    val arg2 = ArgumentLiteral("2")
    val arg3 = ArgumentLiteral("3")
    val arg4 = ArgumentLiteral("4")
    val arg5 = ArgumentLiteral("5")


    syntax(arg1) {

        val block = Block.OAK_SIGN
            .withTag(Tag.Byte("GlowingText"), 1)
            .withTag(Tag.String("Color"), "white")
            .withTag(Tag.Component("Text1"), "abc".toComponent())

        player.instance!!.setBlock(player.getLineOfSight(20)[0], block)

    }.onlyPlayers()

    syntax(arg2) {

        println("===")
        Logger.trace("trace message")
        Logger.debug("debug message")
        Logger.info("info message")
        Logger.warn("warn message")
        Logger.error("error message")
        println("===")
        Manager.command.consoleSender.debugLevel=DebugLevel.DEBUG
        println("===")
        Logger.trace("trace message")
        Logger.debug("debug message")
        Logger.info("info message")
        Logger.warn("warn message")
        Logger.error("error message")
        println("===")
        Manager.command.consoleSender.debugLevel=DebugLevel.WARN
        println("===")
        Logger.trace("trace message")
        Logger.debug("debug message")
        Logger.info("info message")
        Logger.warn("warn message")
        Logger.error("error message")
        println("===")
        Manager.command.consoleSender.debugLevel=DebugLevel.INFO

    }

    syntax(arg3) {



    }


}, "test", "t")
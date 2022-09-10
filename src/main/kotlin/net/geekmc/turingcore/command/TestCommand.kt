package net.geekmc.turingcore.command

import net.geekmc.turingcore.blockhandler.SignHandler
import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.color.toComponent
import net.geekmc.turingcore.framework.*
import net.minestom.server.command.builder.arguments.ArgumentLiteral
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.block.BlockHandler
import net.minestom.server.instance.block.BlockHandler.PlayerDestroy
import net.minestom.server.tag.Tag
import net.minestom.server.utils.NamespaceID
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.parser.SNBTParser
import world.cepi.kstom.Manager
import world.cepi.kstom.command.kommand.Kommand
import world.cepi.kstom.util.register
import java.io.StringReader

object BlockHandlerA : BlockHandler {
    override fun onDestroy(destroy: BlockHandler.Destroy) {
        if (destroy !is PlayerDestroy) return
        destroy.player.send("&gdestory A")
    }

    override fun getNamespaceId(): NamespaceID {
        return NamespaceID.from("turing:a")
    }
}

object BlockHandlerB : BlockHandler {
    override fun onDestroy(destroy: BlockHandler.Destroy) {
        if (destroy !is PlayerDestroy) return
        destroy.player.send("&gdestory B")
    }

    override fun getNamespaceId(): NamespaceID {
        return NamespaceID.from("turing:b")
    }
}

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

        player.sendTrace("trace")
        player.sendDebug("debug")
        player.sendInfo("info")
        player.sendWarn("warn")
        player.sendError("error")

    }

    syntax(arg3) {

        val block = Block.IRON_ORE
            .withHandler(BlockHandlerA)

        player.instance!!.setBlock(player.getLineOfSight(20)[0], block)

    }

    syntax(arg4) {

        val block = Block.IRON_ORE
            .withHandler(BlockHandlerB)

        player.instance!!.setBlock(player.getLineOfSight(20)[0], block)

    }


}, "test", "t")
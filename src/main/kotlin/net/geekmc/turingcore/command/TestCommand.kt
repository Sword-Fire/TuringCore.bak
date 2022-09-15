package net.geekmc.turingcore.command

import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.color.toComponent
import net.minestom.server.command.builder.arguments.ArgumentLiteral
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.block.BlockHandler
import net.minestom.server.instance.block.BlockHandler.PlayerDestroy
import net.minestom.server.item.ItemStack
import net.minestom.server.tag.Tag
import net.minestom.server.utils.NamespaceID
import world.cepi.kstom.command.kommand.Kommand

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



    }

    syntax(arg2) {



    }


    syntax(arg5) {

        val block = Block.OAK_SIGN
            .withTag(Tag.Byte("GlowingText"), 1)
            .withTag(Tag.String("Color"), "white")
            .withTag(Tag.Component("Text1"), "abc".toComponent())

        player.instance!!.setBlock(player.getLineOfSight(20)[0], block)

    }.onlyPlayers()

}, "test", "t")
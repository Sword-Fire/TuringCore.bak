package net.geekmc.turingcore.command

import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.extender.getLineOfSightEntity
import net.geekmc.turingcore.extender.opSyntax
import net.minestom.server.command.builder.arguments.ArgumentLiteral
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.command.kommand.Kommand

object InfoCommand : Kommand({

    val hand = ArgumentLiteral("hand").setDefaultValue("hand")
    val block by literal
    val entity by literal

    playerCallbackFailMessage = {
        it.send("&r只有玩家能使用这个命令。")
    }

    opSyntax(hand) {
        player.send("&y手中物品信息:")
        player.send("&rNbt: " + player.itemInMainHand.toItemNBT().toString())
    }.onlyPlayers()

    opSyntax(block) {
        player.send("&y指向方块信息:")
        val pos = player.getLineOfSight(20)[0]?:return@opSyntax
        val b = player.instance?.getBlock(pos) ?: return@opSyntax
        player.send("&gMaterial: " + b.registry().material())
        player.send("&gNamespaceId: " + b.registry().namespace())
        player.send("&gBlockEntity: " + b.registry().blockEntity())
        player.send("&rNbt: " + b.nbt().toString())

    }.onlyPlayers()

    opSyntax(entity) {
        player.send("&y指向生物信息:")
        player.send("&rNbt: " + player.getLineOfSightEntity(20.0))
    }.onlyPlayers()

}, "if")


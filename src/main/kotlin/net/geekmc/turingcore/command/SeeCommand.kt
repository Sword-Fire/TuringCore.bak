package net.geekmc.turingcore.command

import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.extender.*
import net.geekmc.turingcore.command.kommand.OP
import net.geekmc.turingcore.command.kommand.PLAYER
import net.geekmc.turingcore.command.kommand.format
import net.minestom.server.command.builder.arguments.ArgumentLiteral
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.command.kommand.Kommand

/**
 * 给出手中物品、指向方块或指向生物的详细信息。用于调试。
 */
object SeeCommand : Kommand({

    val hand = ArgumentLiteral("hand").setDefaultValue("hand")
    val block by literal
    val entity by literal

    playerCallbackFailMessage = {
        it.send("&r只有玩家能使用这个命令。")
    }

    // 第一个参数改为 setOf(...)
    // 或者直接手写双vararg 第一个参数有多个。
    format(OP, PLAYER, hand) {
        player.send("&y手中物品信息:")
        player.send("&rNbt: " + player.itemInMainHand.toItemNBT().toString())
    }

    format(OP, PLAYER, block) {
        player.send("&y指向方块信息:")
        val pos = player.getLineOfSight(20)[0] ?: return@format
        val b = player.instance?.getBlock(pos) ?: return@format
        player.send("&gMaterial: " + b.registry().material())
        player.send("&gNamespaceId: " + b.registry().namespace())
        player.send("&gBlockEntity: " + b.registry().blockEntity())
        player.send("&rNbt: " + b.nbt().toString())

    }

    format(OP, PLAYER, entity) {
        player.send("&y指向生物信息:")
        player.send("&rNbt: " + player.getLineOfSightEntity(20.0))
    }.onlyPlayers()

}, "see")


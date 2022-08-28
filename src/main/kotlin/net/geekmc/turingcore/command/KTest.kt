package net.geekmc.turingcore.command

import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import net.minestom.server.command.builder.arguments.number.ArgumentInteger
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.command.kommand.Kommand

object KTest : Kommand({

    val add by literal // shorthand for val add = ArgumentType.Literal("add")
    val remove by literal
    val set by literal
    val amount = ArgumentInteger("amount").min(0)
    val p = ArgumentEntity("player").onlyPlayers(true).singleEntity(true)

    syntax {
        sender.sendMessage("Usage: add|remove|set <amount>")
    }

    subcommand("sub") {
        onlyPlayers()

        val delete by literal

        syntax(delete, amount) {
            player.level += !amount
        }
    }

    syntax(p) {
        val p2=!p
    }

    syntax(add, amount) {
        player.level += !amount
    }.onlyPlayers()

    syntax(remove, amount) {
        player.level = (player.level - !amount).coerceAtLeast(0)
    }.onlyPlayers()

    syntax(set, amount) {
        player.level = !amount
    }.condition { player?.level == 5 } // not realistic but demonstrates custom conditions

}, "k")
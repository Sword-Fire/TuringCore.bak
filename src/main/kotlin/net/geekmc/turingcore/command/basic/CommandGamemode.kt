package net.geekmc.turingcore.command.basic

import net.geekmc.turingcore.command.args
import net.geekmc.turingcore.command.findPlayers
import net.geekmc.turingcore.command.opSyntax
import net.geekmc.turingcore.command.setGameMode
import net.geekmc.turingcore.util.color.message
import net.minestom.server.command.builder.arguments.ArgumentWord
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import world.cepi.kstom.command.kommand.Kommand

@Suppress("SpellCheckingInspection")
object CommandGamemode : Kommand({

    val targetArg = ArgumentEntity("target").onlyPlayers(true)

    // Allow integer 0-3, or the GameMode as string ignoring case, as the args
    val modeArgs = ArgumentWord("mode").from(*kotlin.run {
        mutableListOf<String>().apply {
            addAll(GameMode.values().map { it.toString() })
            addAll(GameMode.values().map { it.toString().lowercase() })
            addAll(listOf("0", "1", "2", "3"))
        }.toTypedArray()
    })

    opSyntax {
        sender.message("&r命令用法不正确: /${context.input}")
    }

    opSyntax(modeArgs) {
        if (sender !is Player) {
            sender.message("&r只有玩家能使用这个命令!")
            return@opSyntax
        }
        setGameMode(player, player, !modeArgs)
    }

    opSyntax(modeArgs, targetArg) {
        val players = (!targetArg).findPlayers(sender)
        if (players.isEmpty()) {
            sender.message("&r找不到玩家: ${args.getRaw(targetArg)}")
            return@opSyntax
        }
        players.forEach {
            setGameMode(sender, it, !modeArgs)
        }
    }
}, name = "gamemode", aliases = arrayOf("gm", "gmode"))
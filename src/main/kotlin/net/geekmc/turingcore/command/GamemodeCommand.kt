package net.geekmc.turingcore.command

import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.extender.args
import net.geekmc.turingcore.extender.findPlayers
import net.geekmc.turingcore.extender.foldToString
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandExecutor
import net.minestom.server.command.builder.arguments.ArgumentWord
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.utils.entity.EntityFinder
import world.cepi.kstom.command.kommand.Kommand

object GamemodeCommand : Kommand({

    val targetArg = ArgumentEntity("target").onlyPlayers(true)
    // Allow integer 0-3, or the GameMode as string ignoring case, as the args
    val modeArg = ArgumentWord("mode").from(*kotlin.run {
        val list = mutableListOf<String>()
        list.addAll(GameMode.values().map { it.toString() })
        list.addAll(GameMode.values().map { it.toString().lowercase() })
        list.addAll(listOf("0", "1", "2", "3"))
        list.toTypedArray()
    })

    fun setGameMode(sender: CommandSender, player: Player, mode: String) {

        player.gameMode = when (mode.uppercase()) {
            "0", "SURVIVAL" ->GameMode.SURVIVAL
            "1", "CREATIVE" -> GameMode.CREATIVE
            "2", "ADVENTURE" -> GameMode.ADVENTURE
            "3", "SPECTATOR" -> GameMode.SPECTATOR
            else -> throw IllegalArgumentException("&r未知的游戏模式 $mode")
        }
        sender.send("&g已将玩家 &y${player.username} &g的游戏模式设置为 &y${player.gameMode}")
    }

    syntax {
        sender.send("&r命令用法不正确: /${context.input}")
    }

    syntax(modeArg) {

        if (sender !is Player) {
            sender.send("&r只有玩家能使用这个命令!")
            return@syntax
        }
        setGameMode(player, player, !modeArg)
    }

    syntax(modeArg, targetArg) {

        val players = (!targetArg).findPlayers(sender)

        if (players.isEmpty()) {
            sender.send("&r找不到玩家: ${args.getRaw(targetArg)}")
            return@syntax
        }
        players.forEach {
            setGameMode(sender, it, !modeArg)
        }

    }

}, "gamemode", "gm")

package net.geekmc.turingcore.command

import net.geekmc.turingcore.util.color.message
import net.geekmc.turingcore.util.isOp
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.utils.entity.EntityFinder
import org.jetbrains.annotations.Contract
import world.cepi.kstom.command.kommand.Kommand

val Kommand.SyntaxContext.args
    get() = this.context

@Contract(pure = true)
fun Kommand.opSyntax(
    vararg arguments: Argument<*> = arrayOf(),
    executor: Kommand.SyntaxContext.() -> Unit
) = syntax(*arguments, executor = {
    if (!sender.isOp()) {
        sender.message("&r只有管理员能使用这个命令!")
        return@syntax
    }
    executor()
})

fun setGameMode(sender: CommandSender, player: Player, mode: String) {
    player.gameMode = when (mode.uppercase()) {
        "0", "SURVIVAL" -> GameMode.SURVIVAL
        "1", "CREATIVE" -> GameMode.CREATIVE
        "2", "ADVENTURE" -> GameMode.ADVENTURE
        "3", "SPECTATOR" -> GameMode.SPECTATOR
        else -> throw IllegalArgumentException("&r未知的游戏模式 $mode")
    }
    sender.message("&g已将玩家 &y${player.username} &g的游戏模式设置为 &y${player.gameMode.toString().lowercase()}")
}

fun EntityFinder.findPlayers(sender: CommandSender): List<Player> {
    return find(sender).filterIsInstance<Player>()
}

fun ArgumentEntity.setDefaultValueToSelf(): ArgumentEntity {
    setDefaultValue(EntityFinder().setTargetSelector(EntityFinder.TargetSelector.SELF))
    return this
}
package net.geekmc.turingcore.command

import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.instance.InstanceService
import net.minestom.server.command.builder.arguments.ArgumentLiteral
import net.minestom.server.item.Material
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.command.kommand.Kommand

//TODO : 查看指针实体；指针方块；手上物品
object InfoCommand : Kommand({

    val hand = ArgumentLiteral("hand").setDefaultValue("hand")
    val block by literal
    val entity by literal

    playerCallbackFailMessage = {
        it.send("&r只有玩家能使用这个命令。")
    }

    syntax(hand) {
        player.send(player.itemInMainHand.toItemNBT().toString())
    }.onlyPlayers()

    syntax(block){
//        player.send(player.getTargetBlockPosition(20))
        for (pos in player.getLineOfSight(5)){
            val b = InstanceService.getInstance("world").getBlock(pos)
            player.send(b.registry().material().toString()+" "+pos.toString())
        }

    }.onlyPlayers()

}, "info")


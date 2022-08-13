package net.geekmc.turingcore.command

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandExecutor
import net.minestom.server.event.entity.EntityTickEvent
import net.minestom.server.potion.PotionEffect
import world.cepi.kstom.Manager
import world.cepi.kstom.event.listen
import world.cepi.kstom.event.listenOnly
import javax.script.ScriptEngineManager

object TestCommand : Command("test", "t") {



    init {

        defaultExecutor =
            CommandExecutor { sender, _ ->
                val serializer = LegacyComponentSerializer.builder().build()
                sender.sendMessage(serializer.deserialize("§ahello &#FFF0F5gu&ays"))
            }

//        addSyntax((sender, context) -> {
//
//                    EntityFinder finder = context.get("player");
//                    Player p = finder.findFirstPlayer(null, null);
//                    if (p == null) {
//                        sender.sendMessage("cant find player");
//                        return;
//                    }
//
//                    PlayerChatEvent playerChatEvent = new PlayerChatEvent(p, MinecraftServer.getConnectionManager().getOnlinePlayers(), () -> {
//                        return Component.text("<" + p.getUsername() + "> " + context.get("word"));
//                    }, context.get("word"));
//                }
//                , ArgumentType.Entity("player").onlyPlayers(true).singleEntity(true)
//                , ArgumentType.Word("word"));
    }
}
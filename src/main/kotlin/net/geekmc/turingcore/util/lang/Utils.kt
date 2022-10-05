package net.geekmc.turingcore.util.lang

import net.geekmc.turingcore.util.color.message
import net.geekmc.turingcore.util.replaceWithOrder
import net.minestom.server.command.CommandSender

fun getLangText(node: String, vararg args: Any): String {
    val lang = LanguageUtil.messageMap[node] ?: return node
    return lang.replaceWithOrder(args)
}

fun CommandSender.sendLang(node: String, vararg args: Any) {
    message(getLangText(node, args))
}
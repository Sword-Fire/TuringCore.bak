package net.geekmc.turingcore.extender

import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.permission.isOp
import net.minestom.server.command.builder.arguments.Argument
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
        sender.send("&r只有管理员能使用这个命令!")
        return@syntax
    }
    executor()
})
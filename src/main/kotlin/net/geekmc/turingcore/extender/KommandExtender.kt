package net.geekmc.turingcore.extender

import world.cepi.kstom.command.kommand.Kommand

val Kommand.SyntaxContext.args
    get() = this.context
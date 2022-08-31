package net.geekmc.turingcore.extender

import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import net.minestom.server.utils.entity.EntityFinder

fun ArgumentEntity.setDefaultValueToSelf(): ArgumentEntity {
    this.setDefaultValue(EntityFinder().setTargetSelector(EntityFinder.TargetSelector.SELF))
    return this
}
package net.geekmc.turingcore.extender

import net.minestom.server.entity.Entity
import java.util.function.Predicate

fun Entity.getLineOfSightEntity(range: Double): Entity? {
    return this.getLineOfSightEntity(range) { true }
}
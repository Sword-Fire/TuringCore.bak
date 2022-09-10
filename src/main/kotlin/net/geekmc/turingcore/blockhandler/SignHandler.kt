package net.geekmc.turingcore.blockhandler

import net.kyori.adventure.key.Key
import net.minestom.server.instance.block.BlockHandler
import net.minestom.server.tag.Tag
import net.minestom.server.utils.NamespaceID

object SignHandler : BlockHandler {
    // 也许不需要Key.key
    override fun getNamespaceId(): NamespaceID = NamespaceID.from("minecraft:sign")
    override fun getBlockEntityTags(): MutableCollection<Tag<*>> {
        return mutableListOf(
            Tag.Byte("GlowingText"),
            Tag.String("Color"),
            // 一个以String存储的Json (String==Component.toString())
            Tag.String("Text1"),
            Tag.String("Text2"),
            Tag.String("Text3"),
            Tag.String("Text4"),
        )
    }
}
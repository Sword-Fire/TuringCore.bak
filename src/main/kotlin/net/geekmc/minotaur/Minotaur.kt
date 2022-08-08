package net.geekmc.minotaur

import net.minestom.server.extensions.Extension
import net.minestom.server.tag.Tag

class Minotaur : Extension() {

    override fun initialize() {
        Tag.ItemStack().list()
        logger.info("Minotaur initialized.")
    }

    override fun terminate() {
        logger.info("Minotaur terminated.")
    }

}
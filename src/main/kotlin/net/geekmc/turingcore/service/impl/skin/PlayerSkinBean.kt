package net.geekmc.turingcore.service.impl.skin

import net.minestom.server.entity.PlayerSkin

data class PlayerSkinBean(var textures: String = "", var signature: String = "") {

    fun toPlayerSkin(): PlayerSkin {
        return PlayerSkin(textures, signature)
    }
}
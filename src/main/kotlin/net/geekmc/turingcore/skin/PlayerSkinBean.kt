package net.geekmc.turingcore.skin

import net.minestom.server.entity.PlayerSkin

fun PlayerSkin.toBean(): PlayerSkinBean {
    return PlayerSkinBean(textures(), signature())
}

data class PlayerSkinBean(var textures: String = "", var signature: String = "") {
    fun toPlayerSkin(): PlayerSkin {
        return PlayerSkin(textures, signature)
    }
}
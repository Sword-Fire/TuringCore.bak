package net.geekmc.turingcore.service.impl.skin

import net.minestom.server.entity.PlayerSkin

fun PlayerSkin.toBean(): PlayerSkinBean {
    return PlayerSkinBean(textures(), signature())
}
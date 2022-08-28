package net.geekmc.turingcore.extender

import net.minestom.server.permission.Permission
import net.minestom.server.permission.PermissionHandler
import org.jglrxavpok.hephaistos.nbt.NBTCompound

fun PermissionHandler.addPermission(string: String, nbt: NBTCompound? = null):Unit = this.addPermission(Permission(string, nbt))
package net.geekmc.turingcore.motd

import net.geekmc.turingcore.TuringCore
import net.geekmc.turingcore.color.toComponent
import net.geekmc.turingcore.data.yaml.YamlData
import net.geekmc.turingcore.extender.GlobalEvent
import net.geekmc.turingcore.extender.resolvePath
import net.geekmc.turingcore.extender.saveResource
import net.minestom.server.event.server.ServerListPingEvent
import net.minestom.server.ping.ResponseData
import world.cepi.kstom.event.listenOnly
import java.io.IOException
import java.nio.file.Files
import java.util.*
import kotlin.collections.List
import kotlin.io.path.exists

object MotdService {

    lateinit var motdData: ResponseData

    /**
     * Load the icon from the motd.png file and set the ping text,
     * which will be sent to player when they ping the server.
     */
    fun start() {

        // save resource
        TuringCore.INSTANCE.saveResource("motd/icon.png")
        TuringCore.INSTANCE.saveResource("motd/motd.yml")

        // set motd response data
        motdData = ResponseData()
        val motdConfig = YamlData(TuringCore.INSTANCE.resolvePath("motd/motd.yml"), MotdService.javaClass.classLoader)

        //出于玄学原因不支持 MiniMessage
        val descriptionList: List<String> = motdConfig.get("description", listOf())
        motdData.description = (descriptionList[0] + "\n" + descriptionList[1]).toComponent()
        motdData.favicon = getIconAsBase64()

        GlobalEvent.listenOnly<ServerListPingEvent> {
            responseData = motdData
        }
    }

    /**
     * Get the base64 encoded icon ,which could be directly sent to player.
     * @return base64 encoded icon with the Mojang-assigned prefix,"" if the icon is not found or could not be encoded.
     */
    private fun getIconAsBase64(): String {
        val path = TuringCore.INSTANCE.resolvePath("motd/icon.png")
        if (!path.exists()) return ""
        try {
            val bytes = Files.readAllBytes(path)
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }
}
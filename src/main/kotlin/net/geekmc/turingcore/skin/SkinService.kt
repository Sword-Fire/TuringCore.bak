package net.geekmc.turingcore.skin

import kotlinx.coroutines.*
import net.geekmc.turingcore.TuringCore
import net.geekmc.turingcore.coroutine.MinestomAsync
import net.geekmc.turingcore.coroutine.MinestomSync
import net.geekmc.turingcore.taml.Taml
import net.geekmc.turingcore.util.GlobalEvent
import net.geekmc.turingcore.util.resolvePath
import net.minestom.server.entity.PlayerSkin
import net.minestom.server.event.player.PlayerSpawnEvent
import world.cepi.kstom.event.listen
import net.geekmc.turingcore.util.saveResource
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor

//延时:1day

//TODO 设置一个私有的CoroutineScope，并在close的时候关闭它
object SkinService {

    private lateinit var skinData: Taml
    private lateinit var scope: CoroutineScope // 只能等服务器启动后再设置
    private var isActive = false

    fun init() {
        if (isActive) return
        isActive = true

        scope = CoroutineScope(Dispatchers.MinestomAsync)

        // 加载皮肤数据
        TuringCore.INSTANCE.saveResource("data/SkinData.yml")
        skinData = Taml(
            TuringCore.INSTANCE.resolvePath("data/SkinData.yml"),
            SkinService::class.java
        )

        // 监听玩家第一次进服
        GlobalEvent.listen<PlayerSpawnEvent> {
            handler {
                if (!isFirstSpawn) return@handler

                // Look up by username,not uuid
                val skinBean: PlayerSkinBean? = skinData["dataByName.${player.username}"]
                scope.launch {

                    //皮肤被缓存过，则立刻更新
                    if (skinBean != null) {
                        player.skin = skinBean.toPlayerSkin()
                    }
                    val skin = withContext(Dispatchers.IO) { PlayerSkin.fromUsername(player.username) } ?: return@launch
                    // 获取的皮肤是新的，则更新皮肤
                    // 如果缓存不存在，也会更新皮肤
                    if (skinBean == null || skinBean.toPlayerSkin() != skin) {
                        skinData["dataByName.${player.username}"] = skin.toBean() // save as PlayerSkinBean
                        player.skin = skin
                    }
                }

            }
        }
    }

    fun close() {
        if (!isActive) return
        isActive = false
        scope.cancel()
        skinData.save()
    }

}
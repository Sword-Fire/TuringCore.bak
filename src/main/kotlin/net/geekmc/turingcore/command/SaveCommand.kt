package net.geekmc.turingcore.command


import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.instance.InstanceService
import net.minestom.server.command.builder.Command

object SaveCommand : Command("save") {

    init {

        setDefaultExecutor { sender, _ ->
            sender.send("&c命令用法不正确。")
        }

        addSyntax({ sender, _ ->

            val world = InstanceService.getInstance(InstanceService.MAIN_INSTANCE);
            world.saveInstance()
            sender.send("成功保存全局数据。")

            val t = System.currentTimeMillis()
            world.saveChunksToStorage()
            sender.send(
                "成功保存 " + world.chunks.size
                        + "个区块，耗时 " + (System.currentTimeMillis() - t) + "ms。"
            )

        })

    }

}
package net.geekmc.turingcore.command


import net.geekmc.turingcore.color.send
import net.geekmc.turingcore.command.kommand.OP
import net.geekmc.turingcore.command.kommand.format
import net.geekmc.turingcore.instance.InstanceService
import world.cepi.kstom.command.kommand.Kommand

object SaveCommand : Kommand({
    format(OP) {

        val world = InstanceService.getInstance(InstanceService.MAIN_INSTANCE);
        world.saveInstance() // save tag data in tag handler of instance
        sender.send("&g成功保存全局数据.")

        val t = System.currentTimeMillis()
        world.saveChunksToStorage()
        sender.send("&g成功保存 &y${world.chunks.size} &g个区块,耗时 &y${System.currentTimeMillis() - t} &ms")
    }
}, "save")
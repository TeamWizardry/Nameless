package teamwizardry.nameless.proxy

import net.minecraft.init.Blocks
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import teamwizardry.nameless.registry.VitaeRegistry

open class Common {
    open fun pre(e: FMLPreInitializationEvent) {}
    open fun init(e: FMLInitializationEvent) {}
    open fun post(e: FMLPostInitializationEvent) {
        VitaeRegistry.register(Blocks.LEAVES, 1.0, Blocks.DIRT)//TODO add decayed leaves
        VitaeRegistry.register(Blocks.LEAVES2, 1.0, Blocks.DIRT)
    }
}

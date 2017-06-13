package teamwizardry.nameless.proxy

import net.minecraft.init.Blocks
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.registry.EntityRegistry
import teamwizardry.nameless.NamelessMod
import teamwizardry.nameless.block.ModBlocks
import teamwizardry.nameless.item.ModItems
import teamwizardry.nameless.registry.VitaeRegistry
import teamwizardry.nameless.silkens.EntitySilken

open class Common {
    open fun pre(e: FMLPreInitializationEvent) {
        ModItems
        ModBlocks
        EntityRegistry.registerModEntity(
                ResourceLocation(NamelessMod.MODID, "silken"), EntitySilken::class.java, "silken",
                0, NamelessMod, 32, 1, true
        )
    }
    open fun init(e: FMLInitializationEvent) {}
    open fun post(e: FMLPostInitializationEvent) {
        VitaeRegistry.register(Blocks.LEAVES, 1.0, Blocks.DIRT)//TODO add decayed leaves
        VitaeRegistry.register(Blocks.LEAVES2, 1.0, Blocks.DIRT)
    }
}

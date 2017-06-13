package teamwizardry.nameless.proxy

import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import teamwizardry.nameless.silkens.EntitySilken
import teamwizardry.nameless.silkens.render.RenderSilken

class Client : Common(){
    override fun pre(e: FMLPreInitializationEvent) {
        super.pre(e)
        RenderingRegistry.registerEntityRenderingHandler(EntitySilken::class.java) { RenderSilken(it) }
    }
    override fun init(e: FMLInitializationEvent) { super.init(e) }
    override fun post(e: FMLPostInitializationEvent) { super.post(e) }
}

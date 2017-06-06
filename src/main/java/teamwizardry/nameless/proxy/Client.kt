package teamwizardry.nameless.proxy

import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

class Client : Common(){
    override fun pre(e: FMLPreInitializationEvent) {}
    override fun init(e: FMLInitializationEvent) {}
    override fun post(e: FMLPostInitializationEvent) {}
}

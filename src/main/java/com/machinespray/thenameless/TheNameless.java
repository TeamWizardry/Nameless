package com.machinespray.thenameless;

import com.machinespray.thenameless.defiler.DefilerHandler;
import com.machinespray.thenameless.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Random;

import static com.machinespray.thenameless.TheNameless.MODID;

@Mod.EventBusSubscriber(modid = MODID)
@Mod(modid = MODID, version = TheNameless.VERSION)

public class TheNameless {
    public static final String MODID = "thenameless";
    public static final String VERSION = "1.0";
    @SidedProxy(modId = MODID, clientSide = "com.machinespray.thenameless.proxy.ClientProxy", serverSide = "com.machinespray.thenameless.proxy.CommonProxy")
    public static CommonProxy proxy;
    public static final Random random = new Random();

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        proxy.preinit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        DefilerHandler.registerVitae();
    }

}

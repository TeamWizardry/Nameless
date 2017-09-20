package com.machinespray.thenameless.proxy;

import com.machinespray.thenameless.Events;
import com.machinespray.thenameless.defiler.DefilerHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
    public void preinit() {
    }

    public void init() {
        MinecraftForge.EVENT_BUS.register(new Events());

    }
}

package com.machinespray.thenameless.proxy;

import com.machinespray.thenameless.client.KeyBindings;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    @Override
    public void preinit() {
        KeyBindings.register();
        super.init();
    }

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new KeyBindings());
        super.init();
    }
}

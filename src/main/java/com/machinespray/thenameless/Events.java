package com.machinespray.thenameless;

import com.machinespray.thenameless.blocks.NamelessBlocks;
import com.machinespray.thenameless.defiler.BlockDeadLeaves;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.machinespray.thenameless.TheNameless.MODID;
import static com.machinespray.thenameless.defiler.DefilerHandler.isDefiler;
import static com.machinespray.thenameless.defiler.DefilerHandler.pullEssenceFromWorld;

@Mod.EventBusSubscriber(modid = MODID)
public class Events {
    private static int tickCount = 0;

    @SubscribeEvent
    public void ServerTick(TickEvent.ServerTickEvent event) {
        if (tickCount % 100 == 0)
            for (EntityPlayer p : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
                if (isDefiler(p))
                    pullEssenceFromWorld(p);
        tickCount++;
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        NamelessBlocks.leaves = new BlockDeadLeaves();
        event.getRegistry().register(NamelessBlocks.leaves);
    }
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void updateLeaves(RenderWorldLastEvent event){
        NamelessBlocks.leaves.setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
    }

}

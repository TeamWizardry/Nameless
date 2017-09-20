package com.machinespray.thenameless.client;

import com.machinespray.thenameless.EntityRock;
import com.machinespray.thenameless.TheNameless;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public class KeyBindings {
    public static KeyBinding fire = new KeyBinding("Fire/Cold", Keyboard.KEY_Z, "key.categories.spellbinds");
    public static KeyBinding water = new KeyBinding("Water/Lightning", Keyboard.KEY_X, "key.categories.spellbinds");
    public static KeyBinding life = new KeyBinding("Life/Death", Keyboard.KEY_C, "key.categories.spellbinds");
    public static KeyBinding earth = new KeyBinding("Earth/Shield", Keyboard.KEY_V, "key.categories.spellbinds");
    private float fovTemp=1;
    private boolean channeling = false;
    private boolean wasChanneling = false;
    public String[] queue = new String[1];

    public static void register() {
        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        ClientRegistry.registerKeyBinding(fire);
        ClientRegistry.registerKeyBinding(water);
        ClientRegistry.registerKeyBinding(life);
        ClientRegistry.registerKeyBinding(earth);
        settings.setOptionKeyBinding(settings.keyBindSaveToolbar, Keyboard.KEY_N);
        settings.setOptionKeyBinding(settings.keyBindLoadToolbar, Keyboard.KEY_M);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    public void onKeyEvent(InputEvent.KeyInputEvent event) {
        if (fire.isPressed()) {
            if (Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()) {
                queue[0] = "c";
            } else {
                queue[0] = "f";
            }
        }
        if (water.isPressed()) {
            queue[0] = "w";
        }
        if (earth.isPressed()) {
            queue[0] = "e";
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    public void onMouseEvent(InputEvent.MouseInputEvent event) {
        channeling = Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown();
        if (!channeling && wasChanneling) {
            if(queue[0]!=null)
                if(queue[0].equals("e"))
                   //Launch rock
            queue = new String[1];
        }
        wasChanneling = channeling;
    }

    @SubscribeEvent
    public void fovUpdate(FOVUpdateEvent e) {
        if(queue[0]!=null)
        if (queue[0].equals("e")&&channeling) {
            if(fovTemp>0.5)
            fovTemp *= 0.99;
            e.setNewfov(e.getFov() * fovTemp);
        }else{
            fovTemp=1;
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientRender(TickEvent.ClientTickEvent event) {
        if (channeling)
            if (queue[0] != null)
                if (queue[0].equals("f")) {
                    renderSpray(Minecraft.getMinecraft().player, Minecraft.getMinecraft().world, EnumParticleTypes.FLAME, 1);
                } else if (queue[0].equals("w")) {
                    renderSpray(Minecraft.getMinecraft().player, Minecraft.getMinecraft().world, EnumParticleTypes.WATER_WAKE, 1);
                } else if (queue[0].equals("c"))
                    renderSpray(Minecraft.getMinecraft().player, Minecraft.getMinecraft().world, EnumParticleTypes.SNOW_SHOVEL, 2);

    }

    public void renderSpray(EntityPlayer p, World w, EnumParticleTypes e, double distance) {
        for (int i = 0; i < 2; i++) {
            double x = (1 + p.getLookVec().x - TheNameless.random.nextDouble() - .5) / 3;
            x = x * distance;
            double z = (1 + p.getLookVec().z - TheNameless.random.nextDouble() - .5) / 3;
            z = z * distance;
            w.spawnParticle(e, p.posX, p.posY + 1, p.posZ, x + p.motionX, p.getLookVec().y / 2 + 0.1, z);
        }
    }
    //@SideOnly(Side.CLIENT)
    //@SubscribeEvent
    //public void onRenderHotbar(){

    //}

}

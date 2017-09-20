package com.machinespray.thenameless;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.world.World;

public class EntityRock extends EntitySnowball {
    public EntityRock(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }
}

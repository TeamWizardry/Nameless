package com.machinespray.thenameless.defiler;

import com.machinespray.thenameless.blocks.NamelessBlocks;
import com.machinespray.thenameless.TheNameless;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefilerHandler {
    public static HashMap<IBlockState, IBlockState> drainToState = new HashMap<>();
    public static HashMap<IBlockState, Double> drainToValue = new HashMap<>();

    private static void register(IBlockState power, IBlockState drained, Double value) {
        drainToState.put(power, drained);
        drainToValue.put(power, value);
    }


    public static void pullEssenceFromWorld(EntityPlayer p) {
        for (BlockPos pos : getBlocksNear(p.world, p.getPosition(), 10)) {
            IBlockState state = p.world.getBlockState(pos);
            if (drainToState.containsKey(state))
                if (TheNameless.random.nextDouble() > .75)
                    p.world.setBlockState(pos, drainToState.get(state));
        }
    }

    public static boolean isDefiler(EntityPlayer p) {
        return true;
    }

    public static List<BlockPos> getBlocksNear(World world, BlockPos playerPos, int radius) {
        List<BlockPos> blocks = new ArrayList<>();
        for (double x = playerPos.getX() - radius; x <= playerPos.getX() + radius; x++) {
            for (double y = playerPos.getY() - radius; y <= playerPos.getY() + radius; y++) {
                for (double z = playerPos.getZ() - radius; z <= playerPos.getZ() + radius; z++) {
                    BlockPos loc = new BlockPos(x, y, z);
                    if (Math.sqrt(Math.pow(Math.abs(playerPos.getX() - x), 2) + Math.pow(Math.abs(playerPos.getZ() - z), 2)) < radius / 2)
                        blocks.add(loc);
                }
            }
        }

        return blocks;
    }

    public static void registerVitae() {
        register(Blocks.GRASS.getDefaultState(), Blocks.DIRT.getStateFromMeta(1), .2D);
        for (int i = 0; i < 3; i++)
            register(Blocks.TALLGRASS.getStateFromMeta(i), Blocks.DEADBUSH.getDefaultState(), 0D);
        for (int i = 0; i < 16; i++)
            register(Blocks.LEAVES.getStateFromMeta(i), NamelessBlocks.leaves.getDefaultState(), 1D);
    }
}

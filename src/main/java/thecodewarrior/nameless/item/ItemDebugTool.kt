package thecodewarrior.nameless.item

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.kotlin.ifCap
import com.teamwizardry.librarianlib.features.kotlin.nbt
import com.teamwizardry.librarianlib.features.network.PacketHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagByte
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thecodewarrior.circuitry.block.ModBlocks
import thecodewarrior.circuitry.internals.CapabilityCircuitWorld
import thecodewarrior.circuitry.api.CircuitryAPI
import thecodewarrior.circuitry.api.NetworkColor
import thecodewarrior.circuitry.network.PacketInspectSync

/**
 * Created by TheCodeWarrior
 */
class ItemDebugTool : ItemMod("circuit_debugger") {
    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        return EnumActionResult.SUCCESS
    }

    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val stack = player.getHeldItem(hand)
        return ActionResult(EnumActionResult.SUCCESS, stack)
    }
}

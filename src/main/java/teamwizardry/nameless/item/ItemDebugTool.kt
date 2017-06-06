package teamwizardry.nameless.item

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World


/**
 * Created by TheCodeWarrior
 */
class ItemDebugTool : ItemMod("test_item") {
    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        return EnumActionResult.SUCCESS
    }

    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val stack = player.getHeldItem(hand)
        return ActionResult(EnumActionResult.SUCCESS, stack)
    }
}

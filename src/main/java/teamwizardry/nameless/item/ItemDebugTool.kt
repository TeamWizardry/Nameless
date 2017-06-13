package teamwizardry.nameless.item

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.vec
import com.teamwizardry.librarianlib.features.kotlin.fromNBT
import com.teamwizardry.librarianlib.features.kotlin.nbt
import com.teamwizardry.librarianlib.features.kotlin.plus
import com.teamwizardry.librarianlib.features.kotlin.toNBT
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import teamwizardry.nameless.block.tile.TileRelay
import teamwizardry.nameless.silkens.EntitySilken


/**
 * Created by TheCodeWarrior
 */
class ItemDebugTool : ItemMod("test_item") {
    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if(!world.isRemote) {
            val nbt = player.getHeldItem(hand).nbt
            val connected: BlockPos? = nbt["pos"]?.fromNBT()

            if(player.isSneaking) {
                val entity = EntitySilken(world)
                entity.setPosition(player.posX, player.posY, player.posZ)
                entity.path.push(Vec3d(pos) + vec(0.5, 0.5, 0.5))
                world.spawnEntity(entity)
            } else {
                if (connected != null) {
                    val cpos = BlockPos(connected)
                    val tile = world.getTileEntity(cpos)
                    if (tile != null && tile is TileRelay) {
                        tile.linkedTo = pos
                    }
                    nbt["pos"] = null
                } else {
                    nbt["pos"] = pos.toNBT()
                }
            }
        }

        return EnumActionResult.SUCCESS
    }

    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val stack = player.getHeldItem(hand)
        return ActionResult(EnumActionResult.SUCCESS, stack)
    }
}

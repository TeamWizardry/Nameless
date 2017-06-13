package teamwizardry.nameless.block

import com.teamwizardry.librarianlib.features.base.block.BlockModContainer
import com.teamwizardry.librarianlib.features.helpers.vec
import com.teamwizardry.librarianlib.features.kotlin.plus
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import teamwizardry.nameless.block.tile.TileRelay
import teamwizardry.nameless.silkens.EntitySilken
import teamwizardry.nameless.silkens.ISilkenListener

/**
 * TODO: Document file BlockRelay
 *
 * Created by TheCodeWarrior
 */
class BlockRelay : BlockModContainer("relay", Material.ROCK), ISilkenListener {
    override fun onIdleAround(state: IBlockState, world: World, pos: BlockPos, silken: EntitySilken) {
        val tile = world.getTileEntity(pos)
        if(tile != null && tile is TileRelay) {
            tile.linkedTo?.also {
                silken.path.push(Vec3d(it) + vec(0.5, 0.5, 0.5))
            }
        }
    }

    override fun onArrive(state: IBlockState, world: World, pos: BlockPos, silken: EntitySilken) {
        val tile = world.getTileEntity(pos)
        if(tile != null && tile is TileRelay) {
            tile.linkedTo?.also {
                silken.path.push(Vec3d(it) + vec(0.5, 0.5, 0.5))
            }
        }
    }

    override fun createTileEntity(world: World, state: IBlockState): TileEntity? = TileRelay()

    override fun onEnter(state: IBlockState, world: World, pos: BlockPos, silken: EntitySilken) {
    }

    override fun onInside(state: IBlockState, world: World, pos: BlockPos, silken: EntitySilken) {

    }

    override fun onLeave(state: IBlockState, world: World, pos: BlockPos, silken: EntitySilken) {

    }
}

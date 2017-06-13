package teamwizardry.nameless.silkens

import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * TODO: Document file ISilkenListener
 *
 * Created by TheCodeWarrior
 */
interface ISilkenListener {
    fun onEnter(state: IBlockState, world: World, pos: BlockPos, silken: EntitySilken)
    fun onInside(state: IBlockState, world: World, pos: BlockPos, silken: EntitySilken)
    fun onLeave(state: IBlockState, world: World, pos: BlockPos, silken: EntitySilken)

    fun onIdleAround(state: IBlockState, world: World, pos: BlockPos, silken: EntitySilken)
    fun onArrive(state: IBlockState, world: World, pos: BlockPos, silken: EntitySilken)
}

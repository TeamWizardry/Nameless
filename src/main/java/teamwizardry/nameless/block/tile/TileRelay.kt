package teamwizardry.nameless.block.tile

import com.teamwizardry.librarianlib.features.autoregister.TileRegister
import com.teamwizardry.librarianlib.features.base.block.TileMod
import com.teamwizardry.librarianlib.features.saving.Save
import net.minecraft.util.math.BlockPos

/**
 * TODO: Document file TileRelay
 *
 * Created by TheCodeWarrior
 */
@TileRegister("relay")
class TileRelay : TileMod() {
    @Save var linkedTo: BlockPos? = null
}

package teamwizardry.nameless.registry

import net.minecraft.block.Block

object VitaeRegistry {
    private var vitaeRegistry = HashMap<Block, Double>()
    private var decayRegistry = HashMap<Block, Block>()

    fun register(b: Block, d: Double, decayBlock: Block) {
        vitaeRegistry.put(b, d)
        decayRegistry.put(b, decayBlock)
    }

    fun getValue(b: Block): Double? {
        return vitaeRegistry.get(b)
    }

    fun getDecay(b: Block): Block? {
        return decayRegistry.get(b)
    }
}
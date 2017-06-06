package teamwizardry.nameless

import com.teamwizardry.librarianlib.features.base.ModCreativeTab
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import teamwizardry.nameless.proxy.Common

@Mod(modid = NamelessMod.MODID, version = NamelessMod.VERSION, name = NamelessMod.MODNAME, dependencies = NamelessMod.DEPENDENCIES, modLanguageAdapter = NamelessMod.ADAPTER, acceptedMinecraftVersions = NamelessMod.ALLOWED)
object NamelessMod {

    @Mod.EventHandler
    fun preInit(e: FMLPreInitializationEvent) {
        PROXY.pre(e)
    }

    @Mod.EventHandler
    fun init(e: FMLInitializationEvent) {
        PROXY.init(e)
    }

    @Mod.EventHandler
    fun postInit(e: FMLPostInitializationEvent) {
        PROXY.post(e)
    }

    const val MODID = "nameless"
    const val MODNAME = "The Nameless"
    const val VERSION = "1.0"
    const val ALLOWED = "[1.11,)"
    const val CLIENT = "teamwizardry.nameless.proxy.Client"
    const val SERVER = "teamwizardry.nameless.proxy.Common"
    const val DEPENDENCIES = "required-after:librarianlib"
    const val ADAPTER = "net.shadowfacts.forgelin.KotlinAdapter"

    @SidedProxy(clientSide = CLIENT, serverSide = SERVER)
    lateinit var PROXY: Common

    val creativeTab = object : ModCreativeTab() {
        init {
            this.registerDefaultTab()
        }
        override val iconStack: ItemStack by lazy {
            ItemStack(Items.REDSTONE) // TODO: Change
        }

    }
}

package teamwizardry.nameless.silkens.render

import com.teamwizardry.librarianlib.features.helpers.vec
import com.teamwizardry.librarianlib.features.kotlin.minus
import com.teamwizardry.librarianlib.features.kotlin.plus
import com.teamwizardry.librarianlib.features.kotlin.pos
import com.teamwizardry.librarianlib.features.kotlin.times
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.Vec3d
import org.lwjgl.opengl.GL11
import teamwizardry.nameless.NamelessMod
import teamwizardry.nameless.silkens.CLOTH_WIDTH
import teamwizardry.nameless.silkens.EntitySilken



/**
 * Created by TheCodeWarrior
 */
class RenderSilken(render: RenderManager) : Render<EntitySilken>(render) {
    val texture = ResourceLocation(NamelessMod.MODID, "textures/entity/silken/cloth.png")
    override fun getEntityTexture(entity: EntitySilken?): ResourceLocation? { return texture }

    override fun doRender(entity: EntitySilken, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        setup(entity, x, y, z, entityYaw, partialTicks)
        /**/ renderCloth(entity, x, y, z, entityYaw, partialTicks)
        teardown(entity, x, y, z, entityYaw, partialTicks)

        super.doRender(entity, x, y, z, entityYaw, partialTicks)
    }

    fun renderCloth(entity: EntitySilken, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        val tessellator = Tessellator.getInstance()
        val vb = tessellator.buffer

        // OpenGL configuration set around `tessellator.draw()` statement at bottom for organization sake

        // VERTEX SETUP ================================================================================================

        vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)

        val height = 16 + entity.renderData.length * 8
        fun pos(w: Int, h: Int) : Vec3d {
            val i = w + h*(CLOTH_WIDTH+1)

            if(i >= entity.renderData.cloth.points.size) {
                return vec(0,0,0)
            }

            val point = entity.renderData.cloth.points[i]

            return point.lastPos + ((point.pos - point.lastPos)*partialTicks)
        }

        fun vert(w: Int, h: Int) {
            vb.pos(pos(w, h)).tex((w+entity.renderData.length*8)/64.0, h/64.0).endVertex()
        }

        for(h in 0..height-1) {
            for (w in 0..CLOTH_WIDTH-1) {
                vert(w  , h  )
                vert(w+1, h  )
                vert(w+1, h+1)
                vert(w  , h+1)
            }
        }

        // OpenGL ======================================================================================================

        // BEGIN OPENGL CONFIGURATION
        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.disableCull()
        Minecraft.getMinecraft().renderEngine.bindTexture(texture)

        tessellator.draw()

        // BEGIN OPENGL TEARDOWN
        GlStateManager.enableCull()
    }

    fun setup(entity: EntitySilken, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        GlStateManager.pushMatrix()
        GlStateManager.translate(x-entity.posX, y-entity.posY, z-entity.posZ)
    }

    fun teardown(entity: EntitySilken, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        GlStateManager.popMatrix()
    }
}

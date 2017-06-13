package teamwizardry.nameless.silkens

import com.teamwizardry.librarianlib.features.base.entity.EntityMod
import com.teamwizardry.librarianlib.features.helpers.vec
import com.teamwizardry.librarianlib.features.kotlin.*
import com.teamwizardry.librarianlib.features.saving.Save
import com.teamwizardry.librarianlib.features.utilities.client.ClientRunnable
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import teamwizardry.nameless.silkens.cloth.Cloth
import teamwizardry.nameless.silkens.cloth.GridCloth
import teamwizardry.nameless.silkens.cloth.LineCloth
import java.util.*
import java.util.concurrent.ThreadLocalRandom

const val CLOTH_WIDTH = 8
const val CLOTH_HEIGHT = 16

class EntitySilken(world: World) : EntityMod(world) {
    override fun entityInit() {
        this.setSize(1f, 1f)
        ClientRunnable.run {
            renderData = SilkenRenderData(this)
        }
    }

    var age = 0

    val speed = (2 + ( rand.nextGaussian() * 0.1 ))/20.0
    @Save val path: Deque<Vec3d> = ArrayDeque()
    @Save val segment: Deque<Vec3d> = ArrayDeque()
    var approachingDirection: Vec3d? = null

    @Save var idlePos: Vec3d? = null
    var idleTarget: Vec3d? = null

    override fun onUpdate() {
        super.onUpdate()
        this.entityBoundingBox = AxisAlignedBB(this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ).expandXyz(4.0)
        ClientRunnable.run {
            renderData.update()
        }

        if(world.isRemote) return

        val acceleration = 0.005

        age++

        val _idlePos = idlePos
        if (_idlePos != null && path.peek() == null && (idleTarget == null || age % 20 == 0)) {
            idleTarget = _idlePos + vec(rand.nextGaussian()*2, rand.nextGaussian()*2, rand.nextGaussian()*2)
        }
        if(segment.peek() != null) {
            idleTarget = null
            idlePos = segment.peek()
        } else {
            idlePos?.also {
                val pos = BlockPos(it)
                val state = this.world.getBlockState(pos)
                if(state.block is ISilkenListener) (state.block as ISilkenListener).onIdleAround(state, this.world, pos, this)
            }
        }
        if(segment.isEmpty() && path.peek() != null) {
            val start = this.positionVector
            val end = path.pop()
            val depth = MathHelper.log2((start-end).lengthVector().toInt()/2)-1
            lightningTransform(segment, start, end, depth)
            this.approachingDirection = segment.peek()!! - this.positionVector
        }


        val target = segment.peek() ?: idleTarget
        segment.forEach { p ->
            (world as? WorldServer)?.also {
                it.spawnParticle(EnumParticleTypes.REDSTONE, p.xCoord, p.yCoord, p.zCoord, 1, 0.0, 0.0, 0.0, 0.0)
            }
        }

        if(this.approachingDirection == null && target != null) {
            this.approachingDirection = target - this.positionVector
        }


        if(target != null) {
            val difference = target - this.positionVector
            val differenceLength = difference.lengthVector()
            val approachDir = this.approachingDirection
            if (approachDir != null && (target - this.positionVector) dot approachDir < 0 && segment.peek() != null) {
                segment.pop()
                this.approachingDirection = null

                val pos = BlockPos(target)
                val state = this.world.getBlockState(pos)
                if(state.block is ISilkenListener) (state.block as ISilkenListener).onArrive(state, this.world, pos, this)
            }

            val direction = difference / differenceLength

            val currentSpeed = this.motionVec.lengthVector()
            var newVelocity = this.motionVec + (direction * acceleration)
            val newSpeed = newVelocity.lengthVector()
            val newVelocityNormal = newVelocity / newSpeed

            if (newSpeed > speed) {
                if (currentSpeed > speed) {
                    newVelocity = newVelocityNormal * Math.min(currentSpeed, newSpeed)
                } else {
                    newVelocity = newVelocityNormal * Math.min(newSpeed, speed)
                }
            }

            this.motionVec = newVelocity
        }

        this.lastTickPosX = this.posX
        this.lastTickPosY = this.posY
        this.lastTickPosZ = this.posZ

        val oldBlock = this.position

        this.posX += this.motionX
        this.posY += this.motionY
        this.posZ += this.motionZ

        if(this.position != oldBlock) {
            var state = this.world.getBlockState(this.position)
            if(state.block is ISilkenListener) (state.block as ISilkenListener).onEnter(state, this.world, this.position, this)
            state = this.world.getBlockState(oldBlock)
            if(state.block is ISilkenListener) (state.block as ISilkenListener).onLeave(state, this.world, oldBlock, this)
        }
        val state = this.world.getBlockState(this.position)
        if(state.block is ISilkenListener) (state.block as ISilkenListener).onInside(state, this.world, this.position, this)

    }

    /**
     * You must add the first position yourself, the rest are added by this method
     */
    fun lightningTransform(list: Deque<Vec3d>, start: Vec3d, end: Vec3d, depth: Int, variation: Double = ((start-end).lengthVector() / 30), taper: Double = 2.0) {
        if(depth < 0) {// allow short-circuiting by passing in negative depth
            list.add(end)
            return
        }
        fun randCoord() = ThreadLocalRandom.current().nextGaussian() * variation

        val normal = (end - start).normalize()
        var randVec = vec(randCoord(), randCoord(), randCoord())
        val proj = normal * (normal dot randVec) // find deviance from plane
        randVec -= proj // remove deviance
        val middle = start + ((end-start) / 2) + randVec

        if(depth == 0) {
            list.addLast(middle)
            list.addLast(end)
            return
        }

        lightningTransform(list, start, middle, depth-1, variation = variation / taper, taper = taper)
        lightningTransform(list, middle, end, depth-1, variation = variation / taper, taper = taper)
    }
    @SideOnly(Side.CLIENT)
    lateinit var renderData: SilkenRenderData

    class SilkenRenderData(val entity: EntitySilken) {
        val length = Math.min(Math.abs(entity.rand.nextGaussian()*2), 4.0).toInt()
        val cloth = Cloth(GridCloth(entity.positionVector - vec(1, 0, 0)/4, vec(1, 0, 0)/16.0, -vec(0, 0, 1)/16.0, CLOTH_WIDTH, 16 + 8*length))
        val pendulum = Cloth(LineCloth(entity.positionVector, vec(0, -1, 0), 2))
        var down = vec(0, -1, 0)
        init {
            (0..CLOTH_WIDTH).forEach { i ->
                cloth.points[i].pinned = true
            }
            pendulum.points[0].pinned = true
            pendulum.dragDampingCoeff = 1.0
            pendulum.gravity = vec(0, -0.1, 0)
        }

        fun update() {
            cloth.gravity = vec(0, -0.01, 0)
            cloth.windVelocity = vec(0, 0, 0)
            cloth.windForce = 0.1


            pendulum.points[0].pos = entity.positionVector
            pendulum.tick()

            val forward = entity.motionVec.normalize()
            down = (down + pendulum.points[1].pos - pendulum.points[0].pos).normalize()

            val side =(down cross forward).let {
                val len = it.lengthVector()
                if(len == 0.0)
                    vec(1, 0, 0)
                else
                    it / len
            }

            val origin = entity.positionVector - (side/4)
            val unit = side / 16.0
            (0..CLOTH_WIDTH).forEach { i ->
                cloth.points[i].pos = origin + (unit*i)
            }

            cloth.tick()

        }
    }
}

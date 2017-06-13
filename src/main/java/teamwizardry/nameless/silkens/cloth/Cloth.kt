package teamwizardry.nameless.silkens.cloth

import com.google.common.primitives.Doubles
import com.teamwizardry.librarianlib.features.helpers.vec
import com.teamwizardry.librarianlib.features.kotlin.*
import net.minecraft.util.math.Vec3d



abstract class Vertex(var pos: Vec3d, var pinned: Boolean = false) {
    var lastPos = pos
    var scratch = vec(0, 0, 0)

    abstract fun normal(): Vec3d
}
data class Constraint(val a: Vertex, val b: Vertex, val length: Double = (a.pos - b.pos).lengthVector(), val hard: Boolean = false) {
    fun resolve(coeff: Double) {
        if(a.pinned && b.pinned) return
        val delta = b.pos - a.pos
        val distance = delta.lengthVector()
        val normalBToA = if(distance == 0.0) return else delta / distance
        val deltaDistance = this.length - distance

        if(deltaDistance == 0.0)
            return
        if(!Doubles.isFinite(distance))
            return

        if(a.pinned || hard) {
            b.pos +=  normalBToA * (deltaDistance * coeff)
        } else if(b.pinned) {
            a.pos += -normalBToA * (deltaDistance * coeff)
        } else {
            b.pos +=  normalBToA * ((deltaDistance/2) * coeff)
            a.pos += -normalBToA * ((deltaDistance/2) * coeff)
        }
    }
}

class Cloth(def: ClothDefinition) {
    var inertialDampingCoeff = 1.0
    var dragDampingCoeff = 0.8
    var springCoeff = 0.8
    var windVelocity = vec(0, 0, 0)
    var windForce = 0.1
    var gravity = vec(0, -0.01, 0)
    val points = mutableListOf<Vertex>()
    val constraints = mutableListOf<Constraint>()

    init {
        val cloth = def.generate(points, constraints)
    }

    fun tick() {
        points.forEach { it.lastPos = it.scratch }
        assembleBBs()
        shiftForAirForce()
        shiftForInertia()
        shiftForAcceleration()
        applyDampening()
        (1..3).forEach {
            resolve()
        }
        points.forEach { it.scratch = it.pos }
    }

    fun assembleBBs() {}

    fun shiftForInertia() {
        points.forEach { point ->
            if(point.pinned) return@forEach

            point.pos = point.pos + (point.pos-point.lastPos)*inertialDampingCoeff
        }
    }

    fun shiftForAcceleration() {
        points.forEach { point ->
            if(point.pinned) return@forEach
            point.pos += gravity
        }
    }

    fun shiftForAirForce() {
        points.forEach { point ->
            if(point.pinned) return@forEach

            val vel = point.pos-point.lastPos

            val airVel = vel + windVelocity

            val norm = point.normal()

            val proj = norm * (norm dot airVel)

            point.scratch = proj * windForce
        }
        points.forEach { if(!it.pinned) it.pos += it.scratch }
    }

    fun applyDampening() {
        points.forEach { point ->
            point.pos = point.lastPos + (point.pos-point.lastPos)*dragDampingCoeff
        }
    }

    fun resolve() {
        constraints.forEach { constraint ->
            constraint.resolve(springCoeff)
        }
    }
}

interface ClothDefinition {
    fun generate(points: MutableList<Vertex>, constraints: MutableList<Constraint>)
}

class GridCloth(val origin: Vec3d, val widthUnit: Vec3d, val heightUnit: Vec3d, val width: Int, val height: Int): ClothDefinition {
    override fun generate(points: MutableList<Vertex>, constraints: MutableList<Constraint>) {
        val newPoints = mutableListOf<Vertex>()
        val newConstraints = mutableListOf<Constraint>()
        addPoints(newPoints)
        newPoints.indices.forEach { addPositiveNeighbors(newConstraints, newPoints, it) }

        points.addAll(newPoints)
        constraints.addAll(newConstraints)
    }

    protected fun addPoints(list: MutableList<Vertex>) {
        for(h in 0..height) {
            for(w in 0..width) {

                val vertex = object : Vertex(origin + (widthUnit * w) + (heightUnit * h)) {
                    override fun normal(): Vec3d {
                        val c = this.pos
                        val u = if(valid(w  , h-1)) list.get(getI(w  , h-1)).pos else null
                        val d = if(valid(w  , h+1)) list.get(getI(w  , h+1)).pos else null
                        val l = if(valid(w-1, h  )) list.get(getI(w-1, h  )).pos else null
                        val r = if(valid(w+1, h  )) list.get(getI(w+1, h  )).pos else null

                        var avg = vec(0,0,0)

                        if(u != null && l != null) {
                            avg += (c-u) cross (c-l)
                        }
                        if(u != null && r != null) {
                            avg += (c-r) cross (c-u)
                        }
                        if(d != null && l != null) {
                            avg += (c-l) cross (c-d)
                        }
                        if(d != null && r != null) {
                            avg += (c-d) cross (c-r)
                        }

                        if(avg.xCoord == 0.0 && avg.yCoord == 0.0 && avg.zCoord == 0.0)
                            return avg
                        return avg.normalize()
                    }
                }
                list.add(vertex)

            }
        }
    }

    protected fun addPositiveNeighbors(constraints: MutableList<Constraint>, list: MutableList<Vertex>, index: Int) {
        val v = list[index]
        val w = getW(index)
        val h = getH(index)

        if(valid(w,   h+1)) constraints.add(Constraint(v, list[getI(w,   h+1)], hard = true)) // down
        if(valid(w+1, h  )) constraints.add(Constraint(v, list[getI(w+1, h  )])) // right
        if(valid(w+1, h+1)) constraints.add(Constraint(v, list[getI(w+1, h+1)])) // down-right
        if(valid(w-1, h+1)) constraints.add(Constraint(v, list[getI(w-1, h+1)])) // down-left
    }

    protected fun getW(index: Int) = index % (width+1)
    protected fun getH(index: Int) = Math.floorDiv(index, width+1)
    protected fun getI(w: Int, h: Int) = w + (width+1) * h
    protected fun valid(w: Int, h: Int) = ( w >= 0 && h >= 0 ) && ( w <= width && h <= height )
}

class LineCloth(val origin: Vec3d, val unit: Vec3d, val length: Int): ClothDefinition {
    override fun generate(points: MutableList<Vertex>, constraints: MutableList<Constraint>) {
        val newPoints = mutableListOf<Vertex>()
        val newConstraints = mutableListOf<Constraint>()
        addPoints(newPoints)
        newPoints.indices.take(length-1).forEach {
            newConstraints.add(Constraint(newPoints[it], newPoints[it+1], hard=true))
        }

        points.addAll(newPoints)
        constraints.addAll(newConstraints)
    }

    protected fun addPoints(list: MutableList<Vertex>) {
        for(i in 0..length-1) {
            val vertex = object : Vertex(origin + (unit * i)) {
                override fun normal(): Vec3d {
                    return vec(0,0,0)
                }

            }
            list.add(vertex)
        }
    }
}

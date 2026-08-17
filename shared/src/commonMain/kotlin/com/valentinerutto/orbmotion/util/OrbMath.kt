package com.valentinerutto.orbmotion.util

import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

internal fun hash01(seed: Int): Float {
    var x = seed
    x = (x xor (x shl 13))
    x = (x xor (x ushr 17))
    x = (x xor (x shl 5))
    return (x and 0x7fffffff) / Int.MAX_VALUE.toFloat()
}


internal const val TAU = (PI * 2).toFloat()

/** Shared 3D primitives for the Working-orbit renderer. */
internal data class ProjectedPoint(val x: Float, val y: Float, val z: Float)

internal class WorkingOrbitProjector(
    private val centerX: Float,
    private val centerY: Float,
    timeSeconds: Float,
) {
    private val sinYaw = sin(timeSeconds * 0.12f)
    private val cosYaw = cos(timeSeconds * 0.12f)
    private val sinTilt = sin(0.3f)
    private val cosTilt = cos(0.3f)

    fun project(x: Float, y: Float, z: Float): ProjectedPoint {
        val x1 = x * cosYaw + z * sinYaw
        val z1 = -x * sinYaw + z * cosYaw
        val y1 = y * cosTilt - z1 * sinTilt
        return ProjectedPoint(
            x = centerX + x1,
            y = centerY - y1,
            z = y * sinTilt + z1 * cosTilt,
        )
    }
}

internal data class WorkingOrbit(
    val radius: Float,
    val phaseOffset: Float,
    val speed: Float,
    private val ux: Float,
    private val uy: Float,
    private val vx: Float,
    private val vy: Float,
    private val vz: Float,
) {
    fun pointAt(angle: Float): ProjectedPoint = ProjectedPoint(
        x = (ux * cos(angle) + vx * sin(angle)) * radius,
        y = (uy * cos(angle) + vy * sin(angle)) * radius,
        z = vz * sin(angle) * radius,
    )
}

internal fun workingOrbit(index: Int, maximumRadius: Float): WorkingOrbit {
    val h1 = workingOrbitHash(index.toFloat(), 1.7f)
    val h2 = workingOrbitHash(index.toFloat(), 5.2f)
    val h3 = workingOrbitHash(index.toFloat(), 8.9f)
    val theta = h1 * TAU
    val phi = acos(2f * h2 - 1f)
    val nx = sin(phi) * cos(theta)
    val ny = cos(phi)
    val nz = sin(phi) * sin(theta)

    var ux = -ny
    var uy = nx
    val basisLength = max(1e-6f, sqrt(ux * ux + uy * uy))
    ux /= basisLength
    uy /= basisLength

    return WorkingOrbit(
        radius = maximumRadius * (0.45f + 0.52f * h1),
        phaseOffset = h2 * 6f,
        speed = (0.25f + 0.55f * h3) * if (h3 > 0.5f) 1f else -1f,
        ux = ux,
        uy = uy,
        vx = -nz * uy,
        vy = nz * ux,
        vz = nx * uy - ny * ux,
    )
}

internal fun workingRadiusScale(size: Float): Float =
    (size / 300f).toDouble().pow(0.6).toFloat()

private fun workingOrbitHash(a: Float, b: Float): Float {
    val value = sin(a * 12.9898f + b * 78.233f) * 43758.5453f
    return value - floor(value)
}


/**
 * A point on a regular polygon with [sides] (3 = triangle, 4 = square, 40+ reads as a circle),
 * centered at the origin with circumradius 1, at normalized perimeter position [t] in `[0, 1)`.
 */
internal fun pointOnPolygon(sides: Int, t: Float): Offset {
    val corner = (t * sides)
    val i = corner.toInt() % sides
    val f = corner - corner.toInt()
    val a0 = TAU * i / sides - PI.toFloat() / 2f
    val a1 = TAU * (i + 1) / sides - PI.toFloat() / 2f
    val x0 = cos(a0); val y0 = sin(a0)
    val x1 = cos(a1); val y1 = sin(a1)
    return Offset(x0 + (x1 - x0) * f, y0 + (y1 - y0) * f)
}


/**
 * Blends between a circle, a triangle and a square outline as [morph] sweeps `0f..1f`:
 * `0..1/3` circle -> triangle, `1/3..2/3` triangle -> square, `2/3..1` square -> circle.
 */
internal fun pointOnMorphedShape(t: Float, morph: Float): Offset {
    val circle =  pointOnPolygon(sides = 64, t = t)
    val triangle = pointOnPolygon(sides = 3, t = t)
    val square = pointOnPolygon(sides = 4, t = t)

    val segment = (morph * 3f).coerceIn(0f, 2.9999f)
    val local = segment - segment.toInt()

    return when (segment.toInt()) {
        0 -> lerp(circle, triangle, local)
        1 -> lerp(triangle, square, local)
        else -> lerp(square, circle, local)
    }
}
private fun lerp(a: Offset, b: Offset, t: Float): Offset =
    Offset(a.x + (b.x - a.x) * t, a.y + (b.y - a.y) * t)



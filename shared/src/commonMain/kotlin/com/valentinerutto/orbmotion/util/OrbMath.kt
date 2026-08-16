package com.valentinerutto.orbmotion.util

import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

internal fun hash01(seed: Int): Float {
    var x = seed
    x = (x xor (x shl 13))
    x = (x xor (x ushr 17))
    x = (x xor (x shl 5))
    return (x and 0x7fffffff) / Int.MAX_VALUE.toFloat()
}


internal const val TAU = (PI * 2).toFloat()


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
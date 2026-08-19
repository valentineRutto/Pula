package com.valentinerutto.orbmotion.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.valentinerutto.orbmotion.orbs.OrbState

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

internal fun DrawScope.drawOrbState(
    state: OrbState,
    phase: Float,
    color: Color,
    dotCount: Int,
    dotRadiusRatio: Float,
) {

    val cx = size.width / 2f
    val cy = size.height / 2f
    val r = size.minDimension / 2f * 0.86f
    val dotR = size.minDimension * dotRadiusRatio

    when (state) {
        OrbState.Working -> drawWorking(cx, cy, color, phase)
        OrbState.Searching -> drawSearching(cx, cy, r, dotR, color, phase, dotCount)
        OrbState.Solving -> drawSolving(cx, cy, r, dotR, color, phase, dotCount)
        OrbState.Listening -> drawListening(cx, cy, r, dotR, color, phase, dotCount)
        OrbState.Composing -> drawComposing(cx, cy, r, dotR, color, phase, dotCount)
        OrbState.Shaping -> drawShaping(cx, cy, r, dotR, color, phase, dotCount)
        OrbState.Breathing -> drawBreathing(cx, cy, r, dotR, color, phase, dotCount)
        OrbState.Connecting -> drawConnecting(cx, cy, r, dotR, color, phase, dotCount)
        OrbState.Weaving -> drawWeaving(cx, cy, r, dotR, color, phase, dotCount)
    }
}

/**
 * Port of thinking-orbs' `frameOrbits`: twelve deterministic 3D orbit planes,
 * each represented by a faint dotted trail and three travelling particles.
 */
private fun DrawScope.drawWorking(
    cx: Float,
    cy: Float,
    color: Color,
    phase: Float,
) {
    data class Dot(val z: Float, val center: Offset, val radius: Float, val alpha: Float)

    val sourceSize = size.minDimension
    val orbitRadius = sourceSize * 0.5f * 0.82f
    val radiusScale = workingRadiusScale(sourceSize)
    val timeSeconds = phase * 3.2f
    val projector = WorkingOrbitProjector(cx, cy, timeSeconds)
    val dots = ArrayList<Dot>(12 * (40 + 3))

    for (orbit in 0 until 12) {
        val plane = workingOrbit(orbit, orbitRadius)

        for (trailDot in 0 until 40) {
            val angle = trailDot.toFloat() / 40f * TAU
            val sourcePoint = plane.pointAt(angle)
            val point = projector.project(sourcePoint.x, sourcePoint.y, sourcePoint.z)
            val depth = (point.z / plane.radius + 1f) * 0.5f
            dots += Dot(
                z = point.z,
                center = Offset(point.x, point.y),
                radius = (0.9f * radiusScale).coerceAtLeast(0.3f),
                alpha = 0.28f * 0.5f * (0.4f + 0.6f * depth),
            )
        }

        for (particle in 0 until 3) {
            val angle = timeSeconds * plane.speed + particle.toFloat() / 3f * TAU + plane.phaseOffset
            val sourcePoint = plane.pointAt(angle)
            val point = projector.project(sourcePoint.x, sourcePoint.y, sourcePoint.z)
            val depth = (point.z / plane.radius + 1f) * 0.5f
            dots += Dot(
                z = point.z,
                center = Offset(point.x, point.y),
                radius = ((1.2f + 1.6f * depth) * radiusScale).coerceAtLeast(0.3f),
                alpha = 0.78f + 0.22f * depth,
            )
        }
    }

    dots.sortBy { it.z }
    dots.forEach { dot ->
        drawCircle(color = color, radius = dot.radius, center = dot.center, alpha = dot.alpha)
    }
}
/** A scan meridian sweeping a dotted globe. */
private fun DrawScope.drawSearching(
    cx: Float, cy: Float, r: Float, dotR: Float, color: Color, phase: Float, dotCount: Int,
) {
    val rings = 8
    val perRing = (dotCount / rings).coerceAtLeast(6)
    val sweep = phase * TAU
    for (j in 0 until rings) {
        val lat = -1f + 2f * j / (rings - 1)
        val y = r * lat * 0.9f
        val ringR = r * sqrt((1f - lat * lat).coerceAtLeast(0f))
        for (i in 0 until perRing) {
            val lon = TAU * i / perRing
            val x = ringR * cos(lon)
            val z = sin(lon)
            val angDist = angularDistance(lon, sweep)
            val glow = (1f - (angDist / (PI.toFloat() / 3f)).coerceIn(0f, 1f))
            val depthAlpha = 0.22f + 0.35f * (z * 0.5f + 0.5f)
            drawCircle(
                color = color,
                radius = dotR * (0.7f + 0.6f * glow),
                center = Offset(cx + x, cy + y),
                alpha = (depthAlpha + glow * 0.65f).coerceIn(0.1f, 1f),
            )
        }
    }
}
/** Concentric bands scramble, then click back into aligned rings. */
private fun DrawScope.drawSolving(
    cx: Float, cy: Float, r: Float, dotR: Float, color: Color, phase: Float, dotCount: Int,
) {
    val rings = 4
    val perRing = (dotCount / rings).coerceAtLeast(6)
    val chaos = when {
        phase < 0.68f -> 1f
        phase < 0.8f -> 1f - (phase - 0.68f) / 0.12f
        else -> 0f
    }.coerceIn(0f, 1f)
    for (ringIdx in 0 until rings) {
        val ringR = r * (0.3f + 0.22f * ringIdx)
        for (i in 0 until perRing) {
            val seed = ringIdx * 1000 + i
            val jitterAngle = (hash01(seed) - 0.5f) * 0.9f * chaos
            val jitterRadius = (hash01(seed + 500) - 0.5f) * r * 0.35f * chaos
            val angle = TAU * i / perRing + jitterAngle
            val radius = ringR + jitterRadius
            drawCircle(
                color = color,
                radius = dotR,
                center = Offset(cx + cos(angle) * radius, cy + sin(angle) * radius),
                alpha = (0.45f + 0.55f * (1f - chaos)).coerceIn(0.2f, 1f),
            )
        }
    }
}
/** A waveform ripple rolling outward through concentric rings. */
private fun DrawScope.drawListening(
    cx: Float, cy: Float, r: Float, dotR: Float, color: Color, phase: Float, dotCount: Int,
) {
    val rings = 5
    val perRing = (dotCount / rings).coerceAtLeast(8)
    for (ringIdx in 0 until rings) {
        val baseR = r * (0.25f + 0.18f * ringIdx)
        for (i in 0 until perRing) {
            val angle = TAU * i / perRing
            val wave = sin(angle * 3f - phase * TAU * 2f + ringIdx * 0.6f)
            val radius = baseR + wave * r * 0.05f
            val glow = wave * 0.5f + 0.5f
            drawCircle(
                color = color,
                radius = dotR * (0.8f + 0.3f * glow),
                center = Offset(cx + cos(angle) * radius, cy + sin(angle) * radius),
                alpha = (0.4f + 0.5f * glow).coerceIn(0.2f, 1f),
            )
        }
    }
}
/** An undulating, multi-band sash of dots. */
private fun DrawScope.drawComposing(
    cx: Float, cy: Float, r: Float, dotR: Float, color: Color, phase: Float, dotCount: Int,
) {
    val bands = 3
    val perBand = (dotCount / bands).coerceAtLeast(10)
    for (b in 0 until bands) {
        val yOffset = (b - (bands - 1) / 2f) * r * 0.32f
        val amplitude = r * 0.22f
        val freq = 2.2f + b * 0.4f
        for (i in 0 until perBand) {
            val t = i.toFloat() / perBand
            val x = (t - 0.5f) * r * 1.7f
            val y = yOffset + sin(t * TAU * freq + phase * TAU + b) * amplitude
            val edgeFade = 1f - (abs(t - 0.5f) * 2f).coerceIn(0f, 1f)
            drawCircle(
                color = color,
                radius = dotR,
                center = Offset(cx + x, cy + y),
                alpha = (0.3f + 0.7f * edgeFade).coerceIn(0.15f, 1f),
            )
        }
    }
}
/** A dotted outline morphing: circle -> triangle -> square -> circle. */
private fun DrawScope.drawShaping(
    cx: Float, cy: Float, r: Float, dotR: Float, color: Color, phase: Float, dotCount: Int,
) {
    for (i in 0 until dotCount) {
        val t = i.toFloat() / dotCount
        val p = pointOnMorphedShape(t = t, morph = phase)
        drawCircle(color = color, radius = dotR, center = Offset(cx + p.x * r, cy + p.y * r), alpha = 0.85f)
    }
}

/** A single ring slowly breathing in and out. */
private fun DrawScope.drawBreathing(
    cx: Float, cy: Float, r: Float, dotR: Float, color: Color, phase: Float, dotCount: Int,
) {
    val pulse = sin(phase * TAU) * 0.5f + 0.5f
    val ringR = r * (0.55f + 0.2f * pulse)
    for (i in 0 until dotCount) {
        val angle = TAU * i / dotCount
        drawCircle(
            color = color,
            radius = dotR * (0.8f + 0.4f * pulse),
            center = Offset(cx + cos(angle) * ringR, cy + sin(angle) * ringR),
            alpha = (0.35f + 0.5f * pulse).coerceIn(0.2f, 1f),
        )
    }
}

/** A constellation of dots wiring itself together edge by edge. */
private fun DrawScope.drawConnecting(
    cx: Float, cy: Float, r: Float, dotR: Float, color: Color, phase: Float, dotCount: Int,
) {
    val nodeCount = dotCount.coerceIn(8, 18)
    val nodes = List(nodeCount) { i ->
        val angle = TAU * hash01(i * 13 + 1)
        val radius = r * (0.35f + 0.55f * hash01(i * 29 + 7))
        Offset(cx + cos(angle) * radius, cy + sin(angle) * radius)
    }
    val revealed = (phase * nodeCount).toInt()
    for (e in 0 until nodeCount) {
        if (e > revealed) continue
        val a = nodes[e]
        val b = nodes[(e * 7 + 3) % nodeCount]
        val edgeAlpha = if (e == revealed) (phase * nodeCount - revealed) else 0.5f
        drawLine(color = color, start = a, end = b, strokeWidth = dotR * 0.5f, alpha = edgeAlpha.coerceIn(0f, 0.5f))
    }
    nodes.forEachIndexed { i, p ->
        drawCircle(color = color, radius = dotR * 1.1f, center = p, alpha = 0.7f + 0.3f * hash01(i))
    }
}

/** Three strands plaiting around the sphere. */
private fun DrawScope.drawWeaving(
    cx: Float, cy: Float, r: Float, dotR: Float, color: Color, phase: Float, dotCount: Int,
) {
    val strandCount = 3
    val perStrand = (dotCount / strandCount).coerceAtLeast(12)
    for (s in 0 until strandCount) {
        val strandOffset = TAU / strandCount * s
        for (i in 0 until perStrand) {
            val t = i.toFloat() / perStrand
            val angle = t * TAU * 2f + phase * TAU + strandOffset
            val depth = cos(angle)
            val x = depth * r * 0.75f
            val y = sin(t * TAU * 2f + phase * TAU * 1.5f + strandOffset) * r * 0.85f
            drawCircle(
                color = color,
                radius = dotR * (0.75f + 0.35f * (depth * 0.5f + 0.5f)),
                center = Offset(cx + x, cy + y),
                alpha = (0.3f + 0.6f * (depth * 0.5f + 0.5f)).coerceIn(0.15f, 1f),
            )
        }
    }
}
private fun angularDistance(a: Float, b: Float): Float {
    var d = (a - b) % TAU
    if (d > PI) d -= TAU
    if (d < -PI) d += TAU
    return abs(d)
}

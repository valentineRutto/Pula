package com.valentinerutto.orbmotion.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import kotlin.math.max

@Composable
internal fun rememberOrbPhase(
    periodMillis: Long,
    speed: Float,
    paused: Boolean,
): Float {
    var phase by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(periodMillis, speed, paused) {
        if (paused) return@LaunchedEffect
        val safeSpeed = max(speed, 0.01f)
        val periodNanos = ((periodMillis * 1_000_000L) / safeSpeed).toLong().coerceAtLeast(1L)
        while (true) {
            withFrameNanos { nowNanos ->
                val inCycle = nowNanos % periodNanos
                phase = inCycle.toFloat() / periodNanos.toFloat()
            }
        }
    }

    return phase
}

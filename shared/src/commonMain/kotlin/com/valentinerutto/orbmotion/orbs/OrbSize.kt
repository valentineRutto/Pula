package com.valentinerutto.orbmotion.orbs

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Two tuned presets, deliberately not just a scale factor on one another: each carries its
 * own dot count and dot radius so the orb still reads clearly at either scale.
 *
 * Marked [Immutable] so the Compose compiler can treat instances as stable and skip
 * recomposing [ThinkingOrb] when an equal [OrbSize] is passed in again.
 */
@Immutable
sealed class OrbSize(
    public val dp: Dp,
    internal val dotCount: Int,
    internal val dotRadiusRatio: Float
){

    /** 64.dp — chat-avatar scale. */
    public data object Large : OrbSize(dp = 64.dp, dotCount = 48, dotRadiusRatio = 0.018f)

    /** 20.dp — inline-text scale. */
    public data object Small : OrbSize(dp = 20.dp, dotCount = 22, dotRadiusRatio = 0.032f)

public data class Custom(val size: Dp): OrbSize(
    dp = size, dotCount = interpolatedDotCount(size), dotRadiusRatio = interpolatedDotRadiusRatio(size)
)

    private companion object{
        fun fraction(size: Dp): Float =
            ((size.value - OrbSize.Small.dp.value) / (OrbSize.Large.dp.value - OrbSize.Small.dp.value)).coerceIn(0f, 1f)

        fun interpolatedDotCount(size: Dp): Int {
            val t = OrbSize.Companion.fraction(size)
            return (OrbSize.Small.dotCount + (OrbSize.Large.dotCount - OrbSize.Small.dotCount) * t)
                .toInt()
                .coerceAtLeast(8)
        }

        fun interpolatedDotRadiusRatio(size: Dp): Float {
            val t = OrbSize.Companion.fraction(size)
            return OrbSize.Small.dotRadiusRatio + (OrbSize.Large.dotRadiusRatio - OrbSize.Small.dotRadiusRatio) * t
        }
    }

}
package com.valentinerutto.orbmotion.orbs

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import com.valentinerutto.orbmotion.util.drawOrbState
import com.valentinerutto.orbmotion.util.rememberOrbPhase




@Composable
fun ThinkingOrb(
    state: OrbState,
    modifier: Modifier = Modifier,
    size: OrbSize = OrbSize.Large,
    theme: OrbTheme = OrbTheme.Light ,
    speed: Float = 1f,
    paused: Boolean = false,
    reducedMotion: Boolean = false,
    contentDescription: String? = null,
    dotColorOverride: Color? = null,
    backgroundOverride: Color? = null,
) {

    val dark = when (theme) {
        OrbTheme.Auto -> isSystemInDarkTheme()
        OrbTheme.Dark -> true
        OrbTheme.Light -> false
    }


    val themeBg = backgroundOverride ?: MaterialTheme.colorScheme.background
    val themeDot = dotColorOverride ?: MaterialTheme.colorScheme.primary

    val dotColor = if (dark) themeDot else themeDot
    val dotRadiusRatio = when (size) { OrbSize.Small -> 0.024f; is OrbSize.Custom -> 0.033f; OrbSize.Large -> 0.045f }


    val animatedPhase = rememberOrbPhase(
        periodMillis = state.basePeriodMillis,
        speed = speed,
        paused = paused || reducedMotion,
    )

    val renderPhase = if (reducedMotion) state.staticPhase else animatedPhase
    val accessibilityDescription = contentDescription ?: state.defaultDescription

    Canvas(
        modifier = modifier.size(size.dp).semantics {
            role = Role.Image
            this.contentDescription = accessibilityDescription
        }
    ) {

        drawOrbState(state = state,phase = renderPhase, color= dotColor, dotCount = size.dotCount, dotRadiusRatio = size.dotRadiusRatio)

    }

}
private val LightDot = Color(0xFFECECEC)
private val DarkDot = Color(0xFF1A1A1A)

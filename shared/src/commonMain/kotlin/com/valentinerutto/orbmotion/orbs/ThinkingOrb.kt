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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.valentinerutto.orbmotion.util.drawOrbState
import com.valentinerutto.orbmotion.util.rememberOrbPhase

import kotlin.math.*



@Composable
fun ThinkingOrb(
    state: OrbState,
    modifier: Modifier = Modifier,
   size: OrbSize = OrbSize.Large,
    theme: OrbTheme = OrbTheme.Auto,
    speed: Float = 1f,
    paused: Boolean = false,
    reducedMotion: Boolean = false,
    contentDescription: String? = null,
) {
    val dark = when (theme) {
        OrbTheme.Auto -> isSystemInDarkTheme()
        OrbTheme.Dark -> true
        OrbTheme.Light -> false
    }
    val dotColor = if (dark) LightDot else DarkDot

    val animatedPhase = rememberOrbPhase(
        periodMillis = state.basePeriodMillis,
        speed = speed,
        paused = paused || reducedMotion,
    )

    val renderPhase = if (reducedMotion) state.staticPhase else animatedPhase

    Canvas(modifier = modifier.size(size.dp).semantics{role = Role.Image
        this.contentDescription = contentDescription ?: stateDescription}){

        drawOrbState(state = state,phase = renderPhase, color= dotColor, dotCount = size.dotCount, dotRadiusRatio = size.dotRadiusRatio)

    }

}
private val LightDot = Color(0xFFECECEC)
private val DarkDot = Color(0xFF1A1A1A)
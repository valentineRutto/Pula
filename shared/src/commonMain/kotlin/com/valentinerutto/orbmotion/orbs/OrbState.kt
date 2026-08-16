package com.valentinerutto.orbmotion.orbs

/**
 * The nine "verbs" an agent can be doing, each mapped to a distinct dotted animation.
 *
 * The state vocabulary and overall dotted, monochrome look are inspired by Jakub Antalik's
 * **Thinking Orbs** (https://orbs.jakubantalik.com, MIT licensed) — full credit in this
 * module's README. This is an independent Compose Multiplatform implementation: the drawing
 * code below was written from scratch against Compose's [androidx.compose.ui.graphics.drawscope.DrawScope],
 * not ported from the original canvas source.
 *
 * @param basePeriodMillis length of one full animation loop at `speed = 1f`.
 * @param staticPhase the representative frame rendered when `reducedMotion = true`.
 * @param defaultDescription accessibility label used when no explicit `contentDescription`
 * is supplied to [ThinkingOrb].
 */
enum class OrbState(
    internal val basePeriodMillis: Long,
    internal val staticPhase: Float,
    internal val defaultDescription: String
){
    /** Particles drifting along tilted, overlapping orbits. */
    Working(basePeriodMillis = 3200, staticPhase = 0.25f, defaultDescription = "Working"),

    /** A scan meridian sweeping around a dotted globe. */
    Searching(basePeriodMillis = 2600, staticPhase = 0.0f, defaultDescription = "Searching"),

    /** Concentric bands of dots scramble, then click back into aligned rings. */
    Solving(basePeriodMillis = 2200, staticPhase = 0.9f, defaultDescription = "Solving"),

    /** A waveform ripple rolling outward through concentric rings. */
    Listening(basePeriodMillis = 1800, staticPhase = 0.5f, defaultDescription = "Listening"),

    /** An undulating, multi-band sash of dots. */
    Composing(basePeriodMillis = 2800, staticPhase = 0.25f, defaultDescription = "Composing"),

    /** A dotted outline morphing: circle to triangle to square, and back. */
    Shaping(basePeriodMillis = 4200, staticPhase = 0.0f, defaultDescription = "Shaping"),

    /** A single ring slowly breathing in and out. */
    Breathing(basePeriodMillis = 4800, staticPhase = 0.5f, defaultDescription = "Idle"),

    /** A constellation of dots wiring itself together edge by edge. */
    Connecting(basePeriodMillis = 3600, staticPhase = 0.6f, defaultDescription = "Connecting"),

    /** Three strands plaiting around the sphere. */
    Weaving(basePeriodMillis = 3000, staticPhase = 0.3f, defaultDescription = "Weaving"),
}
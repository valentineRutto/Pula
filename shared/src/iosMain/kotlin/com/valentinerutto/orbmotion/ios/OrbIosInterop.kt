package com.valentinerutto.orbmotion.ios

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import com.valentinerutto.orbmotion.orbs.OrbSize
import com.valentinerutto.orbmotion.orbs.OrbTheme
import com.valentinerutto.orbmotion.orbs.ThinkingOrbGallery
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

// Factory to create a UIViewController hosting the gallery with controls applied from Swift
fun makeThinkingOrbGalleryViewController(
    speed: Float,
    sizeDp: Float,
    themeOrdinal: Int,
    dotColorArgb: Long,
    bgColorArgb: Long,
): UIViewController = ComposeUIViewController {
    // Resolve OrbSize from ordinal: 0=Small,1=Custom,2=Large
    val orbSize = when (sizeDp.toInt()) {
        0 -> OrbSize.Small
        2 -> OrbSize.Large
        else -> OrbSize.Custom(sizeDp.dp)
    }
    val theme = OrbTheme.values()[themeOrdinal.coerceIn(0, OrbTheme.values().size - 1)]

    val dotColor = Color(dotColorArgb)
    val bgColor = Color(bgColorArgb)

    ThinkingOrbGallery(
        modifier = Modifier.fillMaxSize(),
        size = orbSize,
        theme = theme,
        speed = speed,
        dotColorOverride = dotColor,
        backgroundOverride = bgColor,
    )
}

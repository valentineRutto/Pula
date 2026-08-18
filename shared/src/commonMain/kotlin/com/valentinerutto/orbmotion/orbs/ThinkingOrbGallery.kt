package com.valentinerutto.orbmotion.orbs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
public fun ThinkingOrbGallery(
    modifier: Modifier = Modifier,
    size: OrbSize = OrbSize.Large,
    theme: OrbTheme = OrbTheme.Auto,
    speed: Float = 1f,
    dotColorOverride: Color? = null,
    backgroundOverride: Color? = null,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 96.dp),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(OrbState.entries) { state ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp),
            ) {
                ThinkingOrb(
                    state = state,
                    size = size,
                    theme = theme,
                    speed = speed,
                    dotColorOverride = dotColorOverride,
                    backgroundOverride = backgroundOverride,
                )
                Text(text = state.name)
            }
        }
    }
}
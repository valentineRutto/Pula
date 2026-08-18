package com.valentinerutto.orbmotion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinerutto.orbmotion.orbs.OrbState
import com.valentinerutto.orbmotion.orbs.OrbTheme
import com.valentinerutto.orbmotion.orbs.ThinkingOrb
import com.valentinerutto.orbmotion.orbs.ThinkingOrbGallery
import org.jetbrains.compose.resources.painterResource

import orbmotionkmplibrary.shared.generated.resources.Res
import orbmotionkmplibrary.shared.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {


        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                                    var speed by remember { mutableStateOf(1f) }
                                    var theme by remember { mutableStateOf(OrbTheme.Auto) }
                                    var sizeMode by remember { mutableStateOf(1) } // 0=Small,1=Custom,2=Large
                                    var customSize by remember { mutableStateOf(48f) }

                                    // Controls
                                    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                                        Text(text = "Speed: ${"%.2f".format(speed)}x")
                                        androidx.compose.material3.Slider(value = speed, onValueChange = { speed = it }, valueRange = 0.25f..2f)

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(text = "Size")
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            androidx.compose.material3.Button(onClick = { sizeMode = 0 }) { Text("Small") }
                                            androidx.compose.material3.Button(onClick = { sizeMode = 1 }) { Text("Custom") }
                                            androidx.compose.material3.Button(onClick = { sizeMode = 2 }) { Text("Large") }
                                        }
                                        if (sizeMode == 1) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(text = "Custom size: ${customSize.toInt()} dp")
                                            androidx.compose.material3.Slider(value = customSize, onValueChange = { customSize = it }, valueRange = 20f..128f)
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(text = "Theme")
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            androidx.compose.material3.Button(onClick = { theme = OrbTheme.Auto }) { Text("Auto") }
                                            androidx.compose.material3.Button(onClick = { theme = OrbTheme.Light }) { Text("Light") }
                                            androidx.compose.material3.Button(onClick = { theme = OrbTheme.Dark }) { Text("Dark") }
                                        }
                                    }

                                    // Determine selected OrbSize
                                    val orbSize = when (sizeMode) {
                                        0 -> com.valentinerutto.orbmotion.orbs.OrbSize.Small
                                        2 -> com.valentinerutto.orbmotion.orbs.OrbSize.Large
                                        else -> com.valentinerutto.orbmotion.orbs.OrbSize.Custom(customSize.dp)
                                    }

                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        ThinkingOrbGallery(modifier = Modifier.fillMaxSize().padding(24.dp), size = orbSize, theme = theme, speed = speed)
                                    }

                }



        }
    }
}
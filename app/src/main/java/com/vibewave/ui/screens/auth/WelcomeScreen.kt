package com.vibewave.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vibewave.R
import com.vibewave.ui.theme.VibeWaveTheme
import kotlinx.coroutines.delay

/**
 * Splash + welcome screen. Particles converge into the Wavee logo,
 * then a tagline and CTA buttons slide in.
 *
 * Reveal logic:
 *   • ParticleLogo's onReady fires when the converge animation finishes (~2.2s)
 *   • A safety net at 3.0s reveals the buttons no matter what
 *   • Once visible, ctaVisible never flips back, so subsequent calls are no-ops
 */
@Composable
fun WelcomeScreen(
    onLogin: () -> Unit,
    onSignUp: () -> Unit,
) {
    val colors = VibeWaveTheme.colors
    var ctaVisible by remember { mutableStateOf(false) }

    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    // Hard guarantee: buttons appear after 3 seconds even if ParticleLogo
    // never calls onReady (e.g. canvas size never resolves on weird devices)
    LaunchedEffect(Unit) {
        delay(3000)
        ctaVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0A0A12),
                        colors.surfaceElevated,
                    )
                )
            ),
    ) {
        // Particle field — fills the entire screen, but targets are
        // positioned in the upper 28% so the logo sits near the top.
        ParticleLogo(
            accent = colors.accent,
            onReady = { ctaVisible = true },
        )

        // Tagline + CTAs slide in from below once particles settle.
        AnimatedVisibility(
            visible = ctaVisible,
            enter = slideInVertically(tween(700)) { it / 2 } + fadeIn(tween(700)),
            exit = slideOutVertically(tween(300)) { it / 2 } + fadeOut(tween(300)),
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
                    .padding(bottom = bottomInset + 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
            ) {
                Text(
                    text = stringResource(R.string.welcome_tagline),
                    color = colors.onSurface,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.welcome_subtitle),
                    color = colors.onSurfaceMuted,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(32.dp))

                // Primary: Sign up
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(27.dp))
                        .background(colors.accent)
                        .clickable(onClick = onSignUp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.welcome_get_started),
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                Spacer(Modifier.height(12.dp))

                // Secondary: Log in (ghost style)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(27.dp))
                        .background(Color.White.copy(alpha = 0.06f))
                        .clickable(onClick = onLogin),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.welcome_have_account),
                        color = colors.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    }
}

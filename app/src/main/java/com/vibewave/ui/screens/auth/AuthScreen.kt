package com.vibewave.ui.screens.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vibewave.R
import com.vibewave.ui.theme.VibeWaveTheme

/**
 * Combined login / register. [FormState.Mode] switches between the two
 * variants with a horizontal AnimatedContent.
 */
@Composable
fun AuthScreen(
    startMode: FormState.Mode,
    onBack: () -> Unit,
    onAuthenticated: () -> Unit,
    vm: AuthViewModel = hiltViewModel(),
) {
    val colors = VibeWaveTheme.colors
    val form by vm.form.collectAsStateWithLifecycle()

    // Honor the initial mode from the caller (welcome screen).
    androidx.compose.runtime.LaunchedEffect(Unit) { vm.setMode(startMode) }

    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0A0A12), colors.surfaceElevated)
                )
            )
            .imePadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = topInset, bottom = bottomInset)
                .padding(horizontal = 28.dp),
        ) {
            // ── Top bar (back only) ──────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth().height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.auth_back),
                        tint = colors.onSurface,
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Small logo ───────────────────────────────────────────────
            androidx.compose.foundation.Image(
                painter = painterResource(R.drawable.logo_wavee),
                contentDescription = "Wavee",
                modifier = Modifier
                    .height(42.dp)
                    .padding(horizontal = 4.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(colors.onSurface),
            )

            Spacer(Modifier.height(32.dp))

            // ── Title — changes between login / register ────────────────
            AnimatedContent(
                targetState = form.mode,
                transitionSpec = {
                    (slideInHorizontally(tween(300)) { it } + fadeIn())
                        .togetherWith(slideOutHorizontally(tween(300)) { -it } + fadeOut())
                },
                label = "title",
            ) { mode ->
                Column {
                    Text(
                        when (mode) {
                            FormState.Mode.LOGIN -> stringResource(R.string.auth_welcome_back)
                            FormState.Mode.REGISTER -> stringResource(R.string.auth_create_account)
                        },
                        color = colors.onSurface,
                        style = MaterialTheme.typography.displayMedium,
                    )
                    Text(
                        when (mode) {
                            FormState.Mode.LOGIN -> stringResource(R.string.auth_login_subtitle)
                            FormState.Mode.REGISTER -> stringResource(R.string.auth_signup_subtitle)
                        },
                        color = colors.onSurfaceMuted,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // ── Display name (register only) ─────────────────────────────
            if (form.mode == FormState.Mode.REGISTER) {
                OutlinedTextField(
                    value = form.displayName,
                    onValueChange = vm::setDisplayName,
                    label = { Text(stringResource(R.string.auth_name)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    colors = vibewaveFieldColors(colors),
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(12.dp))
            }

            OutlinedTextField(
                value = form.email,
                onValueChange = vm::setEmail,
                label = { Text(stringResource(R.string.auth_email)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
                colors = vibewaveFieldColors(colors),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = form.password,
                onValueChange = vm::setPassword,
                label = { Text(stringResource(R.string.auth_password)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                colors = vibewaveFieldColors(colors),
                modifier = Modifier.fillMaxWidth(),
            )

            // ── Error message ────────────────────────────────────────────
            if (form.errorRes != null) {
                Spacer(Modifier.height(12.dp))
                Text(
                    stringResource(form.errorRes!!),
                    color = Color(0xFFFF6B6B),
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Submit button ────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(27.dp))
                    .background(
                        if (form.canSubmit && !form.isLoading) colors.accent
                        else colors.accent.copy(alpha = 0.4f),
                    )
                    .clickable(enabled = form.canSubmit && !form.isLoading) {
                        vm.submit(onSuccess = onAuthenticated)
                    },
                contentAlignment = Alignment.Center,
            ) {
                if (form.isLoading) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        strokeWidth = 2.5.dp,
                        modifier = Modifier.size(22.dp),
                    )
                } else {
                    Text(
                        when (form.mode) {
                            FormState.Mode.LOGIN -> stringResource(R.string.auth_login)
                            FormState.Mode.REGISTER -> stringResource(R.string.auth_create_account)
                        },
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Switch mode link ────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    when (form.mode) {
                        FormState.Mode.LOGIN -> stringResource(R.string.auth_no_account)
                        FormState.Mode.REGISTER -> stringResource(R.string.auth_have_account)
                    },
                    color = colors.onSurfaceMuted,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    when (form.mode) {
                        FormState.Mode.LOGIN -> stringResource(R.string.auth_create_one)
                        FormState.Mode.REGISTER -> stringResource(R.string.auth_login_link)
                    },
                    color = colors.accent,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable {
                        vm.setMode(
                            if (form.mode == FormState.Mode.LOGIN) FormState.Mode.REGISTER
                            else FormState.Mode.LOGIN
                        )
                    },
                )
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

/**
 * Outlined TextField colors — tinted to match the current theme's accent.
 * Centralized so we stay visually consistent across all fields.
 */
@Composable
private fun vibewaveFieldColors(c: com.vibewave.ui.theme.VibeColors) =
    OutlinedTextFieldDefaults.colors(
        focusedBorderColor = c.accent,
        unfocusedBorderColor = Color.White.copy(alpha = 0.18f),
        focusedLabelColor = c.accent,
        unfocusedLabelColor = c.onSurfaceMuted,
        cursorColor = c.accent,
        focusedTextColor = c.onSurface,
        unfocusedTextColor = c.onSurface,
    )

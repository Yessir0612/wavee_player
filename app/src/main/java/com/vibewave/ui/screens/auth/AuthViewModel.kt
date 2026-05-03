package com.vibewave.ui.screens.auth

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.vibewave.R
import com.vibewave.data.repository.AuthRepository
import com.vibewave.data.repository.AuthUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Holds form state for both sign-in and sign-up. A single screen switches
 * between "login" and "register" modes by flipping [FormState.mode].
 *
 * [errorRes] holds a *string resource id* rather than a literal string,
 * so the UI shows whichever translation matches the user's chosen locale.
 */
data class FormState(
    val mode: Mode = Mode.LOGIN,
    val email: String = "",
    val password: String = "",
    val displayName: String = "",
    val isLoading: Boolean = false,
    @StringRes val errorRes: Int? = null,
) {
    val canSubmit: Boolean get() = when (mode) {
        Mode.LOGIN -> email.isNotBlank() && password.length >= 6
        Mode.REGISTER -> email.isNotBlank() && password.length >= 6 && displayName.isNotBlank()
    }

    enum class Mode { LOGIN, REGISTER }
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository,
) : ViewModel() {

    private val _form = MutableStateFlow(FormState())
    val form: StateFlow<FormState> = _form.asStateFlow()

    val currentUser: StateFlow<AuthUser?> = repo.currentUser
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    // ── Form mutations ───────────────────────────────────────────────────────

    fun setMode(mode: FormState.Mode) =
        _form.update { it.copy(mode = mode, errorRes = null) }

    fun setEmail(value: String) =
        _form.update { it.copy(email = value, errorRes = null) }

    fun setPassword(value: String) =
        _form.update { it.copy(password = value, errorRes = null) }

    fun setDisplayName(value: String) =
        _form.update { it.copy(displayName = value, errorRes = null) }

    // ── Submit ───────────────────────────────────────────────────────────────

    /** Called from the single submit button — dispatches by mode. */
    fun submit(onSuccess: () -> Unit) {
        val s = _form.value
        if (!s.canSubmit || s.isLoading) return

        _form.update { it.copy(isLoading = true, errorRes = null) }
        viewModelScope.launch {
            val outcome = runCatching {
                when (s.mode) {
                    FormState.Mode.LOGIN -> repo.signIn(s.email.trim(), s.password)
                    FormState.Mode.REGISTER -> repo.signUp(
                        s.email.trim(),
                        s.password,
                        s.displayName.trim().takeIf { it.isNotBlank() },
                    )
                }
            }
            _form.update {
                it.copy(
                    isLoading = false,
                    errorRes = outcome.exceptionOrNull()?.let(::friendlyMessageRes),
                )
            }
            if (outcome.isSuccess) onSuccess()
        }
    }

    /**
     * Map Firebase exceptions to localized string resources.
     * Returns the resource id; the Compose layer resolves it.
     */
    @StringRes
    private fun friendlyMessageRes(t: Throwable): Int = when (t) {
        is FirebaseAuthInvalidCredentialsException -> R.string.auth_error_wrong_password
        is FirebaseAuthInvalidUserException -> R.string.auth_error_no_account
        is FirebaseAuthUserCollisionException -> R.string.auth_error_email_in_use
        is FirebaseAuthWeakPasswordException -> R.string.auth_error_short_password
        else -> R.string.auth_error_generic
    }
}

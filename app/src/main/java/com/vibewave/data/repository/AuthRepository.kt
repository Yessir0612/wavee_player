package com.vibewave.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Domain-level auth user — stripped of Firebase types so the UI layer
 * doesn't need to depend on the Firebase SDK.
 */
data class AuthUser(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?,
)

/**
 * Thin wrapper over [FirebaseAuth]. Exposes:
 *   - [currentUser] — cold Flow that emits on every sign-in/out
 *   - suspend functions for sign-in, sign-up, sign-out
 *
 * All suspend functions throw on failure; callers should wrap in try/catch
 * and map exceptions to user-facing error strings (done in AuthViewModel).
 */
@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) {
    /** True if someone is currently signed in — read synchronously for routing. */
    val isSignedIn: Boolean get() = firebaseAuth.currentUser != null

    /**
     * Emits the current user whenever it changes. Uses [FirebaseAuth]'s
     * internal listener so we're notified of token refreshes, sign-outs,
     * and sign-ins from other parts of the app.
     */
    val currentUser: Flow<AuthUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toDomain())
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    suspend fun signIn(email: String, password: String): AuthUser {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return result.user?.toDomain() ?: error("Sign-in returned no user")
    }

    suspend fun signUp(email: String, password: String, displayName: String?): AuthUser {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: error("Sign-up returned no user")

        // Best-effort set the display name
        displayName?.takeIf { it.isNotBlank() }?.let { name ->
            val update = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            user.updateProfile(update).await()
        }
        return user.toDomain()
    }

    suspend fun sendPasswordReset(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }

    fun signOut() = firebaseAuth.signOut()

    private fun FirebaseUser.toDomain(): AuthUser = AuthUser(
        uid = uid,
        email = email,
        displayName = displayName,
        photoUrl = photoUrl?.toString(),
    )
}

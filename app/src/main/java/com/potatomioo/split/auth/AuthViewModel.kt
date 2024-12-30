package com.potatomioo.split.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    init {
        auth.currentUser?.let { user ->
            _authState.value = AuthState.Success(
                user = AuthUser(
                    id = user.uid,
                    name = user.displayName,
                    email = user.email
                )
            )
        }
    }

    fun handleSignInResult(idToken: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
                val result = auth.signInWithCredential(credential).await()

                result.user?.let { user ->
                    _authState.value = AuthState.Success(
                        AuthUser(
                            id = user.uid,
                            name = user.displayName,
                            email = user.email
                        )
                    )
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign in failed")
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.SignedOut
    }
}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Success(val user: AuthUser) : AuthState()
    data class Error(val message: String) : AuthState()
    object SignedOut : AuthState()
}

data class AuthUser(
    val id: String,
    val name: String?,
    val email: String?
)
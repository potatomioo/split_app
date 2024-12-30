package com.potatomioo.split.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    googleSignInClient: GoogleSignInClient
) {
    val authState by authViewModel.authState.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account.idToken?.let { token ->
                authViewModel.handleSignInResult(token)
            }
        } catch (e: ApiException) {
            // Handle error
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = authState) {
            is AuthState.Initial -> SignInButton { launcher.launch(googleSignInClient.signInIntent) }
            is AuthState.Loading -> CircularProgressIndicator()
            is AuthState.Success -> {
                Text("Welcome ${state.user.name}")
                Button(onClick = { authViewModel.signOut() }) {
                    Text("Sign Out")
                }
            }
            is AuthState.Error -> {
                Text(state.message, color = MaterialTheme.colorScheme.error)
                SignInButton { launcher.launch(googleSignInClient.signInIntent) }
            }
            is AuthState.SignedOut -> SignInButton { launcher.launch(googleSignInClient.signInIntent) }
        }
    }
}

@Composable
private fun SignInButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Sign in with Google")
    }
}
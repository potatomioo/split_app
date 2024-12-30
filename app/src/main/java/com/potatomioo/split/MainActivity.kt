// app/src/main/java/com/potatomioo/split/MainActivity.kt
package com.potatomioo.split

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.potatomioo.split.auth.AuthScreen
import com.potatomioo.split.auth.AuthViewModel
import com.potatomioo.split.ui.theme.Split_appTheme

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            Split_appTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthScreen(
                        authViewModel = authViewModel,
                        googleSignInClient = googleSignInClient
                    )
                }
            }
        }
    }
}
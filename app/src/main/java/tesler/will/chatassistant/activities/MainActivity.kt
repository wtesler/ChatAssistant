package tesler.will.chatassistant.activities

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import tesler.will.chatassistant.R
import tesler.will.chatassistant._components.Main
import tesler.will.chatassistant._components.permissions.PermissionWrapper
import tesler.will.chatassistant.modules.main.mainModule
import tesler.will.chatassistant.stack.BackStackManager
import tesler.will.chatassistant.stack.State.MAIN
import tesler.will.chatassistant.theme.AppTheme


class MainActivity : ComponentActivity() {
    private val backStackManager by inject<BackStackManager>()
    private lateinit var oneTapClient: SignInClient
    private lateinit var auth: FirebaseAuth

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            backStackManager.pop()
            if (backStackManager.empty()) {
                isEnabled = false
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        val currentUser = auth.currentUser
        if (auth.currentUser == null) {
            oneTapClient = Identity.getSignInClient(this)
            val signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.server_client_oauth_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                )
                .setAutoSelectEnabled(true)
                .build()

            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this) { result ->
                    try {
                        startIntentSenderForResult(
                            result.pendingIntent.intentSender, 2,
                            null, 0, 0, 0, null
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e("MainActivity", "Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
                .addOnFailureListener(this) { e ->
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                    Log.e("MainActivity", e.localizedMessage!!, e)
                }
        } else {
            Log.d("MainActivity", "Current User: ${currentUser!!.email}")
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        loadKoinModules(mainModule)

        backStackManager.push(MAIN)

        setContent {
            AppTheme {
                PermissionWrapper(this) {
                    Main(this)
                }
            }
        }

        window.setLayout(MATCH_PARENT, MATCH_PARENT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            2 -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            Log.d("MainActivity", "ID token: $idToken")
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        Log.d(
                                            "MainActivity",
                                            "signInWithCredential:success: ${auth.currentUser}"
                                        )
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.e(
                                            "MainActivity",
                                            "signInWithCredential:failure",
                                            task.exception
                                        )
                                        Toast.makeText(
                                            this,
                                            "Failed to authenticate user.",
                                            Toast.LENGTH_LONG
                                        )
                                            .show()
                                    }
                                }
                        }
                        else -> {
                            Log.e("MainActivity", "No ID token")
                            Toast.makeText(this, "No ID Token received.", Toast.LENGTH_LONG)
                                .show()
                            finish()
                        }
                    }
                } catch (e: ApiException) {
                    Log.e("MainActivity", "Api Exception", e)
                    if (e.statusCode == CommonStatusCodes.CANCELED) {
                        Toast.makeText(this, "You must login to use this app.", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(
                            this,
                            "Received API Error: ${e.statusCode}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                    throw e
                }
            }
        }
    }

    override fun onResume() {
        if (backStackManager.empty()) {
            backStackManager.push(MAIN)
        }
        onBackPressedCallback.isEnabled = true
        super.onResume()
    }

    override fun onDestroy() {
        unloadKoinModules(mainModule)
        super.onDestroy()
    }
}

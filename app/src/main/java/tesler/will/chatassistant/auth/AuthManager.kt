package tesler.will.chatassistant.auth

import android.app.Activity
import android.content.IntentSender
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.tasks.await
import tesler.will.chatassistant.R
import tesler.will.chatassistant.auth.IAuthManager.Listener

class AuthManager : IAuthManager {
    private lateinit var activity: ComponentActivity
    private lateinit var launcher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var oneTapClient: SignInClient
    private lateinit var scope: CoroutineScope

    private val listeners = mutableListOf<Listener>()

    override fun init(componentActivity: ComponentActivity) {
        activity = componentActivity

        launcher = activity.registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult(),
            ::onActivityResult
        )

        oneTapClient = Identity.getSignInClient(activity)
    }

    override fun beginSignIn(scope: CoroutineScope) {
        this.scope = scope

        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(activity.getString(R.string.server_client_oauth_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(activity) { result ->
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(result.pendingIntent.intentSender)
                            .build()

                    launcher.launch(intentSenderRequest)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("Auth", "Couldn't start One Tap UI: ${e.localizedMessage}", e)
                    emitFailure()
                }
            }
            .addOnFailureListener(activity) { e ->
                Log.e("Auth", e.localizedMessage!!, e)
                emitFailure()
            }
    }

    override suspend fun fetchIdToken(): String {
        val token = Firebase.auth.currentUser!!.getIdToken(false)
        val tokenResult = token.await()
        return tokenResult.token!!
    }

    override fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    private fun onActivityResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val auth = Firebase.auth
            val data = result.data
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken
                when {
                    idToken != null -> {
                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener(activity) { task ->
                                if (task.isSuccessful) {
                                    emitSuccess()
                                } else {
                                    Log.e("Auth", "signInWithCredential:failure", task.exception)
                                    emitFailure()
                                }
                            }
                    }
                    else -> {
                        emitFailure()
                    }
                }
            } catch (e: ApiException) {
                Log.e("Auth", "Received API Error: ${e.statusCode}", e)
                emitFailure()
            }
        } else {
            Log.e("Auth", "Activity Result not ok.")
            emitFailure()
        }
    }

    private fun emitSuccess() {
        for (listener in listeners) {
            listener.onAuthSuccess()
        }
    }

    private fun emitFailure() {
        for (listener in listeners) {
            listener.onAuthFailed()
        }
    }
}

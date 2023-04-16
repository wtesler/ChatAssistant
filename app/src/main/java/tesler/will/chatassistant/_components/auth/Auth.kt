package tesler.will.chatassistant._components.auth

import androidx.activity.ComponentActivity
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.compose.koinInject
import tesler.will.chatassistant.R
import tesler.will.chatassistant.auth.IAuthManager

@Composable
fun Auth(activity: ComponentActivity, content: @Composable () -> Unit) {
    val authManager = koinInject<IAuthManager>()

    var isAuthed by remember { mutableStateOf(false) }
    var hasAuthFailed by remember { mutableStateOf(false) }

    val authListener = remember {
        object : IAuthManager.Listener {
            override fun onAuthSuccess() {
                isAuthed = true
            }

            override fun onAuthFailed() {
                hasAuthFailed = true
            }
        }
    }

    DisposableEffect(Unit) {
        authManager.addListener(authListener)

        onDispose {
            authManager.removeListener(authListener)
        }
    }

    LaunchedEffect(Unit) {
        val auth = Firebase.auth
        if (auth.currentUser != null) {
            isAuthed = true
        } else {
            authManager.beginSignIn()
        }
    }

    if (isAuthed) {
        content()
    } else if (hasAuthFailed) {
        val appName = activity.getString(R.string.app_name)

        AlertDialog(
            onDismissRequest = { activity.finish() },
            title = { Text(text = "Login Failed") },
            text = { Text(text = "You must login to use $appName.") },
            confirmButton = {
                Button(onClick = {
                    activity.finish()
                }) {
                    Text(text = "OK")
                }
            }
        )
    }
}

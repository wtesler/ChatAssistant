package tesler.will.chatassistant.auth

import androidx.activity.ComponentActivity
import kotlinx.coroutines.CoroutineScope

interface IAuthManager {
    fun init(componentActivity: ComponentActivity)
    fun beginSignIn(scope: CoroutineScope)
    fun getIdToken(): String?
    fun fetchIdToken(scope: CoroutineScope)
    fun addListener(listener: Listener)
    fun removeListener(listener: Listener)

    interface Listener {
        fun onAuthSuccess()
        fun onAuthFailed()
    }
}



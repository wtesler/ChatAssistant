package tesler.will.chatassistant.auth

import androidx.activity.ComponentActivity

interface IAuthManager {
    fun init(componentActivity: ComponentActivity)
    fun beginSignIn()
    fun addListener(listener: Listener)
    fun removeListener(listener: Listener)

    interface Listener {
        fun onAuthSuccess()
        fun onAuthFailed()
    }
}



package tesler.will.chatassistant.auth

import androidx.activity.ComponentActivity
import kotlinx.coroutines.CoroutineScope

class EmptyAuthManager : IAuthManager {
    override fun init(componentActivity: ComponentActivity) {
    }

    override fun beginSignIn(scope: CoroutineScope) {
    }

    override fun getIdToken(): String {
        return ""
    }

    override fun fetchIdToken(scope: CoroutineScope) {
    }

    override fun addListener(listener: IAuthManager.Listener) {
    }

    override fun removeListener(listener: IAuthManager.Listener) {
    }
}

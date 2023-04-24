package tesler.will.chatassistant.auth

import androidx.activity.ComponentActivity
import kotlinx.coroutines.CoroutineScope

class EmptyAuthManager : IAuthManager {
    override fun init(componentActivity: ComponentActivity) {
    }

    override fun beginSignIn(scope: CoroutineScope) {
    }

    override suspend fun fetchIdToken(): String {
        return ""
    }

    override fun addListener(listener: IAuthManager.Listener) {
    }

    override fun removeListener(listener: IAuthManager.Listener) {
    }
}

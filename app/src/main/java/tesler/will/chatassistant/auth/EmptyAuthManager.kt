package tesler.will.chatassistant.auth

import androidx.activity.ComponentActivity

class EmptyAuthManager : IAuthManager {
    override fun init(componentActivity: ComponentActivity) {
    }

    override fun beginSignIn() {
    }

    override fun addListener(listener: IAuthManager.Listener) {
    }

    override fun removeListener(listener: IAuthManager.Listener) {
    }
}

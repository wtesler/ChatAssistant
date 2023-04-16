package tesler.will.chatassistant.stack

import androidx.activity.ComponentActivity
import tesler.will.chatassistant.stack.IBackStackManager.Listener

class EmptyBackStackManager: IBackStackManager {
    override fun init(componentActivity: ComponentActivity) {
    }

    override fun push(state: State) {

    }

    override fun pop() {

    }

    override fun empty(): Boolean {
        return true
    }

    override fun addListener(listener: Listener) {

    }

    override fun removeListener(listener: Listener) {
    }

    override fun setEnabled(isEnabled: Boolean) {
    }
}



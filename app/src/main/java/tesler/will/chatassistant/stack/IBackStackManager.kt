package tesler.will.chatassistant.stack

import androidx.activity.ComponentActivity

enum class State {
    MAIN, SETTINGS
}

interface IBackStackManager {
    fun init(componentActivity: ComponentActivity)

    fun push(state: State)

    fun pop()

    fun empty(): Boolean

    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    fun setEnabled(isEnabled: Boolean)

    interface Listener {
        fun onBackStackChange(state: State)
    }
}



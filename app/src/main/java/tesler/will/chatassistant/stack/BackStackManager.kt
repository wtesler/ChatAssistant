package tesler.will.chatassistant.stack

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import tesler.will.chatassistant.stack.IBackStackManager.Listener
import java.util.*

class BackStackManager: IBackStackManager {
    private val stack = Stack<State>()

    private lateinit var activity: ComponentActivity

    private val listeners = mutableListOf<Listener>()

    override fun init(componentActivity: ComponentActivity) {
        activity = componentActivity
        activity.onBackPressedDispatcher.addCallback(activity, onBackPressedCallback)
    }

    override fun push(state: State) {
        if (!empty() && state == stack.peek()) {
            return
        }
        stack.push(state)
        for (listener in listeners) {
            listener.onBackStackChange(state)
        }
    }

    override fun pop() {
        if (empty()) {
            return
        }
        stack.pop()
        for (listener in listeners) {
            if (!stack.empty()) {
                listener.onBackStackChange(stack.peek())
            }
        }
    }

    override fun empty(): Boolean {
        return stack.empty()
    }

    override fun addListener(listener: Listener) {
        listeners.add(listener)
        if (!empty()) {
            listener.onBackStackChange(stack.peek())
        }
    }

    override fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    override fun setEnabled(isEnabled: Boolean) {
        onBackPressedCallback.isEnabled = isEnabled
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            pop()
            if (empty()) {
                isEnabled = false
                activity.finish()
            }
        }
    }
}



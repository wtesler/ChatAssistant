package tesler.will.chatassistant.stack

import java.util.*

enum class State {
    MAIN, SETTINGS
}

class BackStackManager {
    private val stack = Stack<State>()

    private val listeners = mutableListOf<Listener>()

    fun push(state: State) {
        if (!empty() && state == stack.peek()) {
            return
        }
        stack.push(state)
        for (listener in listeners) {
            listener.onBackStackChange(state)
        }
    }

    fun pop() {
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

    fun empty(): Boolean {
        return stack.empty()
    }

    fun addListener(listener: Listener) {
        listeners.add(listener)
        if (!empty()) {
            listener.onBackStackChange(stack.peek())
        }
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    interface Listener {
        fun onBackStackChange(state: State)
    }
}



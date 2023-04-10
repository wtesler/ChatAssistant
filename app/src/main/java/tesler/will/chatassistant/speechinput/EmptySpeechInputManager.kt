package tesler.will.chatassistant.speechinput

import tesler.will.chatassistant.speechinput.ISpeechInputManager.*

class EmptySpeechInputManager : ISpeechInputManager {
    override fun init() {
    }

    override fun destroy() {
    }

    override fun start() {
    }

    override fun stop() {
    }

    override fun isAvailable(): Boolean {
        return false
    }

    override fun addListener(listener: Listener) {
    }

    override fun removeListener(listener: Listener) {
    }
}
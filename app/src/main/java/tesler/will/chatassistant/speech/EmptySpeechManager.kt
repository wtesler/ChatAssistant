package tesler.will.chatassistant.speech

import tesler.will.chatassistant.speech.ISpeechManager.*

class EmptySpeechManager : ISpeechManager {
    override fun start() {
    }

    override fun stop() {
    }

    override fun addListener(listener: Listener) {
    }

    override fun removeListener(listener: Listener) {
    }
}
package tesler.will.chatassistant.speechoutput

class EmptySpeechOutputManager : ISpeechOutputManager {
    override fun init() {
    }

    override fun reset() {
    }

    override fun destroy() {
    }

    override fun stop() {
    }

    override fun addListener(listener: ISpeechOutputManager.Listener) {
    }

    override fun removeListener(listener: ISpeechOutputManager.Listener) {
    }

    override fun queueSpeech(text: String) {
    }

    override fun flushSpeech() {
    }

    override fun setMuted(isMuted: Boolean) {
        TODO("Not yet implemented")
    }
}
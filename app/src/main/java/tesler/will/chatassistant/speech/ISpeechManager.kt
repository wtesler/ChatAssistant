package tesler.will.chatassistant.speech

interface ISpeechManager {

    fun start()
    fun stop()
    fun addListener(listener: Listener)
    fun removeListener(listener: Listener)

    interface Listener {
        fun onSpeechStarted() = run { }
        fun onSpeechFinished() = run { }
        fun onText(value: String?) = run { }
        fun onAmplitude(value: Float?) = run { }
        fun onError(statusCode: Int?) = run { }
    }
}

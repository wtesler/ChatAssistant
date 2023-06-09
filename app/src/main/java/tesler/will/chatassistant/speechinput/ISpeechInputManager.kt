package tesler.will.chatassistant.speechinput

interface ISpeechInputManager {

    fun init()
    fun destroy()
    fun start()
    fun stop()
    fun isAvailable(): Boolean
    fun isListening(): Boolean
    fun addListener(listener: Listener)
    fun removeListener(listener: Listener)

    interface Listener {
        fun onListeningStarted() = run { }
        fun onSpeechStarted() = run { }
        fun onSpeechFinished() = run { }
        fun onText(value: String?) = run { }
        fun onAmplitude(value: Float?) = run { }
        fun onError(statusCode: Int?) = run { }
    }
}

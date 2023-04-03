package tesler.will.chatassistant.speechoutput

interface ISpeechOutputManager {
    fun init()
    fun reset()
    fun destroy()
    fun stop()
    fun addListener(listener: Listener)
    fun removeListener(listener: Listener)
    fun queueSpeech(text: String)
    fun flushSpeech()
    fun setMuted(isMuted: Boolean)

    interface Listener {
        fun onSpeechProgress(progress: Float) = run { }
    }
}

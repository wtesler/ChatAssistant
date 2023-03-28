package tesler.will.chatassistant.speech

interface ISpeechManager {

    fun start()
    fun stop()
    fun clear()
    fun addStartedListener(listener: SpeechStartedListener)
    fun addFinishedListener(listener: SpeechFinishedListener)
    fun addTextListener(listener: SpeechTextListener)
    fun addAmplitudeListener(listener: SpeechAmplitudeListener)
    fun addErrorListener(listener: SpeechErrorListener)
    fun removeStartedListener(listener: SpeechStartedListener)
    fun removeFinishedListener(listener: SpeechFinishedListener)
    fun removeTextListener(listener: SpeechTextListener)
    fun removeAmplitudeListener(listener: SpeechAmplitudeListener)
    fun removeErrorListener(listener: SpeechErrorListener)

    interface SpeechStartedListener {
        fun onSpeechStarted()
    }

    interface SpeechFinishedListener {
        fun onSpeechFinished()
    }

    interface SpeechTextListener {
        fun onText(value: String?)
    }

    interface SpeechAmplitudeListener {
        fun onAmplitude(value: Float?)
    }

    interface SpeechErrorListener {
        fun onError(statusCode: Int?)
    }
}
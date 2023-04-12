package tesler.will.chatassistant.speechoutput

import android.speech.tts.Voice

interface ISpeechOutputManager {
    fun init()
    fun isInit(): Boolean
    fun destroy()
    fun stop()
    fun addListener(listener: Listener)
    fun removeListener(listener: Listener)
    fun queueSpeech(text: String)
    fun speak(text: String, queueType: Int)
    fun isSpeaking(): Boolean
    fun flushSpeech()
    fun setMuted(isMuted: Boolean)
    fun getDefaultVoice(): Voice
    fun getVoices(): MutableSet<Voice>
    fun setVoice(voice: String)
    fun setSpeed(speed: Float)

    interface Listener {
        fun onTtsReady() = run { }
        fun onSpeechInProgress() = run { }
        fun onSpeechEnded() = run { }
    }
}

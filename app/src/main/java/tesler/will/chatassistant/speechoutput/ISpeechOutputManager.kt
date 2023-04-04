package tesler.will.chatassistant.speechoutput

import android.speech.tts.Voice

interface ISpeechOutputManager {
    fun init(voice: String?, speed: Float?)
    fun isInit(): Boolean
    fun reset()
    fun destroy()
    fun stop()
    fun addListener(listener: Listener)
    fun removeListener(listener: Listener)
    fun queueSpeech(text: String)
    fun flushSpeech()
    fun setMuted(isMuted: Boolean)
    fun getDefaultVoice(): Voice
    fun getVoices(): MutableSet<Voice>

    interface Listener {
        fun onTtsReady() = run { }
        fun onSpeechProgress(progress: Float) = run { }
    }
}

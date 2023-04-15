package tesler.will.chatassistant.speechoutput

import android.speech.tts.Voice

interface ISpeechOutputManager {
    fun init()
    fun isInit(): Boolean
    fun destroy()
    fun stop()
    fun addListener(listener: Listener)
    fun removeListener(listener: Listener)
    fun queueSpeech(text: String): SpeechChunk?
    fun speak(text: String, queueType: Int)
    fun isSpeaking(): Boolean
    fun flushSpeech(): SpeechChunk
    fun setMuted(isMuted: Boolean)
    fun getDefaultVoice(): Voice
    fun getVoices(): MutableSet<Voice>
    fun setVoice(voice: String)
    fun setSpeed(speed: Float)

    interface Listener {
        fun onTtsReady(chunk: SpeechChunk?) = run { }
        fun onSpeechInProgress() = run { }
        fun onSpeechEnded() = run { }
        fun onSpeechProgress(utteranceId: String?, start: Int, end: Int) = run { }
    }

    data class SpeechChunk(val text: String, val utteranceId: String, val trimmedAmount: Int)
}

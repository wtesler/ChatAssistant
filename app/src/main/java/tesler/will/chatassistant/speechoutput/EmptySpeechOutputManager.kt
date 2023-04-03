package tesler.will.chatassistant.speechoutput

import android.speech.tts.Voice
import java.util.Locale

class EmptySpeechOutputManager : ISpeechOutputManager {
    override fun init(voice: String?) {
    }

    override fun isInit(): Boolean {
        return false
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
    }

    override fun getDefaultVoice(): Voice {
        return Voice("Test", Locale.getDefault(), 500, 500, false, mutableSetOf())
    }

    override fun getVoices(): MutableSet<Voice> {
        return mutableSetOf()
    }
}
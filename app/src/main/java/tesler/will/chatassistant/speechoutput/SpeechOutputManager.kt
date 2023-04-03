package tesler.will.chatassistant.speechoutput

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.util.Log
import android.widget.Toast
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager.Listener
import java.util.*


class SpeechOutputManager(private val context: Context) : ISpeechOutputManager, OnInitListener {

    private var tts: TextToSpeech? = null
    private var pendingText: String? = null
    private var hasInit = false
    private var queuedSpeech = ""
    private var isMute = false

    private val listeners = mutableListOf<Listener>()

    override fun init() {
        if (tts == null) {
            tts = TextToSpeech(context, this)
        } else {
            Log.w("Speech Output", "TTS already started")
        }
    }

    override fun reset() {
    }

    override fun destroy() {
        tts?.shutdown()
        tts = null
        pendingText = null
        hasInit = false
        queuedSpeech = ""
    }

    override fun stop() {
        tts?.stop()
        pendingText = null
        queuedSpeech = ""
    }

    override fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    override fun queueSpeech(text: String) {
        queuedSpeech += text

        if (!hasInit) {
            return
        }

        if (tts!!.isSpeaking) {
            return
        }

        val lastStopCharIndex = findLastStopCharIndex()

        if (lastStopCharIndex <= 0) {
            return
        }

        val speechEndIndex = lastStopCharIndex + 1

        val speech = queuedSpeech.substring(0, speechEndIndex)

        speakInternal(speech)

        if (lastStopCharIndex < queuedSpeech.length - 1) {
            queuedSpeech = queuedSpeech.substring(speechEndIndex, queuedSpeech.length)
        } else {
            queuedSpeech = ""
        }
    }

    override fun flushSpeech() {
        speakInternal(queuedSpeech)
        queuedSpeech = ""
    }

    override fun setMuted(isMuted: Boolean) {
        isMute = isMuted
        if (isMute) {
            stop()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            hasInit = true
            if (pendingText != null) {
                speakInternal(pendingText)
                pendingText = null
            }
        } else {
            Toast.makeText(context, "Failed to start text-to-speech", Toast.LENGTH_LONG).show()
        }
    }

    private fun speakInternal(text: String?) {
        if (isMute) {
            return
        }

        if (tts == null || !hasInit) {
            throw Exception("Must call `start` and wait for init before trying to speak.")
        }
        if (text != null) {
            val utteranceId = UUID.randomUUID().toString()
            tts?.speak(text, TextToSpeech.QUEUE_ADD, null, utteranceId)
        }
    }

    private fun findLastStopCharIndex(): Int {
        val stopChars = arrayOf('.', ':', '!', '?')
        var lastStopCharIndex = -1
        for (char in stopChars) {
            val lastIndex = queuedSpeech.lastIndexOf(char)
            lastStopCharIndex = maxOf(lastStopCharIndex, lastIndex)
        }
        return lastStopCharIndex
    }
}

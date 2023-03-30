package tesler.will.chatassistant.speechoutput

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast

class SpeechOutputManager(private val context: Context) : ISpeechOutputManager,
    TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var pendingText: String? = null
    private var hasInit: Boolean = false

    override fun init() {
        if (tts == null) {
            tts = TextToSpeech(context, this)
        } else {
            Log.w("Speech Output", "TTS already started")
        }
    }

    override fun destroy() {
        tts?.shutdown()
        tts = null
        pendingText = null
        hasInit = false
    }

    override fun stop() {
        tts?.stop()
        pendingText = null
    }

    override fun speak(text: String) {
        if (tts == null) {
            throw Exception("Must call `start` before trying to speak.")
        }

        if (hasInit) {
            speakInternal(text)
        } else {
            pendingText = text
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
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }
}

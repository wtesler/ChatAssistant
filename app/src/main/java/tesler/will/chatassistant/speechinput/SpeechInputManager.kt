package tesler.will.chatassistant.speechinput

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import tesler.will.chatassistant.chat.IChatManager
import tesler.will.chatassistant.speechinput.ISpeechInputManager.Listener
import tesler.will.chatassistant.speechinput.listener.SpeechListener

class SpeechInputManager(
    private val context: Context,
    private val chatManager: IChatManager
) : ISpeechInputManager {

    private var speechRecognizer: SpeechRecognizer? = null

    private var isStarted: Boolean? = null
    private var isFinished: Boolean? = null
    private var isListening: Boolean = false
    private var text: String? = null
    private var amplitude: Float? = null
    private var errorCode: Int? = null

    private val listeners = mutableListOf<Listener>()

    override fun init() {
        if (speechRecognizer != null) {
            throw Exception("Already started. You must call stop first.")
        }

        if (!isAvailable()) {
            for (listener in listeners) {
                listener.onError(-1)
            }
            return
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

        speechRecognizer?.setRecognitionListener(
            SpeechListener(
                ::onReady,
                ::onStarted,
                ::onFinished,
                ::onText,
                ::onAmplitude,
                ::onError
            )
        )
    }

    override fun destroy() {
        if (speechRecognizer != null) {
            speechRecognizer?.destroy()
            speechRecognizer = null
        }
    }

    override fun start() {
        if (speechRecognizer == null || !isAvailable()) {
            Toast.makeText(context, "Speech recognition unavailable.", Toast.LENGTH_LONG).show()
            return
        }

        if (isListening) {
            Toast.makeText(context, "Already listening.", Toast.LENGTH_LONG).show()
            return
        }

        chatManager.clearErrorChats()

        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)

            // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // putExtra(RecognizerIntent.EXTRA_ENABLE_FORMATTING, RecognizerIntent.FORMATTING_OPTIMIZE_LATENCY)
                // putExtra(RecognizerIntent.EXTRA_HIDE_PARTIAL_TRAILING_PUNCTUATION, true)
            // }
        }

        speechRecognizer?.startListening(recognizerIntent)

        isListening = true
        for (listener in listeners) {
            listener.onListeningStarted()
        }
    }

    override fun stop() {
        text = null
        isStarted = null
        isFinished = null
        amplitude = null
        errorCode = null
        isListening = false

        for (listener in listeners) {
            listener.onText(null)
        }

        speechRecognizer?.cancel()
    }

    override fun isAvailable(): Boolean {
        return SpeechRecognizer.isRecognitionAvailable(context)
    }

    override fun isListening(): Boolean {
        return isListening
    }

    override fun addListener(listener: Listener) {
        listeners.add(listener)
        if (isStarted != null) {
            listener.onSpeechStarted()
        }
        if (text != null) {
            listener.onText(text)
        }
        if (amplitude != null) {
            listener.onAmplitude(amplitude)
        }
        if (isFinished != null && text != null) {
            listener.onSpeechFinished()
        }
        if (errorCode != null) {
            listener.onError(errorCode)
        }
    }

    override fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    private fun onReady() {

    }

    private fun onStarted() {
        isStarted = true
        for (listener in listeners) {
            listener.onSpeechStarted()
        }
    }

    private fun onFinished() {
        isFinished = true
        isListening = false
        for (listener in listeners) {
            listener.onSpeechFinished()
        }
    }

    private fun onText(t: String) {
        text = t
        for (listener in listeners) {
            listener.onText(t)
        }
    }

    private fun onAmplitude(f: Float) {
        amplitude = f
        for (listener in listeners) {
            listener.onAmplitude(f)
        }
    }

    private fun onError(code: Int) {
        errorCode = code
        isListening = false
        for (listener in listeners) {
            listener.onError(code)
        }
    }
}

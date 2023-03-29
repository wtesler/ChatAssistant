package tesler.will.chatassistant.speech

import android.content.Context
import android.content.Intent
import android.os.Build
import android.speech.RecognizerIntent
import android.speech.RecognizerIntent.FORMATTING_OPTIMIZE_QUALITY
import android.speech.SpeechRecognizer
import android.widget.Toast
import tesler.will.chatassistant.speech.ISpeechManager.*

class SpeechManager(private val context: Context) : ISpeechManager {

    private var speechRecognizer: SpeechRecognizer? = null

    private var isStarted: Boolean? = null
    private var isFinished: Boolean? = null
    private var text: String? = null
    private var amplitude: Float? = null
    private var errorCode: Int? = null

    private val listeners = mutableListOf<Listener>()

    override fun start() {
        if (speechRecognizer != null) {
            throw Exception("Already started. You must call stop first.")
        }

        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            Toast.makeText(context, "Speech recognition unavailable.", Toast.LENGTH_LONG).show()
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

        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                putExtra(RecognizerIntent.EXTRA_ENABLE_FORMATTING, FORMATTING_OPTIMIZE_QUALITY)
                putExtra(RecognizerIntent.EXTRA_HIDE_PARTIAL_TRAILING_PUNCTUATION, true)
            }
        }

        speechRecognizer?.startListening(recognizerIntent)
    }

    override fun stop() {
        if (speechRecognizer != null) {
            speechRecognizer?.destroy()
            speechRecognizer = null
        }

        isStarted = null
        isFinished = null
        text = null
        amplitude = null
        errorCode = null
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
        if (isFinished != null) {
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
        for (listener in listeners) {
            listener.onError(code)
        }
    }
}

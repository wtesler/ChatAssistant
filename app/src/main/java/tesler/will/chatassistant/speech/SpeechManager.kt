package tesler.will.chatassistant.speech

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
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

    private val startedListeners = mutableListOf<SpeechStartedListener>()
    private val finishedListeners = mutableListOf<SpeechFinishedListener>()
    private val textListeners = mutableListOf<SpeechTextListener>()
    private val amplitudeListeners = mutableListOf<SpeechAmplitudeListener>()
    private val errorListeners = mutableListOf<SpeechErrorListener>()

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
        }

        speechRecognizer?.startListening(recognizerIntent)
    }

    override fun stop() {
        if (speechRecognizer != null) {
            speechRecognizer?.destroy()
            speechRecognizer = null
        }
    }

    override fun clear() {
        stop()
        isStarted = null
        isFinished = null
        text = null
        amplitude = null
        errorCode = null
    }

    override fun addStartedListener(listener: SpeechStartedListener) {
        startedListeners.add(listener)
        if (isStarted != null) {
            listener.onSpeechStarted()
        }
    }

    override fun addFinishedListener(listener: SpeechFinishedListener) {
        finishedListeners.add(listener)
        if (isFinished != null) {
            listener.onSpeechFinished()
        }
    }

    override fun addTextListener(listener: SpeechTextListener) {
        textListeners.add(listener)
        if (text != null) {
            listener.onText(text)
        }
    }

    override fun addAmplitudeListener(listener: SpeechAmplitudeListener) {
        amplitudeListeners.add(listener)
        if (amplitude != null) {
            listener.onAmplitude(amplitude)
        }
    }

    override fun addErrorListener(listener: SpeechErrorListener) {
        errorListeners.add(listener)
        if (errorCode != null) {
            listener.onError(errorCode)
        }
    }

    override fun removeStartedListener(listener: SpeechStartedListener) {
        startedListeners.remove(listener)
    }

    override fun removeFinishedListener(listener: SpeechFinishedListener) {
        finishedListeners.remove(listener)
    }

    override fun removeTextListener(listener: SpeechTextListener) {
        textListeners.remove(listener)
    }

    override fun removeAmplitudeListener(listener: SpeechAmplitudeListener) {
        amplitudeListeners.remove(listener)
    }

    override fun removeErrorListener(listener: SpeechErrorListener) {
        errorListeners.remove(listener)
    }

    private fun onReady() {

    }

    private fun onStarted() {
        isStarted = true
        for (listener in startedListeners) {
            listener.onSpeechStarted()
        }
    }

    private fun onFinished() {
        isFinished = true
        for (listener in finishedListeners) {
            listener.onSpeechFinished()
        }
    }

    private fun onText(t: String) {
        text = t
        for (listener in textListeners) {
            listener.onText(t)
        }
    }

    private fun onAmplitude(f: Float) {
        amplitude = f
        for (listener in amplitudeListeners) {
            listener.onAmplitude(f)
        }
    }

    private fun onError(code: Int) {
        errorCode = code
        for (listener in errorListeners) {
            listener.onError(code)
        }
    }
}
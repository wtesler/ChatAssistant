package tesler.will.chatassistant.speech

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast

class SpeechManager(private val context: Context) {

    private var speechRecognizer: SpeechRecognizer? = null

    private var text: String? = null
    private var isFinished: Boolean? = null
    private var errorCode: Int? = null

    private val textListeners = HashMap<(String?) -> Unit, (String?) -> Unit>()
    private val finishedListeners = HashMap<() -> Unit, () -> Unit>()
    private val errorListeners = HashMap<(Int?) -> Unit, (Int?) -> Unit>()

    fun start() {
        if (speechRecognizer != null) {
            throw Exception("Already started. You must call stop first.")
        }

        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            Toast.makeText(context, "Speech recognition unavailable.", Toast.LENGTH_LONG).show()
            return
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

        speechRecognizer?.setRecognitionListener(SpeechListener(::onText, ::onFinished, ::onError))

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

    fun stop() {
        if (speechRecognizer != null) {
            speechRecognizer?.destroy()
            speechRecognizer = null
        }
    }

    fun clear() {
        stop()
        text = null;
        isFinished = null;
        errorCode = null;
    }

    fun addTextListener(listener: (String?) -> Unit) {
        textListeners[listener] = listener;
        if (text != null) {
            listener(text)
        }
    }

    fun addFinishedListener(listener: () -> Unit) {
        finishedListeners[listener] = listener;
        if (isFinished != null) {
            listener()
        }
    }

    fun addErrorListener(listener: (Int?) -> Unit) {
        errorListeners[listener] = listener;
        if (errorCode != null) {
            listener(errorCode)
        }
    }

    fun removeTextListener(listener: (String?) -> Unit) {
        textListeners.remove(listener);
    }

    fun removeFinishedListener(listener: () -> Unit) {
        finishedListeners.remove(listener);
    }

    fun removeErrorListener(listener: (Int?) -> Unit) {
        errorListeners.remove(listener);
    }

    private fun onText(t: String) {
        text = t
        for (listener in textListeners.values) {
            listener(t)
        }
    }

    private fun onFinished() {
        isFinished = true
        for (listener in finishedListeners.values) {
            listener()
        }
    }

    private fun onError(code: Int) {
        errorCode = code
        for (listener in errorListeners.values) {
            listener(code)
        }
    }
}
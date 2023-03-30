package tesler.will.chatassistant.speechinput.listener

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer.RESULTS_RECOGNITION
import android.util.Log

class SpeechListener(
    val onReady: () -> Unit,
    val onStarted: () -> Unit,
    val onFinished: () -> Unit,
    val onText: (String) -> Unit,
    val onAmplitude: (Float) -> Unit,
    val onSpeechError: (Int) -> Unit
) : RecognitionListener {

    override fun onReadyForSpeech(params: Bundle?) {
        onReady()
    }

    override fun onBeginningOfSpeech() {
        onStarted()
    }

    override fun onRmsChanged(rmsdB: Float) {
        onAmplitude(rmsdB)
    }

    override fun onBufferReceived(buffer: ByteArray?) {
    }

    override fun onEndOfSpeech() {
    }

    override fun onError(errorCode: Int) {
        Log.e("Speech Listener", "Error code: $errorCode")
        onSpeechError(errorCode)
    }

    override fun onResults(results: Bundle?) {
        if (results == null) {
            onFinished()
            return
        }
        val stringList = results.getStringArrayList(RESULTS_RECOGNITION) ?: ArrayList<String>()
        val words = if (stringList.size > 0) stringList[0] else ""
        onText(words)
        onFinished()
    }

    override fun onPartialResults(partialResults: Bundle?) {
        if (partialResults == null) {
            return
        }
        val stringList = partialResults.getStringArrayList(RESULTS_RECOGNITION) ?: return
        val words = if (stringList.size > 0) stringList[0] else ""
        onText(words)
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
    }
}
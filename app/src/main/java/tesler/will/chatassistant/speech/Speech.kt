package tesler.will.chatassistant.speech

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer.RESULTS_RECOGNITION

public class Listener(
    val onWords: (String) -> Unit,
    val onFinished: () -> Unit,
    val onText: (String) -> Unit
) : RecognitionListener {

    override fun onReadyForSpeech(params: Bundle?) {
    }

    override fun onBeginningOfSpeech() {
        println("BEGINNING OF SPEECH")
    }

    override fun onRmsChanged(rmsdB: Float) {
    }

    override fun onBufferReceived(buffer: ByteArray?) {
    }

    override fun onEndOfSpeech() {
        println("END OF SPEECH")
    }

    override fun onError(error: Int) {
        println("Error code: $error")
    }

    override fun onResults(results: Bundle?) {
        if (results == null) {
            return
        }
        val stringList = results.getStringArrayList(RESULTS_RECOGNITION) ?: return;
        val words = if (stringList.size > 0) stringList[0] else ""
        onText(words)
        onFinished()
        println("ON RESULTS")
    }

    override fun onPartialResults(partialResults: Bundle?) {
        if (partialResults == null) {
            return
        }
        val stringList = partialResults.getStringArrayList(RESULTS_RECOGNITION) ?: return;
        val words = if (stringList.size > 0) stringList[0] else ""
        onWords(words)
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
    }
}
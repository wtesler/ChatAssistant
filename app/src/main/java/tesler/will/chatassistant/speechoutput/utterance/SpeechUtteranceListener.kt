package tesler.will.chatassistant.speechoutput.utterance

import android.speech.tts.UtteranceProgressListener

class SpeechUtteranceListener(val checkIfSpeaking: () -> Unit) : UtteranceProgressListener() {
    override fun onStart(utteranceId: String?) {
        checkIfSpeaking()
    }

    override fun onDone(utteranceId: String?) {
        checkIfSpeaking()
    }

    @Deprecated("Deprecated in Java")
    override fun onError(utteranceId: String?) {
        checkIfSpeaking()
    }
}
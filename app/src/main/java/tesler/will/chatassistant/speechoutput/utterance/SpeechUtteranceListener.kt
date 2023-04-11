package tesler.will.chatassistant.speechoutput.utterance

import android.speech.tts.UtteranceProgressListener

class SpeechUtteranceListener(val onUtteranceStart: () -> Unit, val onUtteranceEnd: () -> Unit) :
    UtteranceProgressListener() {

    override fun onStart(utteranceId: String?) {
        onUtteranceStart()
    }

    override fun onDone(utteranceId: String?) {
        onUtteranceEnd()
    }

    @Deprecated("Deprecated in Java")
    override fun onError(utteranceId: String?) {
        onUtteranceEnd()
    }
}
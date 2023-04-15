package tesler.will.chatassistant.speechoutput.utterance

import android.speech.tts.UtteranceProgressListener

class SpeechUtteranceListener(
    val onUtteranceStart: () -> Unit,
    val onUtteranceEnd: () -> Unit,
    val onUtteranceProgress: (utteranceId: String?, start: Int, end: Int) -> Unit
) :
    UtteranceProgressListener() {

    override fun onStart(utteranceId: String?) {
        onUtteranceStart()
    }

    override fun onDone(utteranceId: String?) {
        onUtteranceEnd()
    }

    override fun onRangeStart(utteranceId: String?, start: Int, end: Int, frame: Int) {
        onUtteranceProgress(utteranceId, start, end)
    }

    @Deprecated("Deprecated in Java", ReplaceWith("onUtteranceEnd()"))
    override fun onError(utteranceId: String?) {
        onUtteranceEnd()
    }
}
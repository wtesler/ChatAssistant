package tesler.will.chatassistant.speechoutput

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Toast
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager.Listener
import java.util.UUID


class SpeechOutputManager(private val context: Context) : ISpeechOutputManager, OnInitListener {

    private var tts: TextToSpeech? = null
    private var pendingText: String? = null
    private var hasInit: Boolean = false
    private var queuedSpeech: String = ""

    private data class SpeechRecord(val utteranceId: String, val text: String)

    private var history = ArrayList<SpeechRecord>()

    private val listeners = mutableListOf<Listener>()

    override fun init() {
        if (tts == null) {
            tts = TextToSpeech(context, this)
            tts?.setOnUtteranceProgressListener(ProgressListener(::onRangeStart))
        } else {
            Log.w("Speech Output", "TTS already started")
        }
    }

    override fun reset() {
        history.clear()
    }

    override fun destroy() {
        tts?.shutdown()
        tts = null
        pendingText = null
        hasInit = false
        queuedSpeech = ""
        history.clear()
    }

    override fun stop() {
        tts?.stop()
        pendingText = null
        queuedSpeech = ""
        history.clear()
    }

    override fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    override fun queueSpeech(text: String) {
        queuedSpeech += text

        if (!hasInit) {
            return
        }

        if (tts!!.isSpeaking) {
            return
        }

        val lastPeriod = queuedSpeech.lastIndexOf('.')
        if (lastPeriod <= 0) {
            return
        }

        val speech = queuedSpeech.substring(0, lastPeriod + 1)

        speakInternal(speech)

        if (lastPeriod < queuedSpeech.length - 1) {
            queuedSpeech = queuedSpeech.substring(lastPeriod + 1, queuedSpeech.length)
        } else {
            queuedSpeech = ""
        }
    }

    override fun flushSpeech() {
        speakInternal(queuedSpeech)
        queuedSpeech = ""
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
        if (tts == null || !hasInit) {
            throw Exception("Must call `start` and wait for init before trying to speak.")
        }
        if (text != null) {
            val utteranceId = UUID.randomUUID().toString()
            history.add(SpeechRecord(utteranceId, text))
            tts?.speak(text, TextToSpeech.QUEUE_ADD, null, utteranceId)
        }
    }

    private fun onRangeStart(utteranceId: String?, start: Int, end: Int, frame: Int) {
        var total = 0
        for (record in history) {
            total += record.text.length
        }

        var position = 0
        for (record in history) {
            if (record.utteranceId != utteranceId) {
                position += record.text.length
            } else {
                position += start
                break
            }
        }

        val progress = position.toFloat() / total

        for (listener in listeners) {
            listener.onSpeechProgress(progress)
        }
    }

    private class ProgressListener(
        val rangeStartFunc: (utteranceId: String?, start: Int, end: Int, frame: Int) -> Unit
    ) : UtteranceProgressListener() {

        override fun onRangeStart(utteranceId: String?, start: Int, end: Int, frame: Int) {
            rangeStartFunc(utteranceId, start, end, frame)
        }

        override fun onStart(utteranceId: String?) {
        }

        override fun onDone(utteranceId: String?) {
        }

        @Deprecated("Deprecated in Java")
        override fun onError(utteranceId: String?) {
        }
    }
}

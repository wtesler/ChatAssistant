package tesler.will.chatassistant.speechoutput

import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.media.AudioManager.STREAM_MUSIC
import android.os.Handler
import android.os.Looper
import android.provider.Settings.System.CONTENT_URI
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.speech.tts.TextToSpeech.QUEUE_ADD
import android.speech.tts.Voice
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager.Listener
import tesler.will.chatassistant.speechoutput.utterance.SpeechUtteranceListener
import java.util.*
import kotlin.math.ceil


class SpeechOutputManager(private val context: Context) : ISpeechOutputManager, OnInitListener,
    ContentObserver(Handler(Looper.getMainLooper())) {

    private var tts: TextToSpeech? = null
    private var pendingText: String? = null
    private var hasInit = false
    private var queuedSpeech = ""
    private var isMute = false
    private var voiceString: String? = null
    private var speedFloat: Float? = null
    private var mediaVolume: Int = 0

    private val listeners = mutableListOf<Listener>()

    private val speechListener: SpeechUtteranceListener =
        SpeechUtteranceListener(::onUtteranceStart, ::onUtteranceEnd)

    private var audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    override fun init() {
        if (tts == null) {
            tts = TextToSpeech(context, this)
        } else {
            Log.w("Speech Output", "TTS already started")
        }

        mediaVolume = audioManager.getStreamVolume(STREAM_MUSIC)

        context.contentResolver.registerContentObserver(CONTENT_URI, true, this)
        tts!!.setOnUtteranceProgressListener(speechListener)
    }

    override fun isInit(): Boolean {
        return hasInit
    }

    override fun destroy() {
        if (tts != null && tts!!.isSpeaking) {
            for (listener in listeners) {
                listener.onSpeechEnded()
            }
        }
        if (isMute) {
            setVolume(mediaVolume)
        }
        tts?.setOnUtteranceProgressListener(null)
        tts?.shutdown()
        tts = null
        pendingText = null
        hasInit = false
        queuedSpeech = ""
        voiceString = null
        speedFloat = null
        isMute = false
        context.contentResolver.unregisterContentObserver(this)
    }

    override fun stop() {
        if (tts != null && tts!!.isSpeaking) {
            for (listener in listeners) {
                listener.onSpeechEnded()
            }
        }
        tts?.stop()
        pendingText = null
        queuedSpeech = ""
        isMute = false
    }

    override fun addListener(listener: Listener) {
        listeners.add(listener)
        if (hasInit) {
            listener.onTtsReady()
        }
    }

    override fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    override fun speak(text: String, queueType: Int) {
        if (!hasInit) {
            return
        }
        speakInternal(text, queueType)
    }

    override fun isSpeaking(): Boolean {
        return tts != null && tts!!.isSpeaking
    }

    override fun queueSpeech(text: String) {
        queuedSpeech += text

        if (!hasInit) {
            return
        }

        if (tts!!.isSpeaking) {
            return
        }

        val lastStopCharIndex = findLastStopCharIndex()

        if (lastStopCharIndex <= 0) {
            return
        }

        val speechEndIndex = lastStopCharIndex + 1

        val speech = queuedSpeech.substring(0, speechEndIndex)

        speakInternal(speech)

        if (lastStopCharIndex < queuedSpeech.length - 1) {
            queuedSpeech = queuedSpeech.substring(speechEndIndex, queuedSpeech.length)
        } else {
            queuedSpeech = ""
        }
    }

    override fun flushSpeech() {
        speakInternal(queuedSpeech)
        queuedSpeech = ""
    }

    override fun setMuted(isMuted: Boolean) {
        isMute = isMuted

        var unmutedVolume = mediaVolume
        if (unmutedVolume == 0) {
            unmutedVolume = ceil(audioManager.getStreamMaxVolume(STREAM_MUSIC) / 2.0).toInt()
        }

        val volume = if (isMuted) 0 else unmutedVolume
        setVolume(volume)
    }

    override fun getDefaultVoice(): Voice {
        if (tts == null) {
            throw Exception("Must call init before getting default voice.")
        }
        return tts!!.defaultVoice
    }

    override fun getVoices(): MutableSet<Voice> {
        if (tts == null) {
            throw Exception("Must call init before getting voices.")
        }
        return tts!!.voices
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            hasInit = true

            if (pendingText != null) {
                speakInternal(pendingText)
                pendingText = null
            }
            for (listener in listeners) {
                listener.onTtsReady()
            }
        } else {
            Toast.makeText(context, "Failed to start text-to-speech", LENGTH_LONG).show()
        }
    }

    override fun setVoice(voice: String) {
        val voices = getVoices()
        for (v in voices) {
            if (v.name == voice) {
                tts!!.voice = v
                break
            }
        }
    }

    override fun setSpeed(speed: Float) {
        speedFloat = speed
        tts!!.setSpeechRate(speed)
    }

    override fun onChange(selfChange: Boolean) {
        val currentVolume = audioManager.getStreamVolume(STREAM_MUSIC)
        if (!isMute) {
            mediaVolume = currentVolume
        } else {
            if (currentVolume > 0) {
                setMuted(false)
            }
        }
    }

    private fun speakInternal(text: String?, queueType: Int = QUEUE_ADD) {
        if (tts == null || !hasInit) {
            throw Exception("Must call `start` and wait for init before trying to speak.")
        }
        if (text != null) {
            // val bundle: Bundle = Bundle()
            // bundle.putFloat(Engine.KEY_PARAM_VOLUME, if (isMute) 0f else 1f)
            val utteranceId = UUID.randomUUID().toString()
            tts?.speak(text, queueType, null, utteranceId)
        }
    }

    private fun onUtteranceStart() {
        for (listener in listeners) {
            listener.onSpeechInProgress()
        }
    }

    private fun onUtteranceEnd() {
        for (listener in listeners) {
            listener.onSpeechEnded()
        }
    }

    private fun setVolume(volume: Int) {
        try {
            audioManager.setStreamVolume(STREAM_MUSIC, volume, 0)
        } catch (e: SecurityException) {
            Log.w("Speech Output Manager", "Cannot violate DnD")
        }
    }

    private fun findLastStopCharIndex(): Int {
        val stopChars = arrayOf('.', ':', '!', '?')
        var lastStopCharIndex = -1
        for (char in stopChars) {
            val lastIndex = queuedSpeech.lastIndexOf(char)
            lastStopCharIndex = maxOf(lastStopCharIndex, lastIndex)
        }
        return lastStopCharIndex
    }
}

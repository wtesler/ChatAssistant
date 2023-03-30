package tesler.will.chatassistant.speechoutput

interface ISpeechOutputManager {
    fun init()
    fun destroy()
    fun stop()
    fun speak(text: String)
}

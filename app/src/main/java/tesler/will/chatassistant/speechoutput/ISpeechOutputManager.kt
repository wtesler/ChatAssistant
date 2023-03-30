package tesler.will.chatassistant.speechoutput

interface ISpeechOutputManager {
    fun start()
    fun stop()
    fun speak(text: String)
}

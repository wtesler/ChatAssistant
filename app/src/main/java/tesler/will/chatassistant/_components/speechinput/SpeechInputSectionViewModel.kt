package tesler.will.chatassistant._components.speechinput

data class SpeechInputSectionViewModel(
    val state: State = State.ACTIVE,
    val text: String = "",
    val isSpeaking: Boolean = false
)

enum class State {
    ACTIVE,
    LOADING,
    READY,
    TEXT_INPUT
}

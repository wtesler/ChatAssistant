package tesler.will.chatassistant._components.speechinput

data class SpeechInputSectionViewModel(
    val state: State = State.ACTIVE,
    val text: String = ""
)

enum class State {
    ACTIVE,
    LOADING,
    READY
}

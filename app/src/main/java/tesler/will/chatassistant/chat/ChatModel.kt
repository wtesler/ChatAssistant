package tesler.will.chatassistant.chat

import java.util.*

data class ChatModel(
    var text: String = "",
    var state: State = State.CREATING,
    var isUser: Boolean = true,
    val id: String = UUID.randomUUID().toString()
) {
    enum class State {
        CREATING,
        CREATED,
        ERROR
    }


}
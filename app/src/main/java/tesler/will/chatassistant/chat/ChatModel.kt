package tesler.will.chatassistant.chat

import java.util.*

data class ChatModel(
    var text: String = "",
    var state: State = State.CREATED,
    var isUser: Boolean = true,
    var highlightStartIndex: Int = -1,
    var highlightEndIndex: Int = -1,
    var id: String = ""
) {
    enum class State {
        CREATED,
        ERROR
    }

    /**
     * Sets the id for the chat.
     * This should only be called from within the Chat Manager.
     */
    fun setId() {
        id = UUID.randomUUID().toString()
    }
}
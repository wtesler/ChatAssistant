package tesler.will.chatassistant.server.models.chat

import tesler.will.chatassistant.chat.ChatModel

data class ChatUpdateRequest(var chatModels: List<ChatUpdateRequestModel>) {
    companion object {
        fun build(chatModels: List<ChatModel>): ChatUpdateRequest {
            val models = chatModels.filter { x -> x.state != ChatModel.State.ERROR }.map { x ->
                val role = if (x.isUser) "user" else "assistant"
                ChatUpdateRequestModel(role, x.text)
            }
            return ChatUpdateRequest(models)
        }
    }
}

class ChatUpdateRequestModel(val role: String, val content: String)

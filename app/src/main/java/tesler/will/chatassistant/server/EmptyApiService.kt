package tesler.will.chatassistant.server

import tesler.will.chatassistant.server.models.chat.ChatUpdateRequest
import tesler.will.chatassistant.server.models.chat.ChatUpdateResponse

class EmptyApiService : ApiService {
    override suspend fun updateChat(request: ChatUpdateRequest): ChatUpdateResponse {
        return ChatUpdateResponse("Test Message")
    }
}
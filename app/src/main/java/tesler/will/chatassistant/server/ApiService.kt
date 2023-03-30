package tesler.will.chatassistant.server

import retrofit2.http.Body
import retrofit2.http.POST
import tesler.will.chatassistant.server.models.chat.ChatUpdateRequest
import tesler.will.chatassistant.server.models.chat.ChatUpdateResponse

interface ApiService {
    @POST("updateChatOpen")
    suspend fun updateChat(@Body request: ChatUpdateRequest): ChatUpdateResponse
}

package tesler.will.chatassistant.server

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Streaming
import tesler.will.chatassistant.server.models.chat.ChatUpdateRequest

interface ApiService {
    @POST("updateChatOpen")
    @Streaming
    suspend fun updateChat(@Body request: ChatUpdateRequest): ResponseBody
}

package tesler.will.chatassistant.server

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Streaming
import tesler.will.chatassistant.server.models.chat.ChatUpdateRequest

interface ApiService {
    @POST("updateChatAuthorized")
    @Streaming
    suspend fun updateChat(@Body request: ChatUpdateRequest): ResponseBody

    @POST("warmupAuthorized")
    suspend fun warmup(): ResponseBody
}

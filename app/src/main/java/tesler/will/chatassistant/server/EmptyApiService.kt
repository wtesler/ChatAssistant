package tesler.will.chatassistant.server

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import tesler.will.chatassistant.server.models.chat.ChatUpdateRequest

class EmptyApiService : ApiService {
    override suspend fun updateChat(request: ChatUpdateRequest): ResponseBody {
        return "Test Message".toResponseBody("text/plain".toMediaTypeOrNull())
    }
}

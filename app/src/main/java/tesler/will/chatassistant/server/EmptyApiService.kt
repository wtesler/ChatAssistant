package tesler.will.chatassistant.server

import okhttp3.MediaType
import okhttp3.ResponseBody
import tesler.will.chatassistant.server.models.chat.ChatUpdateRequest

class EmptyApiService : ApiService {
    override suspend fun updateChat(request: ChatUpdateRequest): ResponseBody {
        return ResponseBody.create(MediaType.get("text/plain"), "Test Message")
    }

    override suspend fun warmup(): ResponseBody {
        return ResponseBody.create(MediaType.get("text/plain"), "Test Response")
    }
}

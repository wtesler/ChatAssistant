package tesler.will.chatassistant.server.interceptor

import android.net.TrafficStats
import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.json.JSONObject
import tesler.will.chatassistant.auth.IAuthManager

class ServerInterceptor(private val authManager: IAuthManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        TrafficStats.setThreadStatsTag(Thread.currentThread().id.toInt())

        var reqBuilder = chain.request().newBuilder()

        var idToken: String
        runBlocking {
            idToken = authManager.fetchIdToken()
        }
        reqBuilder = reqBuilder.header("Authorization", "Bearer $idToken")

        val req = reqBuilder.build()

        var response: Response
        try {
            response = chain.proceed(req)
        } catch (e: Exception) {
            Log.e("Server Interceptor", e.message, e)
            response = Response.Builder()
                .request(req)
                .protocol(Protocol.HTTP_2)
                .message(e.message ?: "Failure along request chain.")
                .body(ResponseBody.create(MediaType.get("application/json"), "{}"))
                .code(500)
                .build()
        }

        if (!response.isSuccessful) {
            try {
                val errorString = response.peekBody(5000000).string()
                val errorJson = JSONObject(errorString)
                val message = errorJson.getString("message")
                if (message.isNotEmpty()) {
                    response = response.newBuilder()
                        .message(message)
                        .build()
                }
            } catch (_: Exception) {
                // Intentionally Empty
            }
        }

        return response
    }
}

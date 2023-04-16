package tesler.will.chatassistant.server.interceptor

import android.net.TrafficStats
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject
import tesler.will.chatassistant.auth.IAuthManager

class ServerInterceptor(private val authManager: IAuthManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        TrafficStats.setThreadStatsTag(Thread.currentThread().id.toInt())


        var reqBuilder = chain.request().newBuilder()

        val idToken = authManager.getIdToken()
        if (idToken != null) {
            reqBuilder = reqBuilder.header("Authorization", "Bearer $idToken")
        }

        val req = reqBuilder.build()

        var response = chain.proceed(req)

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

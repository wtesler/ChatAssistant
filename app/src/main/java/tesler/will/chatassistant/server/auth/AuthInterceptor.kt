package tesler.will.chatassistant.server.auth

import android.net.TrafficStats
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        TrafficStats.setThreadStatsTag(Thread.currentThread().id.toInt())

        var req = chain.request()
        // val url = req.url().newBuilder().addQueryParameter("APPID", "your_key_here").build()
        val url = req.url().newBuilder().build()
        req = req.newBuilder().url(url).build()
        return chain.proceed(req)
    }
}

package tesler.will.chatassistant.modules.server

import android.content.Context
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tesler.will.chatassistant.BuildConfig
import tesler.will.chatassistant.R
import tesler.will.chatassistant.auth.IAuthManager
import tesler.will.chatassistant.server.ApiService
import tesler.will.chatassistant.server.interceptor.ServerInterceptor
import tesler.will.chatassistant.server.interceptor.UnsafeDevHttpClientBuilder
import java.util.concurrent.TimeUnit

val serverModule = module {
    factory { provideApiService(get()) }
    single { provideRetrofit(androidContext(), get()) }
}

fun provideApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}

fun provideRetrofit(context: Context, authManager: IAuthManager): Retrofit {
    var baseUrl = context.getString(R.string.production_server_url)
    var builder = OkHttpClient().newBuilder()
    if (BuildConfig.DEBUG) {
        baseUrl = context.getString(R.string.development_server_url)
        builder = UnsafeDevHttpClientBuilder.builder()
    }

    val httpClient = builder
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(ServerInterceptor(authManager))
        .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

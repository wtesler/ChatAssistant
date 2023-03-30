package tesler.will.chatassistant.modules.server

import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tesler.will.chatassistant.BuildConfig
import tesler.will.chatassistant.server.ApiService
import tesler.will.chatassistant.server.auth.AuthInterceptor
import tesler.will.chatassistant.server.auth.UnsafeDevHttpClientBuilder

val serverModule = module {
    factory { provideApiService(get()) }

    factory { AuthInterceptor() }
    factory { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
}

fun provideApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}

fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
    return OkHttpClient().newBuilder().addInterceptor(authInterceptor).build()
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    val baseUrl =
        if (BuildConfig.DEBUG) "https://10.0.0.109:8080/" else "https://will-chat-assistant.uc.r.appspot.com/"

    val httpClient = if (BuildConfig.DEBUG) UnsafeDevHttpClientBuilder.build() else okHttpClient

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

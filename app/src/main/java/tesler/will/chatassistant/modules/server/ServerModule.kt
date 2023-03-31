package tesler.will.chatassistant.modules.server

import android.content.Context
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tesler.will.chatassistant.BuildConfig
import tesler.will.chatassistant.R
import tesler.will.chatassistant.server.ApiService
import tesler.will.chatassistant.server.interceptor.ServerInterceptor
import tesler.will.chatassistant.server.interceptor.UnsafeDevHttpClientBuilder
import java.util.concurrent.TimeUnit

val serverModule = module {
    factory { provideApiService(get()) }
    single { provideRetrofit(get(), androidContext()) }
    factory { provideOkHttpClient() }
}

fun provideApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}

fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient().newBuilder()
        .addInterceptor(ServerInterceptor())
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()
}

fun provideRetrofit(okHttpClient: OkHttpClient, context: Context): Retrofit {
    var httpClient = okHttpClient
    var baseUrl = context.getString(R.string.production_server_url)

    if (BuildConfig.DEBUG) {
        baseUrl = context.getString(R.string.development_server_url)
        httpClient = UnsafeDevHttpClientBuilder.build()
    }

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

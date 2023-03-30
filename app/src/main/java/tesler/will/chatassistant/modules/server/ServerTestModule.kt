package tesler.will.chatassistant.modules.server

import org.koin.dsl.module
import tesler.will.chatassistant.server.ApiService
import tesler.will.chatassistant.server.EmptyApiService

val serverTestModule = module {
    single<ApiService> { EmptyApiService() }
}

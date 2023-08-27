package tesler.will.chatassistant.modules.main

import org.koin.dsl.module
import tesler.will.chatassistant.auth.AuthManager
import tesler.will.chatassistant.auth.IAuthManager
import tesler.will.chatassistant.modules.chat.chatModule
import tesler.will.chatassistant.modules.server.serverModule
import tesler.will.chatassistant.modules.speech.speechModule
import tesler.will.chatassistant.stack.BackStackManager
import tesler.will.chatassistant.stack.IBackStackManager
import tesler.will.chatassistant.store.ISettingsService
import tesler.will.chatassistant.store.SettingsService
import tesler.will.chatassistant.warmup.IWarmupManager
import tesler.will.chatassistant.warmup.WarmupManager

val mainModule = module {
    includes(speechModule, chatModule, serverModule)

    single<ISettingsService> { SettingsService(get()) }
    single<IBackStackManager> { BackStackManager() }
    single<IAuthManager> { AuthManager() }
    single<IWarmupManager> { WarmupManager(get()) }
}

package tesler.will.chatassistant.modules.settings

import org.koin.dsl.module
import tesler.will.chatassistant.modules.speech.speechModule
import tesler.will.chatassistant.store.ISettingsService
import tesler.will.chatassistant.store.SettingsService

val settingsModule = module {
    includes(speechModule)

    single<ISettingsService> { SettingsService(get()) }
}

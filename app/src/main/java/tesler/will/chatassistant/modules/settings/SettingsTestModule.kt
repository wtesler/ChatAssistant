package tesler.will.chatassistant.modules.settings

import org.koin.dsl.module
import tesler.will.chatassistant.modules.speech.speechTestModule
import tesler.will.chatassistant.store.EmptySettingsService
import tesler.will.chatassistant.store.ISettingsService

val settingsTestModule = module {
    includes(speechTestModule)

    single<ISettingsService> { EmptySettingsService() }
}

package tesler.will.chatassistant

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import tesler.will.chatassistant.strictmode.StrictModeHelper

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            StrictModeHelper.setup()
        }

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
        }
    }
}
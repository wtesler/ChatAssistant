package tesler.will.chatassistant.activities

import android.os.Bundle
import android.os.StrictMode
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import tesler.will.chatassistant.BuildConfig
import tesler.will.chatassistant.R
import tesler.will.chatassistant._components.Main
import tesler.will.chatassistant.modules.chat.chatModule
import tesler.will.chatassistant.modules.main.mainModule
import tesler.will.chatassistant.modules.speech.speechModule

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults()
        }

        loadKoinModules(listOf(speechModule, chatModule))

        setContent {
            Main(this)
        }

        setTheme(R.style.Theme_ChatAssistant)

        window.setLayout(MATCH_PARENT, MATCH_PARENT)
    }

    override fun onDestroy() {
        unloadKoinModules(mainModule)
        super.onDestroy()
    }

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAndRemoveTask()
    }
}

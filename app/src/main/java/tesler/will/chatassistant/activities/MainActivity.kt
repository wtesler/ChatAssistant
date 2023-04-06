package tesler.will.chatassistant.activities

import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import tesler.will.chatassistant.R
import tesler.will.chatassistant._components.Main
import tesler.will.chatassistant.modules.main.mainModule

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadKoinModules(mainModule)

        setContent {
            Main(this)
        }

        setTheme(R.style.Theme_ChatAssistant)

        window.setLayout(MATCH_PARENT, MATCH_PARENT)
    }

    override fun onPause() {
        super.onPause()
        finishAndRemoveTask()
    }

    override fun finish() {
        unload()
        super.finish()
    }

    override fun finishAndRemoveTask() {
        unload()
        super.finishAndRemoveTask()
    }

    private fun unload() {
        unloadKoinModules(mainModule)
    }

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAndRemoveTask()
    }
}

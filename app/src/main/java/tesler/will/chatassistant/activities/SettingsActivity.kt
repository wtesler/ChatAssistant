package tesler.will.chatassistant.activities

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import tesler.will.chatassistant.R
import tesler.will.chatassistant._components.settings.SettingsScreen
import tesler.will.chatassistant.modules.settings.settingsModule
import tesler.will.chatassistant.ui.theme.AppTheme

class SettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadKoinModules(settingsModule)

        setContent {
            AppTheme {
                SettingsScreen(this)
            }
        }

        setTheme(R.style.Theme_ChatAssistant)

        window.setLayout(MATCH_PARENT, MATCH_PARENT)
    }

    override fun finish() {
        unloadKoinModules(settingsModule)
        super.finish()
    }

    override fun finishAndRemoveTask() {
        unloadKoinModules(settingsModule)
        super.finishAndRemoveTask()
    }

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAndRemoveTask()
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }
}

package tesler.will.chatassistant.activities

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import tesler.will.chatassistant._components.settings.SettingsScreen
import tesler.will.chatassistant.theme.AppTheme

class SettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                SettingsScreen(this)
            }
        }

        window.setLayout(MATCH_PARENT, MATCH_PARENT)
    }

    override fun onStop() {
        super.onStop()
        finishAndRemoveTask()
    }

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAndRemoveTask()
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }
}

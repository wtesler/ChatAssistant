package tesler.will.chatassistant.activities

import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import tesler.will.chatassistant.R
import tesler.will.chatassistant._components.Main

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Main(this)
        }

        setTheme(R.style.Theme_ChatAssistant)

        window.setLayout(MATCH_PARENT, MATCH_PARENT)
    }

    override fun onStop() {
        super.onStop()
        finishAndRemoveTask()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    override fun finishAndRemoveTask() {
        super.finishAndRemoveTask()
        overridePendingTransition(0, 0)
    }

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAndRemoveTask()
    }
}

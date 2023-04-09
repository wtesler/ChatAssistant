package tesler.will.chatassistant.activities

import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import tesler.will.chatassistant._components.Main

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Main(this)
        }

        window.setLayout(MATCH_PARENT, MATCH_PARENT)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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

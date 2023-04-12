package tesler.will.chatassistant.activities

import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import tesler.will.chatassistant._components.Main
import tesler.will.chatassistant._components.permissions.PermissionWrapper
import tesler.will.chatassistant.modules.main.mainModule
import tesler.will.chatassistant.stack.BackStackManager
import tesler.will.chatassistant.stack.State.MAIN
import tesler.will.chatassistant.theme.AppTheme

class MainActivity : ComponentActivity() {
    private val backStackManager by inject<BackStackManager>()

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            backStackManager.pop()
            if (backStackManager.empty()) {
                isEnabled = false
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        loadKoinModules(mainModule)

        backStackManager.push(MAIN)

        setContent {
            AppTheme {
                PermissionWrapper(this) {
                    Main(this)
                }
            }
        }

        window.setLayout(MATCH_PARENT, MATCH_PARENT)
    }

    override fun onResume() {
        if (backStackManager.empty()) {
            backStackManager.push(MAIN)
        }
        onBackPressedCallback.isEnabled = true
        super.onResume()
    }

    override fun onDestroy() {
        unloadKoinModules(mainModule)
        super.onDestroy()
    }
}

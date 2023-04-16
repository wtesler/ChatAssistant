package tesler.will.chatassistant.activities

import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import tesler.will.chatassistant._components.Main
import tesler.will.chatassistant._components.auth.Auth
import tesler.will.chatassistant._components.permissions.PermissionWrapper
import tesler.will.chatassistant.auth.IAuthManager
import tesler.will.chatassistant.modules.main.mainModule
import tesler.will.chatassistant.stack.IBackStackManager
import tesler.will.chatassistant.stack.State.MAIN
import tesler.will.chatassistant.theme.AppTheme


class MainActivity : ComponentActivity() {
    private val backStackManager by inject<IBackStackManager>()
    private val authManager by inject<IAuthManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadKoinModules(mainModule)

        backStackManager.init(this)
        authManager.init(this)

        backStackManager.push(MAIN)

        setContent {
            AppTheme {
                Auth(this) {
                    PermissionWrapper(this) {
                        Main(this)
                    }
                }
            }
        }

        window.setLayout(MATCH_PARENT, MATCH_PARENT)
    }

    override fun onResume() {
        backStackManager.setEnabled(true)
        if (backStackManager.empty()) {
            backStackManager.push(MAIN)
        }
        super.onResume()
    }

    override fun onDestroy() {
        unloadKoinModules(mainModule)
        super.onDestroy()
    }
}

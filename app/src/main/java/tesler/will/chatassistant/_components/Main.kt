package tesler.will.chatassistant._components

import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import tesler.will.chatassistant._components.background.Background
import tesler.will.chatassistant._components.card.Card
import tesler.will.chatassistant._components.permissions.PermissionWrapper
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.modules.main.mainModule
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.ui.theme.AppTheme

@Composable
fun Main(activity: Activity) {
    AppTheme {
        PermissionWrapper(activity) {
            ContentWrapper(activity)
        }
    }
}

@Composable
private fun ContentWrapper(activity: Activity) {
    var areModulesLoaded by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        loadKoinModules(mainModule)
        areModulesLoaded = true
    }

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                unloadKoinModules(mainModule)
                activity.finishAndRemoveTask()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (areModulesLoaded) {
        Content(activity)
    }
}

@Composable
private fun Content(activity: Activity?) {
    Background(activity) {
        Card()
    }
}

@Preview
@Composable
private fun MainPreview() {
    Previews.Wrap(mainTestModule, true) {
        Content(null)
    }
}

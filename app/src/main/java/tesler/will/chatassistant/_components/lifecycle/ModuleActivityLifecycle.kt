package tesler.will.chatassistant._components.lifecycle

import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module

@Composable
fun ModuleActivityLifecycle(activity: Activity, module: Module, content: @Composable () -> Unit) {
    var isModuleLoaded by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        loadKoinModules(module)
        isModuleLoaded = true

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                unloadKoinModules(module)
                activity.finishAndRemoveTask()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (isModuleLoaded) {
        content()
    }
}

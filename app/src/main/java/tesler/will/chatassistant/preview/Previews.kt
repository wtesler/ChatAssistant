package tesler.will.chatassistant.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import tesler.will.chatassistant.ui.theme.AppTheme

/**
 * Provides a `Wrap` method which is commonly useful in Preview composables.
 */
class Previews {
    companion object {
        @Composable
        fun Wrap(
            modules: List<Module> = arrayListOf(),
            isLightTheme: Boolean = true,
            content: @Composable () -> Unit
        ) {
            val context = LocalContext.current
            startKoin {
                androidContext(context)
                modules(modules)
            }
            AppTheme(darkTheme = !isLightTheme) {
                content()
            }
        }

        @Composable
        fun Wrap(
            module: Module,
            isLightTheme: Boolean = true,
            content: @Composable () -> Unit
        ) {
            Wrap(listOf(module), isLightTheme) {
                content()
            }
        }
    }
}

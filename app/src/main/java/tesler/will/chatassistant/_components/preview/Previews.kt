package tesler.will.chatassistant._components.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import tesler.will.chatassistant.theme.AppTheme

/**
 * Provides a `Wrap` method which is commonly useful in Preview composables.
 */
class Previews {
    companion object {
        @Composable
        fun Wrap(
            modules: List<Module> = listOf(),
            isDarkTheme: Boolean = true,
            content: @Composable () -> Unit
        ) {
            val context = LocalContext.current
            startKoin {
                androidContext(context)
                modules(modules)
            }
            AppTheme(isDarkTheme = isDarkTheme) {
                content()
            }
        }

        @Composable
        fun Wrap(
            module: Module,
            isDarkTheme: Boolean = true,
            content: @Composable () -> Unit
        ) {
            Wrap(listOf(module), isDarkTheme) {
                content()
            }
        }

        @Composable
        fun Wrap(
            isDarkTheme: Boolean = true,
            content: @Composable () -> Unit
        ) {
            Wrap(listOf(), isDarkTheme) {
                content()
            }
        }
    }
}

package tesler.will.chatassistant._components.background

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tesler.will.chatassistant.modifiers.noRippleClickable
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.theme.AppTheme

@Composable
fun Background(activity: Activity?, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.underlay)
            .statusBarsPadding()
            .noRippleClickable {
                activity?.finishAndRemoveTask()
            },
        contentAlignment = Alignment.BottomCenter,
    ) {
        content()
    }
}

@Preview
@Composable
private fun BackgroundPreview() {
    Previews.Wrap(mainTestModule, false) {
        Background(null) {
            Text(text = "Hello!", style = AppTheme.type.body1)
        }
    }
}

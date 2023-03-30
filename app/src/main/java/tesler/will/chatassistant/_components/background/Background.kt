package tesler.will.chatassistant._components.background

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tesler.will.chatassistant.modifiers.noRippleClickable
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.preview.Previews

@Composable
fun Background(activity: Activity?, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
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
fun BackgroundPreview() {
    Previews.Wrap(mainTestModule, false) {
        Background(null) {
            Text(text = "Hello!", color = MaterialTheme.colors.onSurface)
        }
    }
}

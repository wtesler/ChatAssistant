package tesler.will.chatassistant._components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import tesler.will.chatassistant._components.background.Background
import tesler.will.chatassistant._components.card.Card
import tesler.will.chatassistant._components.permissions.PermissionWrapper
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.ui.theme.AppTheme

@Composable
fun Main(activity: Activity?) {
    AppTheme {
        PermissionWrapper(activity) {
            Content(activity)
        }
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
fun MainPreview() {
    Previews.Wrap(mainTestModule, true) {
        Content(null)
    }
}

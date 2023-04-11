package tesler.will.chatassistant._components.speechinput.settingsbutton

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.activities.SettingsActivity
import tesler.will.chatassistant.modules.main.mainTestModule

@Composable
fun SettingsButtonResolver(modifier: Modifier) {
    val context = LocalContext.current

    val onIconClick = {
        val intent = Intent(context, SettingsActivity::class.java)
        val activity: Activity = context as Activity
        activity.finish()
        context.startActivity(intent)
    }

    SettingsButton(
        modifier = modifier,
        onIconClick
    )
}

@Preview
@Composable
private fun SettingsButtonResolverPreview() {
    Previews.Wrap(mainTestModule, true) {
        SettingsButtonResolver(Modifier)
    }
}

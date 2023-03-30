package tesler.will.chatassistant._components.permissions

import android.Manifest.permission.RECORD_AUDIO
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant._components.preview.Previews

@Composable
fun PermissionWrapper(activity: Activity?, content: @Composable () -> Unit) {
    var permissionsAccepted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        permissionsAccepted = it

        if (!permissionsAccepted) {
            Toast.makeText(
                activity,
                "You must enable the audio permission.",
                Toast.LENGTH_LONG
            ).show()
            activity?.finish()
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(RECORD_AUDIO)
    }

    when (permissionsAccepted) {
        true -> content()
        false -> Unit
    }
}

@Preview
@Composable
fun DefaultPreview() {
    Previews.Wrap(mainTestModule, true) {

    }
}
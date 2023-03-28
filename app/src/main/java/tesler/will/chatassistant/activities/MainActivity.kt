package tesler.will.chatassistant.activities

import android.Manifest.permission.RECORD_AUDIO
import android.os.Bundle
import android.os.StrictMode
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.unloadKoinModules
import tesler.will.chatassistant.BuildConfig
import tesler.will.chatassistant.R
import tesler.will.chatassistant.components.Card
import tesler.will.chatassistant.di.main.mainModule
import tesler.will.chatassistant.di.main.mainTestModule
import tesler.will.chatassistant.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults()
        }

        loadKoinModules(mainModule)

        setContent {
            PermissionWrapper()
        }

        setTheme(R.style.Theme_ChatAssistant)

        window.setLayout(MATCH_PARENT, MATCH_PARENT)
    }

    override fun onDestroy() {
        unloadKoinModules(mainModule)
        super.onDestroy()
    }
}

@Composable
fun PermissionWrapper() {
    var permissionsAccepted by remember { mutableStateOf(false) }

    val activity = LocalContext.current as ComponentActivity

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        permissionsAccepted = it

        if (!permissionsAccepted) {
            Toast.makeText(
                activity,
                "You must enable the audio permission.",
                Toast.LENGTH_LONG
            ).show()
            activity.finish()
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(RECORD_AUDIO)
    }

    when (permissionsAccepted) {
        true -> MainThemed()
        false -> Unit
    }
}

@Composable
fun Main() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Card()
    }
}

@Composable
fun MainThemed() {
    AppTheme {
        Main()
    }
}

@Preview(showBackground = true, backgroundColor = 0xF00, device = Devices.NEXUS_5)
@Composable
fun DefaultPreview() {
    val context = LocalContext.current
    startKoin {
        androidContext(context)
        modules(mainTestModule)
    }
    AppTheme(darkTheme = true) {
        Main()
    }
}
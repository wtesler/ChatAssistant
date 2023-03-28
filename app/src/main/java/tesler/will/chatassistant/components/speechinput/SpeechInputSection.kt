package tesler.will.chatassistant.components.speechinput

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.android.ext.koin.androidContext
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import tesler.will.chatassistant.di.main.mainTestModule
import tesler.will.chatassistant.components.speechinput.indicator.SpeechInputIndicator
import tesler.will.chatassistant.speech.ISpeechManager
import tesler.will.chatassistant.ui.theme.AppTheme
import tesler.will.chatassistant.ui.theme.spacing

@Composable
fun SpeechInputSection() {
    val speechManager = koinInject<ISpeechManager>()

    var text by remember { mutableStateOf("Hi, how can I help?") }

    val speechTextListener = remember {
        object: ISpeechManager.SpeechTextListener {
            override fun onText(value: String?) {
                if (value != null) {
                    text = value
                }
            }
        }
    }

    val speechErrorListener = remember {
        object: ISpeechManager.SpeechErrorListener {
            override fun onError(statusCode: Int?) {
                if (statusCode != null) {
                    text = if (statusCode == 7) {
                        "Did not receive any speech."
                    } else {
                        "Error. Status code $statusCode."
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        speechManager.addTextListener(speechTextListener)
        speechManager.addErrorListener(speechErrorListener)
        speechManager.start()

        onDispose {
            speechManager.stop()
            speechManager.removeTextListener(speechTextListener)
            speechManager.removeErrorListener(speechErrorListener)
        }
    }

    val TOP_PADDING = MaterialTheme.spacing.large
    val BOTTOM_PADDING = MaterialTheme.spacing.medium

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(0.dp, TOP_PADDING, 0.dp, BOTTOM_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.wrapContentWidth(),
            text = text,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            SpeechInputIndicator()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0x00F, device = Devices.NEXUS_5)
@Composable
fun SpeechInputSectionPreview() {
    val context = LocalContext.current
    startKoin {
        androidContext(context)
        modules(mainTestModule)
    }
    AppTheme(darkTheme = true) {
        SpeechInputSection()
    }
}

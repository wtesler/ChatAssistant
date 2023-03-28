package tesler.will.chatassistant.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject
import tesler.will.chatassistant.speech.SpeechManager
import tesler.will.chatassistant.ui.theme.spacing

@Composable
fun ReceivingInput() {
    val speechManager = koinInject<SpeechManager>()

    var text by remember { mutableStateOf("Speak Now") }

    fun onText(t: String?) {
        if (t != null) {
            text = t
        }
    }

    fun onError(errorCode: Int?) {
        if (errorCode != null) {
            text = if (errorCode == 7) {
                "Did not receive any speech."
            } else {
                "Error. Status code $errorCode."
            }
        }
    }

    DisposableEffect(Unit) {
        speechManager.addTextListener(::onText)
        speechManager.addErrorListener(::onError)
        speechManager.start()

        onDispose {
            speechManager.stop()
            speechManager.removeTextListener(::onText)
            speechManager.removeErrorListener(::onError)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.medium)
    ) {
        Text(text = text)
    }
}

package tesler.will.chatassistant.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
fun SendingInput() {
    val speechManager = koinInject<SpeechManager>()

    var text by remember { mutableStateOf("") }

    fun onText(t: String?) {
        if (t != null) {
            text = t
        }
    }

    DisposableEffect(Unit) {
        speechManager.addTextListener(::onText)
        onDispose {
            speechManager.removeTextListener(::onText)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.medium)
    ) {
        Column(

        ) {
            Text(text = text)
            Text(text = "\nSending Input...")
        }
    }
}

package tesler.will.chatassistant.components

import android.content.Intent
import android.speech.RecognizerIntent.*
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import tesler.will.chatassistant.speech.Listener
import tesler.will.chatassistant.ui.theme.spacing

@Composable
fun ReceivingInput(onFinished: () -> Unit, onText: (String) -> Unit) {
    var text by remember { mutableStateOf("Speak Now") }

    val context = LocalContext.current

    DisposableEffect(Unit) {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            var speech = SpeechRecognizer.createSpeechRecognizer(context)

            speech.setRecognitionListener(Listener({ words -> text = words }, onFinished, onText))

            val recognizerIntent = Intent(ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(EXTRA_MAX_RESULTS, 1)
                putExtra(EXTRA_LANGUAGE_MODEL, LANGUAGE_MODEL_FREE_FORM)
                putExtra(EXTRA_CALLING_PACKAGE, context.packageName)
                putExtra(EXTRA_PARTIAL_RESULTS, true)
                putExtra(EXTRA_PREFER_OFFLINE, true)
            }
            speech.startListening(recognizerIntent)

            onDispose {
                speech.destroy()
                speech = null
            }
        } else {
            Toast.makeText(context, "Speech recognition unavailable.", Toast.LENGTH_LONG).show()
            onDispose {}
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

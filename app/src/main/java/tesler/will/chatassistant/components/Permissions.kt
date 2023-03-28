package tesler.will.chatassistant.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import tesler.will.chatassistant.ui.theme.spacing

@Composable
fun Permissions(onFinished: () -> Unit) {
    var text by remember { mutableStateOf("Checking Permissions") }

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.medium)
    ) {
        Text(text = text)
    }
}

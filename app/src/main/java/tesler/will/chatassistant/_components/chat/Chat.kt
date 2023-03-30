package tesler.will.chatassistant._components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.chat.ChatModel
import tesler.will.chatassistant.chat.ChatModel.State.*
import tesler.will.chatassistant.ui.theme.spacing

@Composable
fun Chat(chatModel: ChatModel) {
    var backgroundColor = MaterialTheme.colors.background;
    if (chatModel.state == ERROR) {
        backgroundColor = MaterialTheme.colors.error
    } else if (chatModel.state == CREATED) {
        if (chatModel.isUser) {
            backgroundColor = MaterialTheme.colors.primary
        } else {
            backgroundColor = MaterialTheme.colors.primaryVariant
        }
    }

    val hPadding = MaterialTheme.spacing.large
    val vPadding = MaterialTheme.spacing.xxlarge

    val bottomPadding = when (chatModel.state) {
        CREATING -> 0.dp
        else -> vPadding
    }

    if (chatModel.text.isNotBlank()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(backgroundColor)
                .padding(hPadding, vPadding, hPadding, bottomPadding),
            contentAlignment = Center
        ) {
            Text(
                modifier = Modifier
                    .wrapContentWidth(),
                text = chatModel.text,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}

@Preview
@Composable
fun ChatPreview() {
    Previews.Wrap(true) {
        Chat(ChatModel("Hi, how can I help?", ChatModel.State.CREATED))
    }
}

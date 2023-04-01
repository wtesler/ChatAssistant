package tesler.will.chatassistant._components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.chat.ChatModel
import tesler.will.chatassistant.chat.ChatModel.State.*
import tesler.will.chatassistant.ui.theme.spacing

@Composable
fun Chat(modifier: Modifier, chatModel: ChatModel) {
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

    if (chatModel.text.isNotBlank()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(backgroundColor)
                .padding(hPadding, vPadding),
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
private fun ChatPreview() {
    Previews.Wrap(true) {
        Chat(Modifier, ChatModel("Hi, how can I help?", ChatModel.State.CREATED))
    }
}

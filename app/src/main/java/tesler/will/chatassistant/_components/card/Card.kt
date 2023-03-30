package tesler.will.chatassistant._components.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant._components.chat.ChatSection
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant._components.speechinput.SpeechInputSection
import tesler.will.chatassistant.modifiers.noRippleClickable
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.ui.theme.spacing

@Composable
fun Card(defaultVisible: Boolean = false) {
    val ENTRANCE_ANIM_SPEED_MS = 500

    var visible by remember { mutableStateOf(defaultVisible) }

    val shape = RoundedCornerShape(MaterialTheme.spacing.large)

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible,
        enter = slideInVertically(
            tween(ENTRANCE_ANIM_SPEED_MS, easing = LinearOutSlowInEasing),
            initialOffsetY = {fullHeight ->  fullHeight}
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(0.dp, MaterialTheme.spacing.medium)
                .border(BorderStroke(0.dp, Color.Black), shape)
                .clip(shape)
                .noRippleClickable {},
            color = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom
            ) {

                Box(
                    modifier = Modifier.weight(1f, false)
                ) {
                    ChatSection()
                }

                Box(
                    modifier = Modifier
                ) {
                    SpeechInputSection()
                }
            }
        }
    }
}

@Preview
@Composable
fun CardPreview() {
    Previews.Wrap(mainTestModule, true) {
        Card(true)
    }
}

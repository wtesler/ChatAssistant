package tesler.will.chatassistant._components.speechinput.indicator

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.math.MathUtils
import org.koin.compose.koinInject
import tesler.will.chatassistant.modules.speech.speechTestModule
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.speechinput.ISpeechInputManager
import kotlin.math.pow

@Composable
fun SpeechInputIndicatorDot(
    durationMs: Int,
    wobbleDistance: Float,
    delayMs: Int,
    amplitudeMultiplier: Float,
    amplitudePow: Float
) {
    val CIRCLE_LENGTH = 9.dp
    val COLOR = MaterialTheme.colors.onSurface
    val MAX_STRETCH = 2.5f
    val STRETCH_SMOOTHING = .6f

    val speechManager = koinInject<ISpeechInputManager>()

    var yOffset by remember { mutableStateOf(0.dp) }
    var isSpeechStarted by remember { mutableStateOf(false) }
    var previousAmplitude by remember { mutableStateOf(0f) }
    var amplitude by remember { mutableStateOf(0f) }
    val stretch by remember(isSpeechStarted, amplitude) {
        derivedStateOf {
            if (isSpeechStarted) {
                val clampedAmplitude = MathUtils.clamp(amplitude, -2f, 10f)
                var amplitudeProgress = (clampedAmplitude + 2f) / 12f
                amplitudeProgress = amplitudeProgress.pow(amplitudePow)
                val lerp = ((1 - amplitudeProgress) * 1) + (amplitudeProgress * MAX_STRETCH)
                lerp * amplitudeMultiplier
            } else {
                1f
            }
        }
    }

    val speechListener = remember {
        object : ISpeechInputManager.Listener {
            override fun onSpeechStarted() {
                isSpeechStarted = true
            }

            override fun onAmplitude(value: Float?) {
                if (!isSpeechStarted) {
                    return
                }
                if (value != null) {
                    val amplitudeHold = amplitude
                    amplitude =
                        (previousAmplitude * STRETCH_SMOOTHING) + (value * (1 - STRETCH_SMOOTHING))
                    previousAmplitude = amplitudeHold
                }
            }
        }
    }

    DisposableEffect(Unit) {
        speechManager.addListener(speechListener)

        onDispose {
            speechManager.removeListener(speechListener)
        }
    }

    val transition = rememberInfiniteTransition()

    val animY by transition.animateFloat(
        initialValue = wobbleDistance,
        targetValue = -wobbleDistance,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMs, 0, EaseInOutSine),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(delayMs, StartOffsetType.Delay)
        )
    )

    yOffset = if (isSpeechStarted) yOffset * 0.9f else animY.dp

    Box(
        modifier = Modifier
            .width(CIRCLE_LENGTH)
            .height(CIRCLE_LENGTH * stretch)
            .offset(0.dp, yOffset)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(COLOR, CircleShape)
        )
    }
}

@Preview
@Composable
private fun SpeechInputDotPreview() {
    Previews.Wrap(speechTestModule, true) {
        SpeechInputIndicatorDot(1000, 2f, 0, 1f, 1.5f)
    }
}

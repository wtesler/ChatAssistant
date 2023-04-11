package tesler.will.chatassistant._components.speechinput.mutebutton

import android.content.Context.AUDIO_SERVICE
import android.database.ContentObserver
import android.media.AudioManager
import android.media.AudioManager.STREAM_MUSIC
import android.os.Handler
import android.provider.Settings.System.CONTENT_URI
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.speechoutput.ISpeechOutputManager

@Composable
fun MuteButtonResolver(modifier: Modifier) {
    val context = LocalContext.current

    val speechOutputManager = koinInject<ISpeechOutputManager>()

    val audioManager = remember { context.getSystemService(AUDIO_SERVICE) as AudioManager }
    val isInitiallyMuted by remember(audioManager) {
        derivedStateOf {
            audioManager.getStreamVolume(STREAM_MUSIC) == 0
        }
    }

    var viewModel by remember { mutableStateOf(MuteButtonViewModel(isInitiallyMuted)) }

    fun setIsMuted(isMuted: Boolean) {
        viewModel = viewModel.copy(isMuted = isMuted)
    }

    val contentObserver = remember {
        object : ContentObserver(Handler(context.mainLooper)) {
            override fun onChange(selfChange: Boolean) {
                val volume = audioManager.getStreamVolume(STREAM_MUSIC)
                if (volume == 0) {
                    setIsMuted(true)
                } else {
                    setIsMuted(false)
                }
            }
        }
    }

    DisposableEffect(Unit) {
        context.contentResolver.registerContentObserver(CONTENT_URI, true, contentObserver)

        onDispose {
            context.contentResolver.unregisterContentObserver(contentObserver)
        }
    }

    val onClick = {
        speechOutputManager.setMuted(!viewModel.isMuted)
    }

    MuteButton(
        modifier = modifier,
        viewModel,
        onClick
    )
}

@Preview
@Composable
private fun MuteButtonResolverPreview() {
    Previews.Wrap(mainTestModule, true) {
        MuteButtonResolver(Modifier)
    }
}

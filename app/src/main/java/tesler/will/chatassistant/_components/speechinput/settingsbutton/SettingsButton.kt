package tesler.will.chatassistant._components.speechinput.settingsbutton

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import tesler.will.chatassistant.R
import tesler.will.chatassistant._components.preview.Previews
import tesler.will.chatassistant.modules.main.mainTestModule
import tesler.will.chatassistant.theme.AppTheme

@Composable
fun SettingsButton(
    modifier: Modifier,
    model: SettingsButtonViewModel,
    onIconClick: () -> Unit,
    onSettingsDismiss: () -> Unit
) {
    Box(
        modifier = modifier
            .size(AppTheme.dimens.icon_normal)
            .padding(0.dp, 0.dp, 0.dp, 0.dp)
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false),
                    onClick = onIconClick
                )
                .align(Center),
            painter = painterResource(id = R.drawable.ic_launcher_round),
            contentDescription = "Settings Button",
        )

        DropdownMenu(
            modifier = Modifier
                .align(Center)
                .background(AppTheme.colors.dropdownBackground),
            expanded = model.isExpanded,
            offset = DpOffset(0.dp, 10.dp),
            onDismissRequest = onSettingsDismiss
        ) {

            for (option in model.options) {
                DropdownMenuItem(
                    modifier = Modifier
                        .wrapContentWidth(),
                    content = {
                        Text(
                            option.text,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = AppTheme.type.body1
                        )
                    },
                    onClick = option.onClick,
                    contentPadding = PaddingValues(0.dp, 0.dp)
                )

            }
        }
    }
}

@Preview
@Composable
private fun SettingsButtonPreview() {
    Previews.Wrap(mainTestModule, false) {
        SettingsButton(
            Modifier,
            SettingsButtonViewModel(true, listOf()),
            {},
            {}
        )
    }
}

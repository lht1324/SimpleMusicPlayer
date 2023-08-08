package com.overeasy.simplemusicplayer.scenario.setting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.overeasy.simplemusicplayer.R
import com.overeasy.simplemusicplayer.composeComponents.Header
import com.overeasy.simplemusicplayer.composeComponents.dpToSp
import com.overeasy.simplemusicplayer.composeComponents.noRippleClickable
import com.overeasy.simplemusicplayer.ui.fontFamily

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsState(initial = false)

    BackHandler {
        onClickBack()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.primary)
    ) {
        Header(
            title = "설정",
            doesShowDivider = true,
            startContent = {
                Image(
                    painter = painterResource(
                        id = if (isDarkTheme)
                            R.drawable.icon_back_arrow_white
                        else
                            R.drawable.icon_back_arrow_black
                    ),
                    modifier = Modifier.noRippleClickable(onClick = onClickBack),
                    contentDescription = "음악 재생 화면으로 이동합니다."
                )
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            SettingItem(
                text = "어두운 화면",
                isChecked = isDarkTheme,
                onCheckChanged = { checkState ->
                    viewModel.onClickUpdateIsDarkTheme(checkState)
                }
            )
        }
    }
}

@Composable
private fun SettingItem(
    modifier: Modifier = Modifier,
    text: String,
    isChecked: Boolean,
    onCheckChanged: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable {
                onCheckChanged(!isChecked)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            color = MaterialTheme.colors.secondary,
            fontSize = 20.dpToSp(),
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamily
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckChanged,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary,
                uncheckedThumbColor = MaterialTheme.colors.primary,
                checkedTrackColor = MaterialTheme.colors.secondary,
                uncheckedTrackColor = MaterialTheme.colors.primaryVariant,
                checkedBorderColor = MaterialTheme.colors.secondary,
                uncheckedBorderColor = MaterialTheme.colors.primaryVariant
            ),
            thumbContent = {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = MaterialTheme.colors.primary,
                            shape = CircleShape
                        )
                )
            }
        )
    }
}
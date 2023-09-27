package com.velkonost.getbetter.core.emojipicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.velkonost.getbetter.core.compose.emojipicker.utils.capitalizeWords

@Composable
fun ComposeEmojiPickerGroupTitle(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    titleTextColor: Color,
    titleText: String,
    titleTextStyle: TextStyle,
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
            )
            .padding(
                all = 8.dp,
            ),
        text = titleText
            .replace(
                oldChar = '-',
                newChar = ' ',
            )
            .capitalizeWords(),
        color = titleTextColor,
        style = titleTextStyle,
    )
}

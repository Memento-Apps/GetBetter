package com.velkonost.getbetter.android.features.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.velkonost.getbetter.shared.resources.SharedR
import dev.icerock.moko.resources.compose.colorResource
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: Painter,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = modifier
                .size(42.dp)
                .background(
                    color = colorResource(resource = SharedR.colors.background_icon),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(8.dp),
            painter = icon,
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                color = colorResource(resource = SharedR.colors.icon_inactive)
            )
        )

        Text(
            modifier = modifier.padding(start = 12.dp),
            text = title,
            color = colorResource(resource = SharedR.colors.text_primary),
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(modifier = modifier.weight(1f))

        Image(
            modifier = modifier
                .size(24.dp),
            painter = painterResource(imageResource = SharedR.images.ic_arrow_right),
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                color = colorResource(resource = SharedR.colors.icon_inactive)
            )
        )
    }
}
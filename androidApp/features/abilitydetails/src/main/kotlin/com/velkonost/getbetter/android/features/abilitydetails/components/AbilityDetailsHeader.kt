package com.velkonost.getbetter.android.features.abilitydetails.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.velkonost.getbetter.shared.resources.SharedR
import dev.icerock.moko.resources.compose.colorResource
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun AbilityDetailsHeader(
    modifier: Modifier = Modifier,
    title: String,
    imageAlpha: Float,
    onBackClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier.padding(top = 40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = modifier
                .alpha(imageAlpha)
                .padding(start = 20.dp)
                .size(42.dp)
                .background(
                    color = colorResource(resource = SharedR.colors.background_icon),
                    shape = MaterialTheme.shapes.small
                )
                .padding(4.dp)

                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { onBackClick() },
            painter = painterResource(imageResource = SharedR.images.ic_arrow_back),
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                color = colorResource(resource = SharedR.colors.icon_active)
            )
        )

        Text(
            modifier = modifier.padding(start = 12.dp),
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = colorResource(resource = SharedR.colors.text_title),
        )
    }
}
package com.velkonost.getbetter.android.features.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.velkonost.getbetter.shared.resources.SharedR
import dev.icerock.moko.resources.compose.colorResource
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun Avatar(
    modifier: Modifier,
    isLoading: Boolean,
    avatarUrl: String?,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = MaterialTheme.shapes.medium,
            )
            .size(128.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        colorResource(resource = SharedR.colors.button_gradient_start),
                        colorResource(resource = SharedR.colors.button_gradient_end)
                    )
                ),
                shape = MaterialTheme.shapes.medium
            )
            .clickable {
                if (!isLoading) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick.invoke()
                }
            },
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = modifier
                    .size(64.dp)
                    .align(Alignment.Center),
                color = colorResource(resource = SharedR.colors.text_light).copy(alpha = 0.5f)
            )
        } else {
            SubcomposeAsyncImage(
                modifier = modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(avatarUrl)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                loading = {
                    Box {
                        CircularProgressIndicator(
                            modifier = modifier
                                .size(64.dp)
                                .align(Alignment.Center),
                            color = colorResource(resource = SharedR.colors.text_light).copy(alpha = 0.5f)
                        )
                    }
                },
                error = {
                    Image(
                        modifier = modifier
                            .size(64.dp)
                            .clip(MaterialTheme.shapes.small),
                        painter = painterResource(imageResource = SharedR.images.logo),
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@Composable
fun BoxScope.Placeholder(
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .size(64.dp)
            .align(Alignment.Center),
        painter = painterResource(imageResource = SharedR.images.ic_image_placeholder),
        contentDescription = null,
        colorFilter = ColorFilter.tint(
            color = colorResource(resource = SharedR.colors.text_light).copy(alpha = 0.5f)
        )
    )
}
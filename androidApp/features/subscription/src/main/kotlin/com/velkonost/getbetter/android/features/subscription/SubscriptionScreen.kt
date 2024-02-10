package com.velkonost.getbetter.android.features.subscription

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.velkonost.getbetter.shared.features.subscription.presentation.SubscriptionViewModel
import com.velkonost.getbetter.shared.resources.SharedR
import dev.icerock.moko.resources.compose.colorResource
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SubscriptionScreen(
    modifier: Modifier = Modifier,
    viewModel: SubscriptionViewModel
) {

    val state by viewModel.viewState.collectAsStateWithLifecycle()

    val firstPointVisible = remember { mutableStateOf(false) }
    val secondPointVisible = remember { mutableStateOf(false) }
    val thirdPointVisible = remember { mutableStateOf(false) }
    val forthPointVisible = remember { mutableStateOf(false) }
    val fifthPointVisible = remember { mutableStateOf(false) }
    val sixthPointVisible = remember { mutableStateOf(false) }

    val logoVisible = remember { mutableStateOf(false) }
    val titleVisible = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            delay(500)
            logoVisible.value = true
            delay(500)
            titleVisible.value = true

            delay(1000)
            firstPointVisible.value = true
            delay(1000)
            secondPointVisible.value = true
            delay(1000)
            thirdPointVisible.value = true
            delay(1000)
            forthPointVisible.value = true
            delay(1000)
            fifthPointVisible.value = true
            delay(1000)
            sixthPointVisible.value = true
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorResource(resource = SharedR.colors.main_background))
            .padding(start = 16.dp, end = 16.dp)
    ) {

        Column(modifier = modifier.padding(top = 50.dp)) {
            Row(modifier = modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(imageResource = SharedR.images.ic_close),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        color = colorResource(resource = SharedR.colors.icon_active)
                    )
                )
                Spacer(modifier.weight(1f))

                Text(
                    text = stringResource(resource = SharedR.strings.paywall_restore),
                    style = MaterialTheme.typography.titleSmall,
                    color = colorResource(resource = SharedR.colors.text_secondary)
                )
            }

            AnimatedVisibility(visible = logoVisible.value, label = "") {
                Image(
                    modifier = modifier
                        .fillMaxWidth()
                        .alpha(0.8f)
                        .padding(top = 16.dp),
                    painter = painterResource(
                        imageResource = if (isSystemInDarkTheme()) SharedR.images.ic_getbetter_light_
                        else SharedR.images.ic_getbetter_dark_
                    ),
                    contentDescription = null
                )
            }

            AnimatedVisibility(visible = titleVisible.value, label = "") {
                Row(modifier = modifier.padding(top = 16.dp)) {
                    Spacer(modifier.weight(1f))
                    Text(
                        text = stringResource(resource = SharedR.strings.paywall_title),
                        color = colorResource(resource = SharedR.colors.text_title),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier.weight(1f))
                }
            }

            AnimatedVisibility(visible = firstPointVisible.value, label = "") {
                SubscriptionPoint(
                    title = stringResource(resource = SharedR.strings.paywall_point_1),
                    visible = firstPointVisible.value,
                    index = 0
                )
            }

            AnimatedVisibility(visible = secondPointVisible.value, label = "") {
                SubscriptionPoint(
                    title = stringResource(resource = SharedR.strings.paywall_point_2),
                    visible = secondPointVisible.value,
                    index = 1
                )
            }

            AnimatedVisibility(visible = thirdPointVisible.value, label = "") {
                SubscriptionPoint(
                    title = stringResource(resource = SharedR.strings.paywall_point_3),
                    visible = thirdPointVisible.value,
                    index = 2
                )
            }

            AnimatedVisibility(visible = forthPointVisible.value, label = "") {
                SubscriptionPoint(
                    title = stringResource(resource = SharedR.strings.paywall_point_4),
                    visible = forthPointVisible.value,
                    index = 3
                )
            }

            AnimatedVisibility(visible = fifthPointVisible.value, label = "") {
                SubscriptionPoint(
                    title = stringResource(resource = SharedR.strings.paywall_point_5),
                    visible = fifthPointVisible.value,
                    index = 4
                )
            }

            AnimatedVisibility(visible = sixthPointVisible.value, label = "") {
                SubscriptionPoint(
                    title = stringResource(resource = SharedR.strings.paywall_point_6),
                    visible = sixthPointVisible.value,
                    index = 5
                )
            }

        }
    }

}

@Composable
fun SubscriptionPoint(
    modifier: Modifier = Modifier,
    title: String,
    visible: Boolean,
    index: Int
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(SharedR.files.anim_mark.rawResId)
    )

    val scope = rememberCoroutineScope()
    val animVisible = remember { mutableStateOf(false) }

    LaunchedEffect(visible) {
        if (visible) {
            scope.launch {
                delay(100)
                animVisible.value = true
            }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (animVisible.value) {
            LottieAnimation(
                modifier = modifier.size(48.dp),
                composition = composition,
                iterations = 1,
            )
        } else {
            Spacer(modifier.size(48.dp))
        }

        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = colorResource(resource = SharedR.colors.text_title),
            textAlign = TextAlign.Start,
        )

        Spacer(modifier.weight(1f))
    }
}
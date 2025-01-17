package com.velkonost.getbetter.core.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.velkonost.getbetter.shared.resources.SharedR
import dev.icerock.moko.resources.compose.colorResource

@Composable
fun PrimaryBox(
    modifier: Modifier = Modifier,
    padding: Int = 16,
    topPadding: Int = 12,
    isBright: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .padding(top = topPadding.dp)
            .shadow(
                elevation = 8.dp,
                shape = MaterialTheme.shapes.medium,
            )
            .fillMaxWidth()
            .background(
                color = colorResource(
                    resource =
                    if (isBright) SharedR.colors.button_gradient_start
                    else SharedR.colors.background_item
                ),
                shape = MaterialTheme.shapes.medium
            )
            .padding(padding.dp)
    ) {
        content()
    }
}
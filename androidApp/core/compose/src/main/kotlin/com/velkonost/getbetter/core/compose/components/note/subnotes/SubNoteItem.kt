package com.velkonost.getbetter.core.compose.components.note.subnotes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.velkonost.getbetter.shared.core.model.ui.SubNoteUI
import com.velkonost.getbetter.shared.resources.SharedR
import dev.icerock.moko.resources.compose.colorResource
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun SubNoteItem(
    modifier: Modifier = Modifier,
    item: SubNoteUI,
    isCompleteVisible: Boolean = false,
    onlyView: Boolean = false,
    onDeleteSubNote: (SubNoteUI) -> Unit,
    onCompleteClick: ((SubNoteUI) -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scroll = rememberScrollState(0)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp)
            .clip(shape = MaterialTheme.shapes.medium)
            .background(
                color = colorResource(resource = SharedR.colors.text_field_background),
                shape = MaterialTheme.shapes.medium
            ),

        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth(0.8f)
                .padding(start = 12.dp)
                .horizontalScroll(scroll),
            text = item.text,
            style = MaterialTheme.typography.titleSmall
                .copy(
                    color = colorResource(resource = SharedR.colors.text_secondary),
                    textAlign = TextAlign.Start
                ),
            maxLines = 1,
        )

        Spacer(modifier = modifier.weight(1f))

        AnimatedVisibility(visible = !onlyView) {
            Box(
                modifier = modifier
                    .padding(end = 12.dp)
                    .size(36.dp)
                    .background(
                        color = colorResource(resource = SharedR.colors.background_item),
                        shape = MaterialTheme.shapes.medium
                    )
                    .align(Alignment.CenterVertically)

            ) {
                Image(
                    modifier = modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { onDeleteSubNote.invoke(item) },
                    painter = painterResource(imageResource = SharedR.images.ic_close),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = colorResource(resource = SharedR.colors.icon_inactive))
                )
            }
        }

        AnimatedVisibility(visible = isCompleteVisible && onlyView) {
            Box(
                modifier = modifier
                    .padding(end = 12.dp)
                    .size(36.dp)
                    .background(
                        color = colorResource(resource = SharedR.colors.button_gradient_start),
                        shape = MaterialTheme.shapes.medium
                    )
                    .align(Alignment.CenterVertically)

            ) {
                Image(
                    modifier = modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { onCompleteClick?.invoke(item) },
                    painter = painterResource(
                        imageResource =
                        if (item.completionDate != null) SharedR.images.ic_cancel
                        else SharedR.images.ic_save
                    ),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = colorResource(resource = SharedR.colors.text_light))
                )
            }
        }

    }

}
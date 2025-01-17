package com.velkonost.getbetter.android.features.social.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.velkonost.getbetter.core.compose.components.PrimaryBox
import com.velkonost.getbetter.core.compose.components.note.tags.TagItem
import com.velkonost.getbetter.core.compose.components.notelist.NoteItemData
import com.velkonost.getbetter.core.compose.components.notelist.NoteItemHeader
import com.velkonost.getbetter.shared.core.model.Emoji
import com.velkonost.getbetter.shared.core.model.note.Note
import com.velkonost.getbetter.shared.core.model.ui.TagUI
import com.velkonost.getbetter.shared.resources.SharedR
import dev.icerock.moko.resources.compose.colorResource
import dev.icerock.moko.resources.compose.painterResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FeedNoteItem(
    modifier: Modifier = Modifier,
    item: Note,
    paddings: PaddingValues = PaddingValues(horizontal = 20.dp),
    onClick: (Note) -> Unit,
    onLikeClick: (Note) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    PrimaryBox(
        modifier = modifier
            .padding(paddings)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick.invoke(item)
            }
    ) {
        Column {
            NoteItemHeader(
                areaName = item.area.name,
                taskName = item.task?.name,
                areaIcon = Emoji.getIconById(item.area.emojiId!!),
                likesData = item.likesData,
                onLikeClick = {
                    onLikeClick.invoke(item)
                }
            )

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .background(
                        color = colorResource(resource = SharedR.colors.text_field_background),
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(8.dp)
            ) {

                NoteItemData(
                    noteType = item.noteType,
                    subNotes = item.subNotes,
                    expectedCompletionDate = item.expectedCompletionDateStr?.toString(context),
                    expectedCompletionDateExpired = item.expectedCompletionDateExpired,
                    completionDate = item.completionDateStr?.toString(context),
                    mediaAmount = item.mediaUrls.size,
                    isPrivate = item.isPrivate
                )

                Text(
                    modifier = modifier.padding(top = 12.dp),
                    text = item.text,
                    maxLines = 10,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(resource = SharedR.colors.text_title)
                )

                FlowRow(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    item.tags.forEach { tag ->
                        TagItem(tag = TagUI(text = tag))
                    }
                }
            }

            Row(
                modifier = modifier.padding(top = 6.dp, start = 4.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                item.author?.avatarUrl?.let { avatarUrl ->
                    SubcomposeAsyncImage(
                        modifier = modifier
                            .size(24.dp)
                            .clip(MaterialTheme.shapes.small),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(avatarUrl)
                            .build(),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        error = {
                            Image(
                                modifier = modifier
                                    .size(24.dp)
                                    .clip(MaterialTheme.shapes.small),
                                painter = painterResource(imageResource = SharedR.images.logo),
                                contentDescription = null
                            )
                        }
                    )
                }


                item.author?.displayName?.let {
                    Text(
                        modifier = modifier.padding(start = 6.dp),
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(resource = SharedR.colors.text_primary)
                    )
                }

                Spacer(modifier.weight(1f))

                Text(
                    text = item.createdDateStr.toString(context),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(resource = SharedR.colors.text_primary)
                )
            }
        }
    }
}
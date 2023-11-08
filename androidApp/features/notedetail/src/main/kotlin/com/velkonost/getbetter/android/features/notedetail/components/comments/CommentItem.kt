package com.velkonost.getbetter.android.features.notedetail.components.comments

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.velkonost.getbetter.shared.core.model.comments.Comment
import com.velkonost.getbetter.shared.resources.SharedR
import dev.icerock.moko.resources.compose.colorResource
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun CommentItem(
    modifier: Modifier = Modifier,
    item: Comment
) {

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
//            .padding(top = 16.dp)
    ) {
        Row(
            modifier = modifier.padding(top = 6.dp, start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (item.author.avatar != null) {
                item.author.avatar?.let { avatarBytes ->
                    SubcomposeAsyncImage(
                        modifier = modifier
                            .size(24.dp)
                            .clip(MaterialTheme.shapes.small),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(
                                BitmapFactory.decodeByteArray(
                                    avatarBytes, 0, avatarBytes.size
                                )
                            )
                            .build(),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                    )
                }
            } else {
                Image(
                    modifier = modifier
                        .size(24.dp)
                        .clip(MaterialTheme.shapes.small),
                    painter = painterResource(imageResource = SharedR.images.logo),
                    contentDescription = null
                )
            }

            item.author.displayName?.let {
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

        Text(
            modifier = modifier.padding(top = 12.dp),
            text = item.text,
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(resource = SharedR.colors.text_secondary)
        )

    }
}
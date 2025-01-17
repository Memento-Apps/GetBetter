package com.velkonost.getbetter.android.features.feedback.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.velkonost.getbetter.shared.core.model.feedback.Feedback
import com.velkonost.getbetter.shared.core.model.feedback.FeedbackMessage
import com.velkonost.getbetter.shared.features.feedback.presentation.contract.FeedbackDetailsState
import com.velkonost.getbetter.shared.resources.SharedR
import dev.icerock.moko.resources.compose.colorResource

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedbackDetailBottomSheet(
    modifier: Modifier = Modifier,
    modalSheetState: ModalBottomSheetState,
    item: Feedback?,
    feedbackDetailsState: FeedbackDetailsState,
    onAnswerTextChanged: (String) -> Unit,
    onAnswerSendClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetBackgroundColor = colorResource(resource = SharedR.colors.main_background),
        sheetContent = {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                        .padding(bottom = 40.dp)
                        .padding(horizontal = 4.dp)
                        .verticalScroll(scrollState)
                ) {
                    Spacer(modifier.height(32.dp))

                    item?.messages?.forEach { message ->
                        FeedbackMessageView(message = message)
                    }
                }

                Column(modifier = modifier.fillMaxSize()) {
                    Spacer(modifier.weight(1f))
                    FeedbackAnswerTextField(
                        value = feedbackDetailsState.answerText,
                        onValueChanged = onAnswerTextChanged,
                        onSendClick = onAnswerSendClick
                    )
                }
            }
        }
    ) {}
}

@Composable
fun FeedbackMessageView(
    modifier: Modifier = Modifier,
    message: FeedbackMessage
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .padding(horizontal = 16.dp)
    ) {
        if (!message.fromUser) {
            Spacer(modifier.weight(1f))
        }

        Column(
            modifier = modifier
                .shadow(
                    elevation = 8.dp,
                    shape = MaterialTheme.shapes.medium,
                )
                .background(
                    color = colorResource(
                        resource =
                        if (message.fromUser) SharedR.colors.background_item
                        else SharedR.colors.button_gradient_start
                    ),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(6.dp),
            horizontalAlignment = if (message.fromUser) Alignment.Start else Alignment.End
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(resource = SharedR.colors.text_secondary),
                textAlign = if (message.fromUser) TextAlign.Start else TextAlign.End
            )

            Text(
                modifier = modifier.padding(top = 8.dp),
                text = message.datetimeStr.toString(LocalContext.current),
                style = MaterialTheme.typography.labelSmall,
                color = colorResource(resource = SharedR.colors.text_primary),
                textAlign = if (message.fromUser) TextAlign.Start else TextAlign.End
            )
        }

        if (message.fromUser) {
            Spacer(modifier.weight(1f))
        }
    }

}
package com.velkonost.getbetter.core.compose.components.note

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.velkonost.getbetter.core.compose.components.AppAlertDialog
import com.velkonost.getbetter.core.compose.components.AppButton
import com.velkonost.getbetter.core.compose.components.AppDatePickerDialog
import com.velkonost.getbetter.core.compose.components.PrimaryBox
import com.velkonost.getbetter.shared.resources.SharedR
import dev.icerock.moko.resources.compose.colorResource
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc

@Composable
fun CompletionDateBlock(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    initialValue: Long? = null,
    initialValueStr: String? = null,
    isCompleteVisible: Boolean = false,
    completionDateStr: String? = null,
    onSetCompletionDate: (Long?) -> Unit,
    onCompleteClick: (() -> Unit)? = null
) {
    val context = LocalContext.current

    val notSetText = StringDesc
        .Resource(SharedR.strings.create_note_completion_date_hint)
        .toString(context)
    var date by remember { mutableStateOf(initialValueStr ?: notSetText) }

    var showDatePicker by remember { mutableStateOf(false) }
    val confirmCancelCompletionDialog = remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }

    PrimaryBox(padding = 0) {
        Column {
            Row(
                modifier = modifier.height(60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = modifier.padding(start = 16.dp),
                    text = stringResource(resource = SharedR.strings.create_note_completion_date_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = colorResource(resource = SharedR.colors.text_primary)
                )
                Spacer(modifier = modifier.weight(1f))

                AnimatedVisibility(visible = date != notSetText && enabled) {
                    Image(
                        modifier = modifier
                            .padding(end = 4.dp)
                            .size(16.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                date = notSetText
                                onSetCompletionDate.invoke(null)
                            },
                        painter = painterResource(imageResource = SharedR.images.ic_cancel),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(colorResource(resource = SharedR.colors.icon_inactive))
                    )
                }

                AnimatedContent(targetState = date, label = "") { content ->
                    Text(
                        modifier = modifier
                            .padding(end = 16.dp)
                            .background(
                                color = colorResource(resource = SharedR.colors.text_field_background),
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(12.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                if (enabled) {
                                    showDatePicker = true
                                }
                            },
                        text = content,
                        style = MaterialTheme.typography.titleMedium,
                        color = colorResource(resource = SharedR.colors.text_primary),
                    )
                }
            }

            AnimatedVisibility(visible = isCompleteVisible) {
                if (onCompleteClick != null) {
                    AnimatedVisibility(visible = completionDateStr == null) {
                        Row(
                            modifier = modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier.weight(1f))

                            AppButton(
                                labelText = "Complete",
                                isLoading = isLoading,
                                onClick = onCompleteClick
                            )

                            Spacer(modifier.weight(1f))
                        }
                    }
                    AnimatedVisibility(visible = completionDateStr != null) {
                        Row(
                            modifier = modifier.height(60.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = modifier.padding(start = 16.dp),
                                text = stringResource(resource = SharedR.strings.create_note_completion_date_title),
                                style = MaterialTheme.typography.titleMedium,
                                color = colorResource(resource = SharedR.colors.text_primary)
                            )
                            Spacer(modifier = modifier.weight(1f))

                            AnimatedContent(
                                targetState = completionDateStr,
                                label = ""
                            ) { content ->
                                if (content != null) {
                                    Text(
                                        modifier = modifier
                                            .padding(end = 16.dp)
                                            .background(
                                                color = colorResource(resource = SharedR.colors.button_gradient_start),
                                                shape = MaterialTheme.shapes.medium
                                            )
                                            .padding(12.dp)
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                            ) {
                                                confirmCancelCompletionDialog.value = true
                                            },
                                        text = content,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = colorResource(resource = SharedR.colors.text_light),
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    if (showDatePicker) {
        AppDatePickerDialog(
            title = stringResource(resource = SharedR.strings.create_note_completion_date_title),
            initialValue = initialValue,
            onConfirm = { millis, selectedDate ->
                showDatePicker = false
                date = selectedDate

                onSetCompletionDate.invoke(millis)
            },
            onDecline = {
                showDatePicker = false
            }
        )
    }

    if (confirmCancelCompletionDialog.value) {
        AppAlertDialog(
            title = stringResource(resource = SharedR.strings.add_area_confirm_delete_title),
            text = stringResource(resource = SharedR.strings.add_area_confirm_delete_text),
            confirmTitle = stringResource(resource = SharedR.strings.confirm),
            cancelTitle = stringResource(resource = SharedR.strings.cancel),
            onDismiss = { confirmCancelCompletionDialog.value = false },
            onConfirmClick = {
                onCompleteClick?.invoke()
                confirmCancelCompletionDialog.value = false
            }
        )
    }
}


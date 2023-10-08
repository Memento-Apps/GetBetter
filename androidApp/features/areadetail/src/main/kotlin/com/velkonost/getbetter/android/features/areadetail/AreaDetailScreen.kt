package com.velkonost.getbetter.android.features.areadetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velkonost.getbetter.android.features.areadetail.components.AreaDetailContent
import com.velkonost.getbetter.android.features.areadetail.components.BottomButtons
import com.velkonost.getbetter.android.features.areadetail.components.SubmitDialogs
import com.velkonost.getbetter.core.compose.components.Loader
import com.velkonost.getbetter.shared.features.areadetail.presentation.AreaDetailViewModel
import com.velkonost.getbetter.shared.features.areadetail.presentation.contract.AreaDetailAction
import com.velkonost.getbetter.shared.features.areadetail.presentation.contract.AreaDetailEvent
import com.velkonost.getbetter.shared.resources.SharedR
import dev.icerock.moko.resources.compose.colorResource
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AreaDetailScreen(
    modifier: Modifier = Modifier,
    areaId: String,
    viewModel: AreaDetailViewModel = koinViewModel(),
    modalSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val isEmojiPickerVisible = remember { mutableStateOf(false) }

    val confirmDeleteAreaDialog = remember { mutableStateOf(false) }
    val confirmLeaveAreaDialog = remember { mutableStateOf(false) }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetBackgroundColor = colorResource(resource = SharedR.colors.main_background),
        sheetContent = {
            if (state.isLoading) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                        .padding(20.dp)
                ) { Loader(modifier = modifier.align(Alignment.Center)) }
            } else {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                ) {
                    state.item?.let { area ->
                        AreaDetailContent(
                            areaData = area,
                            isEditing = state.isEditing,
                            isEmojiPickerVisible = isEmojiPickerVisible,
                            onEmojiClick = {
                                viewModel.dispatch(AreaDetailAction.EmojiChanged(it))
                            },
                            onNameChanged = {
                                viewModel.dispatch(AreaDetailAction.NameChanged(it))
                            },
                            onDescriptionChanged = {
                                viewModel.dispatch(AreaDetailAction.DescriptionChanged(it))
                            }
                        )
                    }

                    BottomButtons(
                        isEditing = state.isEditing,
                        onEditClick = {
                            viewModel.dispatch(AreaDetailAction.StartEdit)
                        },
                        onLeaveClick = {
                            confirmLeaveAreaDialog.value = true
                        },
                        onDeleteClick = {
                            confirmDeleteAreaDialog.value = true
                        },
                        onSaveClick = {
                            isEmojiPickerVisible.value = false
                            viewModel.dispatch(AreaDetailAction.EndEdit)
                        }
                    )
                }
            }
        }
    ) {}

    SubmitDialogs(
        isDeleteAreaDialogVisible = confirmDeleteAreaDialog,
        isLeaveAreaDialogVisible = confirmLeaveAreaDialog,
        onConfirmDelete = {
            viewModel.dispatch(AreaDetailAction.DeleteClick)
        },
        onConfirmLeave = {
            viewModel.dispatch(AreaDetailAction.LeaveClick)
        }
    )

    LaunchedEffect(areaId) {
        viewModel.dispatch(AreaDetailAction.Load(areaId))
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest {
            when (it) {
                is AreaDetailEvent.DeleteSuccess -> {
                    modalSheetState.hide()
                }

                is AreaDetailEvent.LeaveSuccess -> {
                    modalSheetState.hide()
                }
            }
        }
    }
}
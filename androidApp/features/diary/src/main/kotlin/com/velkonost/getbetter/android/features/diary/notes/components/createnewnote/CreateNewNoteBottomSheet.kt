package com.velkonost.getbetter.android.features.diary.notes.components.createnewnote

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velkonost.getbetter.android.features.diary.notes.components.createnewnote.areapicker.AreaPicker
import com.velkonost.getbetter.android.features.diary.notes.components.createnewnote.subnotes.SubNotesBlock
import com.velkonost.getbetter.android.features.diary.notes.components.createnewnote.tags.TagsBlock
import com.velkonost.getbetter.core.compose.components.Loader
import com.velkonost.getbetter.core.compose.components.MultilineTextField
import com.velkonost.getbetter.shared.core.model.NoteType
import com.velkonost.getbetter.shared.features.diary.contracts.CreateNewNoteViewState
import com.velkonost.getbetter.shared.features.diary.model.SubNoteUI
import com.velkonost.getbetter.shared.resources.SharedR
import dev.icerock.moko.resources.compose.colorResource
import dev.icerock.moko.resources.compose.stringResource
import model.Area

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
fun CreateNewNoteBottomSheet(
    modifier: Modifier = Modifier,
    state: CreateNewNoteViewState,
    modalSheetState: ModalBottomSheetState,
    onAreaSelect: (Area) -> Unit,
    onTextChanged: (String) -> Unit,
    onPrivateChanged: (Boolean) -> Unit,
    onNewTagChanged: (String) -> Unit,
    onAddNewTag: () -> Unit,
    onTagDelete: (String) -> Unit,
    onNewSubNoteChanged: (String) -> Unit,
    onAddNewSubNote: () -> Unit,
    onSubNoteDelete: (SubNoteUI) -> Unit
) {

    val isAreaPickerVisible = remember { mutableStateOf(false) }
    val isSubNotesBlockVisible = remember { mutableStateOf(false) }

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
                ) {
                    Loader(modifier = modifier.align(Alignment.Center))
                }
            } else {

                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 100.dp)
                ) {
                    Column {
                        Text(
                            modifier = modifier.align(Alignment.CenterHorizontally),
                            text = stringResource(
                                resource =
                                if (state.type == NoteType.Default) SharedR.strings.create_note_title
                                else SharedR.strings.create_goal_title
                            ),
                            color = colorResource(resource = SharedR.colors.text_title),
                            style = MaterialTheme.typography.headlineSmall
                        )

                        AreaPicker(
                            areas = state.availableAreas,
                            selectedArea = state.selectedArea,
                            isAreaPickerVisible = isAreaPickerVisible,
                            onAreaSelect = onAreaSelect,
                            modalSheetState = modalSheetState,
                            noteType = state.type
                        )

                        MultilineTextField(
                            value = state.text,
                            placeholderText = stringResource(
                                resource =
                                if (state.type == NoteType.Default) SharedR.strings.create_note_text_hint
                                else SharedR.strings.create_goal_text_hint
                            ),
                            onValueChanged = { onTextChanged.invoke(it) }
                        )

                        PrivateSwitch(
                            isPrivate = state.isPrivate,
                            isEnable = state.selectedArea != null && state.selectedArea?.isPrivate == false,
                            onCheckedChange = onPrivateChanged
                        )

                        TagsBlock(
                            tags = state.tags,
                            newTag = state.newTag,
                            onNewTagChanged = onNewTagChanged,
                            onAddNewTag = onAddNewTag,
                            onTagDelete = onTagDelete
                        )

                        SubNotesBlock(
                            items = state.subNotes,
                            newSubNote = state.newSubNote,
                            isSubNotesBlockVisible = isSubNotesBlockVisible,
                            onAddNewSubNote = onAddNewSubNote,
                            onNewSubNoteChanged = onNewSubNoteChanged,
                            onSubNoteDelete = onSubNoteDelete
                        )
                    }
                }
            }
        }
    ) {

    }

    LaunchedEffect(modalSheetState.currentValue) {
        isAreaPickerVisible.value = false
        isSubNotesBlockVisible.value = false
    }
}
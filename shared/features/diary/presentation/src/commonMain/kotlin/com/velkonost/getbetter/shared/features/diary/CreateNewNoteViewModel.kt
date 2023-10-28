package com.velkonost.getbetter.shared.features.diary

import com.velkonost.getbetter.shared.core.model.NoteType
import com.velkonost.getbetter.shared.core.util.DatetimeFormatter.convertToServerDatetime
import com.velkonost.getbetter.shared.core.vm.BaseViewModel
import com.velkonost.getbetter.shared.core.vm.resource.Message
import com.velkonost.getbetter.shared.core.vm.resource.MessageType
import com.velkonost.getbetter.shared.features.diary.contracts.CreateNewNoteAction
import com.velkonost.getbetter.shared.features.diary.contracts.CreateNewNoteEvent
import com.velkonost.getbetter.shared.features.diary.contracts.CreateNewNoteViewState
import com.velkonost.getbetter.shared.features.diary.model.SubNoteUI
import com.velkonost.getbetter.shared.features.diary.model.TagUI
import com.velkonost.getbetter.shared.features.notes.api.NotesRepository
import com.velkonost.getbetter.shared.resources.SharedR
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import model.Area

class CreateNewNoteViewModel
internal constructor(
    private val notesRepository: NotesRepository
) : BaseViewModel<CreateNewNoteViewState, CreateNewNoteAction, Nothing, CreateNewNoteEvent>(
    initialState = CreateNewNoteViewState()
) {
    override fun dispatch(action: CreateNewNoteAction) = when (action) {
        is CreateNewNoteAction.InitAvailableAreas -> initAvailableAreas(action.value)
        is CreateNewNoteAction.OpenDefault -> obtainOpenDefault()
        is CreateNewNoteAction.OpenGoal -> obtainOpenGoal()
        is CreateNewNoteAction.AreaSelect -> obtainAreaSelect(action.value)
        is CreateNewNoteAction.TextChanged -> obtainTextChanged(action.value)
        is CreateNewNoteAction.PrivateChanged -> obtainPrivateChanged()
        is CreateNewNoteAction.NewTagTextChanged -> obtainNewTagTextChanged(action.value)
        is CreateNewNoteAction.AddNewTag -> addNewTag()
        is CreateNewNoteAction.RemoveTag -> removeTag(action.value)
        is CreateNewNoteAction.NewSubNoteTextChanged -> obtainNewSubNoteTextChanged(action.value)
        is CreateNewNoteAction.AddSubNote -> addSubNote()
        is CreateNewNoteAction.RemoveSubNote -> removeSubNote(action.value)
        is CreateNewNoteAction.AddImageUrl -> TODO()
        is CreateNewNoteAction.RemoveImageUrl -> TODO()
        is CreateNewNoteAction.SetCompletionDate -> obtainSetCompletionDate(action.value)
        is CreateNewNoteAction.CloseBecauseZeroAreas -> obtainZeroAreasError()
    }

    private fun initAvailableAreas(value: List<Area>) {
        emit(viewState.value.copy(availableAreas = value))
    }

    private fun obtainOpenDefault() {
        emit(
            viewState.value.copy(
                type = NoteType.Default,
                selectedArea = viewState.value.availableAreas.firstOrNull(),
                text = "",
                mediaUrls = emptyList(),
                tags = emptyList(),
                newTag = TagUI(),
                subNotes = emptyList(),
                newSubNote = SubNoteUI(),
                isPrivate = true
            )
        )
    }

    private fun obtainOpenGoal() {
        emit(
            viewState.value.copy(
                type = NoteType.Goal,
                selectedArea = viewState.value.availableAreas.firstOrNull(),
                text = "",
                mediaUrls = emptyList(),
                tags = emptyList(),
                newTag = TagUI(),
                subNotes = emptyList(),
                newSubNote = SubNoteUI(),
                isPrivate = true
            )
        )
    }

    private fun obtainAreaSelect(value: Area) {
        emit(viewState.value.copy(selectedArea = value))

        val forceSetPrivate = value.isPrivate
        if (forceSetPrivate) {
            emit(viewState.value.copy(isPrivate = true))
        }
    }

    private fun obtainTextChanged(value: String) {
        emit(viewState.value.copy(text = value))
    }

    private fun obtainPrivateChanged() {
        val prevValue = viewState.value.isPrivate
        emit(viewState.value.copy(isPrivate = !prevValue))
    }

    private fun obtainSetCompletionDate(value: Long?) {
        val serverValue = value?.convertToServerDatetime()
        emit(viewState.value.copy(completionDate = value))
    }

    private fun obtainNewTagTextChanged(value: String) {
        if (value.isNotEmpty() && value.last() == ' ') {
            addNewTag()
        } else {
            emit(
                viewState.value.copy(
                    newTag = TagUI(text = value.replace(" ", "").take(15))
                )
            )
        }
    }

    private fun addNewTag() {
        val tagText = viewState.value.newTag.text.replace(" ", "")
        val tagsList = viewState.value.tags

        if (tagsList.none { it.text == tagText } && tagText.isNotEmpty()) {
            emit(
                viewState.value.copy(
                    newTag = TagUI(),
                    tags = tagsList.plus(TagUI(text = tagText))
                )
            )
        } else {
            obtainNewTagTextChanged(tagText)
        }
    }

    private fun removeTag(value: String) {
        val tagsList = viewState.value.tags

        tagsList.firstOrNull { it.text == value }?.let {
            emit(viewState.value.copy(tags = tagsList.minus(it)))
        }
    }

    private fun obtainNewSubNoteTextChanged(value: String) {
        emit(viewState.value.copy(newSubNote = SubNoteUI(text = value)))
    }

    private fun addSubNote() {
        val subNote = viewState.value.newSubNote
        val subNotesList = viewState.value.subNotes

        if (!subNotesList.contains(subNote) && subNote.text.isNotEmpty()) {
            emit(
                viewState.value.copy(
                    newSubNote = SubNoteUI(),
                    subNotes = subNotesList.plus(subNote)
                )
            )
        }
    }

    private fun removeSubNote(value: SubNoteUI) {
        val subNotesList = viewState.value.subNotes

        emit(
            viewState.value.copy(
                subNotes = subNotesList.filter { it.id != value.id }
            )
        )
    }

    private fun obtainZeroAreasError() {
        val message = Message.Builder()
            .id("error_code_message")
            .text(StringDesc.Resource(SharedR.strings.create_note_error_no_areas))
            .messageType(MessageType.SnackBar.Builder().build())
            .build()
        emit(message)
    }

}
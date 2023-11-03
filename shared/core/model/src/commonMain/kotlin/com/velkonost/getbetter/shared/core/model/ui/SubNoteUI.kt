package com.velkonost.getbetter.shared.core.model.ui

import com.velkonost.getbetter.shared.core.model.note.SubNote
import com.velkonost.getbetter.shared.core.util.randomUUID

data class SubNoteUI(
    val id: String = randomUUID(),
    val text: String = ""
)

val List<SubNoteUI>.asExternalModels: List<SubNote>
    get() = map { it.asExternalModel }

val SubNoteUI.asExternalModel: SubNote
    get() = SubNote(
        id = 0,
        text = text,
        completionDate = null,
        expectedCompletionDate = null
    )

val SubNote.asUI: SubNoteUI
    get() = SubNoteUI(
        id = id.toString(),
        text = text
    )

val List<SubNote>.asUI: List<SubNoteUI>
    get() = map { it.asUI }
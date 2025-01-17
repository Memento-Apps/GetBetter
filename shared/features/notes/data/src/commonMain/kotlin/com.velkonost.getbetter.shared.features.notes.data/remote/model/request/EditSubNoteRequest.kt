package com.velkonost.getbetter.shared.features.notes.data.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditSubNoteRequest(
    @SerialName("noteId")
    val noteId: Int,

    @SerialName("subNoteId")
    val subNoteId: Int,

    @SerialName("subNoteText")
    val subNoteText: String,

    @SerialName("subNoteCompletionDate")
    val subNoteCompletionDate: Long?,

    @SerialName("subNoteExpectedCompletionDate")
    val subNoteExpectedCompletionDate: Long?
)
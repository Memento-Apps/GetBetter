package com.velkonost.getbetter.shared.features.notes.data.remote.model.response

import com.velkonost.getbetter.shared.core.model.likes.LikeType
import com.velkonost.getbetter.shared.core.model.likes.LikesData
import com.velkonost.getbetter.shared.core.model.note.Note
import com.velkonost.getbetter.shared.core.model.note.NoteType
import com.velkonost.getbetter.shared.features.areas.data.remote.model.response.KtorArea
import com.velkonost.getbetter.shared.features.areas.data.remote.model.response.asExternalModel
import com.velkonost.getbetter.shared.features.tasks.data.remote.model.response.KtorTask
import com.velkonost.getbetter.shared.features.tasks.data.remote.model.response.asExternalModel
import com.velkonost.getbetter.shared.features.userinfo.data.remote.model.response.KtorUserInfoShort
import com.velkonost.getbetter.shared.features.userinfo.data.remote.model.response.asExternalModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KtorNote(
    @SerialName("id")
    val id: Int = 0,

    @SerialName("text")
    val text: String = "",

    @SerialName("noteType")
    val noteType: String?,

    @SerialName("authorId")
    val authorId: String = "",

    @SerialName("author")
    val author: KtorUserInfoShort,

    @SerialName("createdDate")
    val createdDate: Long = 0L,

    @SerialName("completionDate")
    val completionDate: Long?,

    @SerialName("expectedCompletionDate")
    val expectedCompletionDate: Long?,

    @SerialName("expectedCompletionDateExpired")
    val expectedCompletionDateExpired: Boolean,

    @SerialName("media")
    val media: List<ByteArray>,

    @SerialName("tags")
    val tags: List<String> = emptyList(),

    @SerialName("isActive")
    val isActive: Boolean,

    @SerialName("isPrivate")
    val isPrivate: Boolean,

    @SerialName("area")
    val ktorArea: KtorArea,

    @SerialName("task")
    val ktorTask: KtorTask?,

    @SerialName("subNotes")
    val subNotes: List<KtorSubNote> = emptyList(),

    @SerialName("allowEdit")
    val allowEdit: Boolean,

    @SerialName("totalLikes")
    val totalLikes: Int,

    @SerialName("userLike")
    val userLike: String,

    @SerialName("canHide")
    val canHide: Boolean
)

fun KtorNote.asExternalModel() =
    Note(
        id = id,
        noteType = NoteType.entries.first { it.responseName == noteType },
        text = text,
        authorId = authorId,
        author = author.asExternalModel(),
        createdDate = createdDate,
        completionDate = completionDate,
        expectedCompletionDate = expectedCompletionDate,
        expectedCompletionDateExpired = expectedCompletionDateExpired,
        mediaUrls = emptyList(),
        tags = tags,
        isActive = isActive,
        isPrivate = isPrivate,
        subNotes = subNotes.asExternalModel(),
        area = ktorArea.asExternalModel(),
        task = ktorTask?.asExternalModel(),
        allowEdit = allowEdit,
        allowHide = canHide,
        likesData = LikesData(
            totalLikes = totalLikes,
            userLike = LikeType.entries.first { it.responseName == userLike }
        )
    )
package remote.model.response

import com.velkonost.getbetter.shared.core.model.NoteType
import com.velkonost.getbetter.shared.features.notes.api.model.Note
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

    @SerialName("createdDate")
    val createdDate: Long = 0L,

    @SerialName("completionDate")
    val completionDate: Long?,

    @SerialName("media")
    val media: List<ByteArray>,

    @SerialName("tags")
    val tags: List<String> = emptyList(),

    @SerialName("isActive")
    val isActive: Boolean,

    @SerialName("isPrivate")
    val isPrivate: Boolean,

    @SerialName("areaId")
    val areaId: Int?,

    @SerialName("subNotes")
    val subNotes: List<KtorSubNote> = emptyList()
)

fun KtorNote.asExternalModel() =
    Note(
        id = id,
        noteType = NoteType.values().first { it.responseName == noteType },
        text = text,
        authorId = authorId,
        createdDate = createdDate,
        completionDate = completionDate,
        mediaUrls = emptyList(),
        tags = tags,
        isActive = isActive,
        isPrivate = isPrivate,
        subNotes = subNotes.asExternalModel(),
    )
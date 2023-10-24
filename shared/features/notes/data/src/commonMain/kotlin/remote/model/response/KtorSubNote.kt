package remote.model.response

import com.velkonost.getbetter.shared.features.notes.api.model.SubNote
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KtorSubNote(
    @SerialName("id")
    val id: Int = 0,

    @SerialName("text")
    val text: String = "",

    @SerialName("completionDate")
    val completionDate: Long = 0L
)

fun KtorSubNote.asExternalModel() =
    SubNote(
        id = id,
        text = text,
        completionDate = completionDate
    )

fun List<KtorSubNote>.asExternalModel() = this.map { it.asExternalModel() }
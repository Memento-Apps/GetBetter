package com.velkonost.getbetter.shared.features.feedback.data.remote.model.response

import com.velkonost.getbetter.shared.core.model.feedback.Feedback
import com.velkonost.getbetter.shared.core.model.feedback.FeedbackMessage
import com.velkonost.getbetter.shared.core.model.feedback.FeedbackStatus
import com.velkonost.getbetter.shared.core.model.feedback.FeedbackType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KtorFeedback(
    @SerialName("id")
    val id: Int?,

    @SerialName("type")
    val type: String?,

    @SerialName("datetime")
    val datetime: Long?,

    @SerialName("status")
    val status: String?,

    @SerialName("messages")
    val messages: List<KtorFeedbackMessage>
)

@Serializable
data class KtorFeedbackMessage(
    @SerialName("fromUser")
    val fromUser: Boolean,

    @SerialName("text")
    val text: String?,

    @SerialName("datetime")
    val datetime: Long?
)

fun List<KtorFeedback>.asExternalModel() = map { it.asExternalModel() }

fun KtorFeedback.asExternalModel() =
    Feedback(
        id = id,
        type = FeedbackType.entries.first { it.responseName == type },
        status = FeedbackStatus.entries.first { it.responseName == status },
        datetime = datetime,
        messages = messages.map { it.asExternalModel() }
    )

fun KtorFeedbackMessage.asExternalModel() =
    FeedbackMessage(
        fromUser = fromUser,
        text = text!!,
        datetime = datetime!!
    )

package com.velkonost.getbetter.shared.features.feedback.data.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddAnswerFeedbackRequest(
    @SerialName("feedbackId")
    val feedbackId: Int,

    @SerialName("text")
    val text: String
)
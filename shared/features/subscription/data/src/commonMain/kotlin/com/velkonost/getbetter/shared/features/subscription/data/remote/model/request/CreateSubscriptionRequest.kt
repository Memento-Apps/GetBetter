package com.velkonost.getbetter.shared.features.subscription.data.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSubscriptionRequest(
    @SerialName("subscriptionType")
    val subscriptionType: String
)
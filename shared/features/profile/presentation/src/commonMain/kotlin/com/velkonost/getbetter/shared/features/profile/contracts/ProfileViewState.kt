package com.velkonost.getbetter.shared.features.profile.contracts

import com.velkonost.getbetter.shared.core.model.profile.UIThemeMode
import com.velkonost.getbetter.shared.core.model.subscription.Subscription
import com.velkonost.getbetter.shared.core.model.user.ExperienceData
import com.velkonost.getbetter.shared.core.network.PRIVACY_URL
import com.velkonost.getbetter.shared.core.network.TERMS_URL
import com.velkonost.getbetter.shared.core.vm.contracts.UIContract

data class ProfileViewState(
    val isLoading: Boolean = false,
    val isLogoutLoading: Boolean = false,
    val userName: String = "",
    val avatarUrl: String? = null,
    val experienceData: ExperienceData? = null,
    val selectedTheme: UIThemeMode = UIThemeMode.SystemTheme,
    val isUserAnonymous: Boolean = true,

    val subscriptionData: SubscriptionData = SubscriptionData(),

    val privacyLink: String = PRIVACY_URL,
    val termsLink: String = TERMS_URL
) : UIContract.State


data class SubscriptionData(
    val available: Boolean = false,
    val subscription: Subscription? = Subscription.NoSubscription
)
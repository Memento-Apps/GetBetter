package com.velkonost.getbetter.shared.core.vm.navigation

sealed class NavigationScreen(val route: String) {
    data object SplashNavScreen : NavigationScreen(SPLASH_DESTINATION)

    data object OnboardingNavScreen : NavigationScreen(ONBOARDING_DESTINATION)

    data object AuthNavScreen : NavigationScreen(
        "$AUTH_DESTINATION/?$ARG_IDENTIFY_ANONYMOUS={$ARG_IDENTIFY_ANONYMOUS}&$ARG_GO_PAYWALL={$ARG_GO_PAYWALL}"
    )

    data object SocialNavScreen : NavigationScreen(SOCIAL_DESTINATION)
    data object DiaryNavScreen : NavigationScreen(DIARY_DESTINATION)
    data object AddAreaNavScreen : NavigationScreen(ADD_AREA_DESTINATION)
    data object AreaDetailNavScreen : NavigationScreen(AREA_DETAIL_DESTINATION)
    data object NoteDetailNavScreen : NavigationScreen(
        "$NOTE_DETAIL_DESTINATION/?$ARG_NOTE={$ARG_NOTE}"
    )

    data object TaskDetailNavScreen : NavigationScreen(
        "$TASK_DETAIL_DESTINATION/?$ARG_TASK={$ARG_TASK}"
    )

    data object CalendarsNavScreen : NavigationScreen(CALENDARS_DESTINATION)
    data object WisdomNavScreen : NavigationScreen(WISDOM_DESTINATION)

    data object ProfileNavScreen : NavigationScreen(PROFILE_DESTINATION)

    data object SettingsNavScreen : NavigationScreen(SETTINGS_DESTINATION)

    data object FeedbackNavScreen : NavigationScreen(FEEDBACK_DESTINATION)

    data object AbilitiesNavScreen : NavigationScreen(ABILITIES_DESTINATION)

    data object AbilityDetailsNavScreen : NavigationScreen(
        "$ABILITY_DETAIL_DESTINATION/?$ARG_ABILITY={$ARG_ABILITY}&$ARG_IS_FAVORITE={$ARG_IS_FAVORITE}"
    )

    data object SubscriptionNavScreen : NavigationScreen(
        "$SUBSCRIPTION_DESTINATION/?$ARG_RETURN_TO_PROFILE={$ARG_RETURN_TO_PROFILE}"
    )
}

const val NAV_PREFIX: String = "com.velkonost.getbetter"

const val SPLASH_DESTINATION: String = "$NAV_PREFIX.splash/SplashScreen"
const val ONBOARDING_DESTINATION: String = "$NAV_PREFIX.splash/OnboardingScreen"
const val AUTH_DESTINATION: String = "$NAV_PREFIX.auth/AuthScreen"

const val SOCIAL_DESTINATION: String = "$NAV_PREFIX.social/SocialScreen"

const val DIARY_DESTINATION: String = "$NAV_PREFIX.diary/DiaryScreen"
const val ADD_AREA_DESTINATION: String = "$NAV_PREFIX.diary/AddAreaScreen"
const val AREA_DETAIL_DESTINATION: String = "$NAV_PREFIX.diary/AreaDetailScreen"
const val NOTE_DETAIL_DESTINATION: String = "$NAV_PREFIX.diary/NoteDetailScreen"
const val TASK_DETAIL_DESTINATION: String = "$NAV_PREFIX.diary/TaskDetailScreen"

const val CALENDARS_DESTINATION: String = "$NAV_PREFIX.calendars/CalendarsScreen"
const val WISDOM_DESTINATION: String = "$NAV_PREFIX.wisdom/WisdomScreen"

const val PROFILE_DESTINATION: String = "$NAV_PREFIX.profile/ProfileScreen"
const val SETTINGS_DESTINATION: String = "$NAV_PREFIX.profile/SettingsScreen"
const val FEEDBACK_DESTINATION: String = "$NAV_PREFIX.profile/FeedbackScreen"

const val ABILITIES_DESTINATION: String = "$NAV_PREFIX.abilities/AbilitiesScreen"
const val ABILITY_DETAIL_DESTINATION: String = "$NAV_PREFIX.abilities/AbilityDetailsScreen"

const val SUBSCRIPTION_DESTINATION: String = "$NAV_PREFIX.subscription/SubscriptionScreen"

const val ARG_NOTE: String = "arg_note"
const val ARG_TASK: String = "arg_note"
const val ARG_ABILITY: String = "arg_ability"
const val ARG_IS_FAVORITE: String = "arg_is_favorite"
const val ARG_IDENTIFY_ANONYMOUS: String = "arg_identify_anonymous"
const val ARG_GO_PAYWALL: String = "arg_go_paywall"
const val ARG_RETURN_TO_PROFILE: String = "arg_return_to_profile"






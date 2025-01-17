package com.velkonost.getbetter.shared.features.settings.presentation.contract

import com.velkonost.getbetter.shared.core.vm.contracts.UIContract
import com.velkonost.getbetter.shared.core.vm.navigation.NavigationEvent

sealed interface SettingsAction: UIContract.Action {

    data object ChangePasswordClick : SettingsAction

    data object DeleteAccountConfirm : SettingsAction

    data object SaveNameClick : SettingsAction

    data class NameChanged(val value: String) : SettingsAction

    data object NavigateBack : SettingsAction, SettingsNavigation {
        override val event: NavigationEvent = NavigationEvent.NavigateUp()
    }
}

sealed interface ChangePasswordAction : SettingsAction {
    data class OldPasswordChanged(val value: String) : SettingsAction

    data class NewPasswordChanged(val value: String) : SettingsAction

    data class RepeatedNewPasswordChanged(val value: String) : SettingsAction

    data object ChangeClick : SettingsAction
}
package com.velkonost.getbetter.shared.features.settings.presentation.contract

import com.velkonost.getbetter.shared.core.vm.contracts.UIContract

data class SettingsViewState(
    val isLoading: Boolean = false
): UIContract.State
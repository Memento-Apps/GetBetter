package com.velkonost.getbetter.shared.features.areadetail.presentation.contract

import com.velkonost.getbetter.shared.core.vm.contracts.UIContract

sealed interface AreaDetailEvent : UIContract.Event {
    data object DeleteSuccess : AreaDetailEvent
    data object LeaveSuccess : AreaDetailEvent
}
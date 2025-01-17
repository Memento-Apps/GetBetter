package com.velkonost.getbetter.shared.features.feedback.presentation.contract

import com.velkonost.getbetter.shared.core.model.feedback.FeedbackType
import com.velkonost.getbetter.shared.core.vm.contracts.UIContract
import com.velkonost.getbetter.shared.core.vm.navigation.NavigationEvent

sealed interface FeedbackAction : UIContract.Action {
    data object NavigateBack : FeedbackAction, FeedbackNavigation {
        override val event: NavigationEvent = NavigationEvent.NavigateUp()
    }

    data class DetailsClick(val feedbackId: Int) : FeedbackAction
}

sealed interface NewFeedbackAction : FeedbackAction {
    data class TypeChanged(val value: FeedbackType) : NewFeedbackAction

    data class TextChanged(val value: String) : NewFeedbackAction

    data object CreateClick : NewFeedbackAction
}

sealed interface FeedbackAnswerAction : FeedbackAction {
    data class AnswerTextChanged(val value: String) : FeedbackAction

    data object SendAnswerClick : FeedbackAction
}
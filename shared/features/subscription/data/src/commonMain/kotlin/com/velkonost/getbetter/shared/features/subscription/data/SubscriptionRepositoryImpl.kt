package com.velkonost.getbetter.shared.features.subscription.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.velkonost.getbetter.shared.core.datastore.ALLOW_SUBSCRIPTION
import com.velkonost.getbetter.shared.core.datastore.RESUME_SUBSCRIPTION_SUGGESTED
import com.velkonost.getbetter.shared.core.datastore.SESSION_NUMBER
import com.velkonost.getbetter.shared.core.datastore.TRIAL_SUGGESTED
import com.velkonost.getbetter.shared.core.datastore.extension.getUserToken
import com.velkonost.getbetter.shared.core.model.subscription.Subscription
import com.velkonost.getbetter.shared.core.network.model.RemoteResponse
import com.velkonost.getbetter.shared.core.network.model.ResponseStatus
import com.velkonost.getbetter.shared.core.util.ResultState
import com.velkonost.getbetter.shared.core.util.flowRequest
import com.velkonost.getbetter.shared.features.subscription.api.SubscriptionRepository
import com.velkonost.getbetter.shared.features.subscription.api.model.SubscriptionPayment
import com.velkonost.getbetter.shared.features.subscription.data.remote.SubscriptionRemoteDataSource
import com.velkonost.getbetter.shared.features.subscription.data.remote.model.request.CreateSubscriptionRequest
import com.velkonost.getbetter.shared.features.subscription.data.remote.model.response.KtorCreateSubscription
import com.velkonost.getbetter.shared.features.subscription.data.remote.model.response.KtorSubscription
import com.velkonost.getbetter.shared.features.subscription.data.remote.model.response.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SubscriptionRepositoryImpl(
    private val localDataSource: DataStore<Preferences>,
    private val remoteDataSource: SubscriptionRemoteDataSource
) : SubscriptionRepository {

    override fun createSubscription(subscriptionType: String): Flow<ResultState<SubscriptionPayment>> =
        flowRequest(
            mapper = KtorCreateSubscription::asExternalModel,
            request = {
                markResumeSubscriptionAsNotSuggested()

                val token = localDataSource.getUserToken()
                val body = CreateSubscriptionRequest(subscriptionType)
                remoteDataSource.createSubscription(token, body)
            }
        )

    override fun startTrial(): Flow<ResultState<Subscription>> = flowRequest(
        mapper = KtorSubscription::asExternalModel,
        request = {
            markTrialAsSuggested()

            val token = localDataSource.getUserToken()
            remoteDataSource.startTrial(token)
        }
    )

    override fun cancelAutoRenewal(): Flow<ResultState<Subscription>> = flowRequest(
        mapper = KtorSubscription::asExternalModel,
        request = {
            val token = localDataSource.getUserToken()
            remoteDataSource.cancelAutoRenewal(token)
        }
    )

    override fun isServiceAvailable(): Flow<ResultState<Boolean>> = flowRequest(
        request = {
            val localData = localDataSource.data.first()[ALLOW_SUBSCRIPTION] ?: false
            if (!localData) {
                return@flowRequest RemoteResponse(ResponseStatus(code = 200), data = localData)
            }

            val token = localDataSource.getUserToken()
            remoteDataSource.isSubscriptionsAvailable(token)
        }
    )

    override fun fetchSubscriptionStatus(): Flow<ResultState<Subscription>> = flowRequest(
        mapper = KtorSubscription::asExternalModel,
        request = {
            val token = localDataSource.getUserToken()
            remoteDataSource.getSubscriptionStatus(token)
        }
    )

    override fun canCreateArea(): Flow<ResultState<Boolean>> = flowRequest(
        mapper = { !this },
        request = {
            val token = localDataSource.getUserToken()
            remoteDataSource.isAreasLimitReached(token)
        }
    )

    override suspend fun shouldSuggestTrial(): Boolean {
        val data = localDataSource.data.first()
        val sessionNumber = data[SESSION_NUMBER] ?: 0
        val trialSuggested = data[TRIAL_SUGGESTED] ?: false

        return !trialSuggested && sessionNumber % 3 == 0
    }

    override suspend fun shouldSuggestResumeSubscription(): Boolean {
        return localDataSource.data.first()[RESUME_SUBSCRIPTION_SUGGESTED] ?: false
    }

    override suspend fun markResumeSubscriptionAsSuggested() {
        localDataSource.edit { preferences ->
            preferences[RESUME_SUBSCRIPTION_SUGGESTED] = true
        }
    }

    private suspend fun markTrialAsSuggested() {
        localDataSource.edit { preferences ->
            preferences[TRIAL_SUGGESTED] = true
        }
    }

    private suspend fun markResumeSubscriptionAsNotSuggested() {
        localDataSource.edit { preferences ->
            preferences[RESUME_SUBSCRIPTION_SUGGESTED] = false
        }
    }

}
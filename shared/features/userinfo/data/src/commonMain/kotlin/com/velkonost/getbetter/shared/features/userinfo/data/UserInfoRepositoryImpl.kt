package com.velkonost.getbetter.shared.features.userinfo.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.velkonost.getbetter.shared.core.datastore.TOKEN_KEY
import com.velkonost.getbetter.shared.core.datastore.USER_REGISTRATION_MILLIS
import com.velkonost.getbetter.shared.core.datastore.extension.getUserToken
import com.velkonost.getbetter.shared.core.datastore.extension.resetStates
import com.velkonost.getbetter.shared.core.model.user.UserInfo
import com.velkonost.getbetter.shared.core.model.user.UserInfoShort
import com.velkonost.getbetter.shared.core.util.ResultState
import com.velkonost.getbetter.shared.core.util.flowRequest
import com.velkonost.getbetter.shared.core.util.locale
import com.velkonost.getbetter.shared.features.userinfo.api.UserInfoRepository
import com.velkonost.getbetter.shared.features.userinfo.data.remote.UserInfoRemoteDataSource
import com.velkonost.getbetter.shared.features.userinfo.data.remote.model.request.BlockUserRequest
import com.velkonost.getbetter.shared.features.userinfo.data.remote.model.request.ChangePasswordRequest
import com.velkonost.getbetter.shared.features.userinfo.data.remote.model.request.InitSettingsRequest
import com.velkonost.getbetter.shared.features.userinfo.data.remote.model.request.UpdateValueRequest
import com.velkonost.getbetter.shared.features.userinfo.data.remote.model.response.KtorUserInfo
import com.velkonost.getbetter.shared.features.userinfo.data.remote.model.response.KtorUserInfoShort
import com.velkonost.getbetter.shared.features.userinfo.data.remote.model.response.asExternalModel
import kotlinx.coroutines.flow.Flow

class UserInfoRepositoryImpl(
    private val remoteDataSource: UserInfoRemoteDataSource,
    private val localDataSource: DataStore<Preferences>
) : UserInfoRepository {

    override suspend fun fetchInfo(): Flow<ResultState<UserInfo>> =
        flowRequest(
            mapper = KtorUserInfo::asExternalModel,
            request = {
                val token = localDataSource.getUserToken()
                remoteDataSource.getInfo(token)
            },
        )

    override suspend fun fetchInfoAboutOtherUser(userId: String): Flow<ResultState<UserInfoShort>> =
        flowRequest(
            mapper = KtorUserInfoShort::asExternalModel,
            request = {
                val token = localDataSource.getUserToken()
                remoteDataSource.getShortInfo(token, userId)
            }
        )

    override suspend fun initSettings(email: String): Flow<ResultState<UserInfo>> = flowRequest(
        mapper = KtorUserInfo::asExternalModel,
        request = {
            val token = localDataSource.getUserToken()
            var name = email.substringBefore("@")
            if (name.isEmpty()) name = "Anonymous"

            val body = InitSettingsRequest(
                name = name,
                locale = locale
            )

            remoteDataSource.initSettings(token, body)
        },
        onSuccess = {
            saveRegistrationDate(it.registrationDate)
        }
    )

    override suspend fun updateRegistrationTime(): Flow<ResultState<UserInfo>> = flowRequest(
        mapper = KtorUserInfo::asExternalModel,
        request = {
            val token = localDataSource.getUserToken()
            remoteDataSource.updateRegistrationDate(token)
        }
    )

    override suspend fun updateLastLogin(): Flow<ResultState<UserInfo>> = flowRequest(
        mapper = KtorUserInfo::asExternalModel,
        request = {
            val token = localDataSource.getUserToken()
            remoteDataSource.updateLastLogin(token)
        }
    )

    override suspend fun updateLocale(locale: String): Flow<ResultState<UserInfo>> = flowRequest(
        mapper = KtorUserInfo::asExternalModel,
        request = {
            val token = localDataSource.getUserToken()
            val body = UpdateValueRequest(locale)
            remoteDataSource.updateLocale(token, body)
        }
    )

    override suspend fun updateName(newName: String): Flow<ResultState<UserInfo>> = flowRequest(
        mapper = KtorUserInfo::asExternalModel,
        request = {
            val token = localDataSource.getUserToken()
            val body = UpdateValueRequest(newName)
            remoteDataSource.updateName(token, body)
        }
    )

    override suspend fun updateAvatarUrl(
        fileContent: ByteArray
    ): Flow<ResultState<UserInfo>> = flowRequest(
        mapper = KtorUserInfo::asExternalModel,
        request = {
            val token = localDataSource.getUserToken()
            remoteDataSource.updateAvatar(token!!, fileContent)
        }
    )

    override suspend fun logout(): Flow<ResultState<UserInfo>> = flowRequest(
        mapper = KtorUserInfo::asExternalModel,
        request = {
            val token = localDataSource.getUserToken()
            remoteDataSource.performLogout(token)
        },
        onSuccess = {
            localDataSource.resetStates()
            localDataSource.edit { preferences ->
                preferences.remove(TOKEN_KEY)
            }
        }
    )

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        newRepeatedPassword: String
    ): Flow<ResultState<UserInfo>> = flowRequest(
        mapper = KtorUserInfo::asExternalModel,
        request = {
            val token = localDataSource.getUserToken()
            val body = ChangePasswordRequest(oldPassword, newPassword, newRepeatedPassword)
            remoteDataSource.changePassword(token, body)
        }
    )

    override suspend fun deleteAccount(): Flow<ResultState<Unit>> = flowRequest(
        mapper = { },
        request = {
            val token = localDataSource.getUserToken()

            localDataSource.resetStates()
            localDataSource.edit { preferences ->
                preferences.remove(TOKEN_KEY)
            }

            remoteDataSource.deleteAccount(token)
        }
    )

    override suspend fun blockUser(userId: String): Flow<ResultState<UserInfo>> = flowRequest(
        mapper = KtorUserInfo::asExternalModel,
        request = {
            val token = localDataSource.getUserToken()
            val body = BlockUserRequest(userId)
            remoteDataSource.blockUser(token, body)
        }
    )

    private suspend fun saveRegistrationDate(value: Long) {
        localDataSource.edit { preferences ->
            preferences[USER_REGISTRATION_MILLIS] = value
        }
    }

}
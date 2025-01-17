package com.velkonost.getbetter.shared.features.diary.api

import com.velkonost.getbetter.shared.core.util.ResultState
import kotlinx.coroutines.flow.Flow

interface DiaryRepository {

    suspend fun saveUpdatedNoteId(noteId: Int)

    suspend fun getUpdatedNoteId(): Flow<ResultState<Int>>

    suspend fun checkNeedsResetState(): Boolean

    suspend fun shouldShowNotesHint(): Boolean

    suspend fun shouldShowAreasHint(): Boolean

    suspend fun shouldShowTasksHint(): Boolean

    suspend fun shouldShowCreateAreaHint(): Boolean


}
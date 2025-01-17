import com.velkonost.getbetter.shared.core.model.area.Area
import com.velkonost.getbetter.shared.core.util.ResultState
import kotlinx.coroutines.flow.Flow

interface AreasRepository {

    fun fetchAllAreas(
        page: Int,
        perPage: Int
    ): Flow<ResultState<List<Area>>>

    fun editArea(
        id: Int,
        name: String,
        description: String,
        emojiId: Int? = null,
        imageUrl: String? = null
    ): Flow<ResultState<Area>>

    fun createNewArea(
        name: String,
        description: String,
        isPrivate: Boolean,
        requiredLevel: Int,
        emojiId: Int? = null,
        imageUrl: String? = null
    ): Flow<ResultState<Area>>

    fun deleteArea(areaId: Int): Flow<ResultState<Area>>

    fun leaveArea(areaId: Int): Flow<ResultState<Area>>

    fun fetchUserAreas(): Flow<ResultState<List<Area>>>

    fun addUserArea(areaId: Int): Flow<ResultState<Area>>

    suspend fun fetchPublicAreasToAdd(
        page: Int,
        perPage: Int
    ): Flow<ResultState<List<Area>>>

    fun fetchAreaDetails(areaId: Int): Flow<ResultState<Area>>
}
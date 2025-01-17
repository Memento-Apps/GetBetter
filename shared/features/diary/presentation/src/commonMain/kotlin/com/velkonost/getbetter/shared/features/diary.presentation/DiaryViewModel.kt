package com.velkonost.getbetter.shared.features.diary.presentation

import AreasRepository
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.velkonost.getbetter.shared.core.model.EntityType
import com.velkonost.getbetter.shared.core.model.area.Area
import com.velkonost.getbetter.shared.core.model.hint.UIHint
import com.velkonost.getbetter.shared.core.model.likes.LikeType
import com.velkonost.getbetter.shared.core.model.likes.LikesData
import com.velkonost.getbetter.shared.core.model.note.Note
import com.velkonost.getbetter.shared.core.model.task.TaskUI
import com.velkonost.getbetter.shared.core.util.PagingConfig
import com.velkonost.getbetter.shared.core.util.isLoading
import com.velkonost.getbetter.shared.core.util.onSuccess
import com.velkonost.getbetter.shared.core.vm.BaseViewModel
import com.velkonost.getbetter.shared.features.createnote.api.CreateNoteRepository
import com.velkonost.getbetter.shared.features.createnote.presentation.CreateNewNoteViewModel
import com.velkonost.getbetter.shared.features.createnote.presentation.contract.CreateNewNoteAction
import com.velkonost.getbetter.shared.features.createnote.presentation.contract.CreateNewNoteEvent
import com.velkonost.getbetter.shared.features.diary.api.DiaryRepository
import com.velkonost.getbetter.shared.features.diary.presentation.contracts.AddAreaClick
import com.velkonost.getbetter.shared.features.diary.presentation.contracts.AreaLikeClick
import com.velkonost.getbetter.shared.features.diary.presentation.contracts.CreateNewAreaAction
import com.velkonost.getbetter.shared.features.diary.presentation.contracts.DiaryAction
import com.velkonost.getbetter.shared.features.diary.presentation.contracts.DiaryEvent
import com.velkonost.getbetter.shared.features.diary.presentation.contracts.DiaryNavigation
import com.velkonost.getbetter.shared.features.diary.presentation.contracts.DiaryViewState
import com.velkonost.getbetter.shared.features.diary.presentation.contracts.NavigateToAddArea
import com.velkonost.getbetter.shared.features.diary.presentation.contracts.NavigateToNoteDetail
import com.velkonost.getbetter.shared.features.diary.presentation.contracts.NavigateToPaywall
import com.velkonost.getbetter.shared.features.diary.presentation.contracts.NavigateToTaskDetail
import com.velkonost.getbetter.shared.features.diary.presentation.contracts.NoteClick
import com.velkonost.getbetter.shared.features.diary.presentation.contracts.NoteLikeClick
import com.velkonost.getbetter.shared.features.diary.presentation.contracts.TaskClick
import com.velkonost.getbetter.shared.features.diary.presentation.model.DiaryTab
import com.velkonost.getbetter.shared.features.likes.api.LikesRepository
import com.velkonost.getbetter.shared.features.notes.api.NotesRepository
import com.velkonost.getbetter.shared.features.subscription.api.SubscriptionRepository
import com.velkonost.getbetter.shared.features.subscription.domain.CheckSubscriptionUseCase
import com.velkonost.getbetter.shared.features.tasks.api.TasksRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DiaryViewModel
internal constructor(
    private val areasRepository: AreasRepository,
    private val notesRepository: NotesRepository,
    private val tasksRepository: TasksRepository,
    private val diaryRepository: DiaryRepository,
    private val likesRepository: LikesRepository,
    private val createNoteRepository: CreateNoteRepository,
    private val subscriptionRepository: SubscriptionRepository,
    private val checkSubscriptionUseCase: CheckSubscriptionUseCase
) : BaseViewModel<DiaryViewState, DiaryAction, DiaryNavigation, DiaryEvent>(
    initialState = DiaryViewState()
) {

    private val _notesPagingConfig = PagingConfig()
    private var notesLoadingJob: Job? = null

    private val notesLikesJobsMap: HashMap<Int, Job> = hashMapOf()
    private val areasLikesJobsMap: HashMap<Int, Job> = hashMapOf()
    private val tasksFavoriteJobsMap: HashMap<Int, Job> = hashMapOf()

    fun refreshData() {
        fetchAreas()
        fetchTasks()
        checkSubscription()

        launchJob {
            if (diaryRepository.checkNeedsResetState()) {
                refreshNotesState()
            } else {
                checkUpdatedNote()
            }

            fetchNotes()
        }
    }

    override fun init() {
        launchJob {
            createNewAreaViewModel.value.viewState.collect {
                emit(viewState.value.copy(createNewAreaViewState = it))
            }
        }

        launchJob {
            createNewAreaViewModel.value.events.collect {
                emit(it)
            }
        }

        launchJob {
            createNewNoteViewModel.value.viewState.collect {
                emit(viewState.value.copy(createNewNoteViewState = it))
            }
        }

        launchJob {
            createNewNoteViewModel.value.events.collect {
                when (it) {
                    is CreateNewNoteEvent.CreatedSuccess -> emit(DiaryEvent.NewNoteCreatedSuccess)
                }
            }
        }
    }

    @NativeCoroutinesState
    val createNewAreaViewModel: StateFlow<CreateNewAreaViewModel> =
        MutableStateFlow(CreateNewAreaViewModel(areasRepository, diaryRepository))

    @NativeCoroutinesState
    val createNewNoteViewModel: StateFlow<CreateNewNoteViewModel> =
        MutableStateFlow(
            CreateNewNoteViewModel(
                notesRepository,
                diaryRepository,
                createNoteRepository
            )
        )

    override fun dispatch(action: DiaryAction) = when (action) {
        is CreateNewAreaAction -> dispatchCreateNewAreaAction(action)
        is AddAreaClick -> emit(NavigateToAddArea)
        is NoteClick -> obtainNoteClick(action.value)
        is TaskClick -> obtainTaskClick(action.value)
        is NoteLikeClick -> obtainNoteLikeClick(action.value)
        is AreaLikeClick -> obtainAreaLikeClick(action.value)
        is DiaryAction.NotesLoadNextPage -> fetchNotes()
        is DiaryAction.TaskFavoriteClick -> obtainTaskFavorite(action.value)
        is DiaryAction.TasksListUpdateClick -> fetchTasks(forceUpdate = true)
        is DiaryAction.HintClick -> showHint(firstTime = action.firstTime, index = action.index)
        is DiaryAction.NavigateToPaywallClick -> emit(NavigateToPaywall)
        is DiaryAction.StartTrialClick -> obtainStartTrial()
    }

    fun dispatch(action: CreateNewNoteAction) = dispatchCreateNewNoteAction(action)

    private fun checkSubscription() {
        checkAreasLimit()

        launchJob {
            val shouldSuggestTrial = subscriptionRepository.shouldSuggestTrial()

            checkSubscriptionUseCase() collectAndProcess {
                onSuccess { result ->
                    result?.let {
                        val tasksViewState =
                            viewState.value.tasksViewState.copy(canUpdateList = it.isActive)
                        emit(
                            viewState.value.copy(
                                tasksViewState = tasksViewState,
                                showAds = !it.isActive || it.fake,
                            )
                        )

                        if (shouldSuggestTrial && !it.isActive && !it.trialUsed) {
                            emit(DiaryEvent.SuggestTrial)
                        }
                    }
                }
            }
        }
    }

    private fun checkAreasLimit() {
        launchJob {
            subscriptionRepository.canCreateArea() collectAndProcess {
                onSuccess { result ->
                    result?.let {
                        val areasViewState =
                            viewState.value.areasViewState.copy(canCreateNewArea = it)
                        emit(viewState.value.copy(areasViewState = areasViewState))
                    }
                }
            }
        }
    }

    private fun showHint(firstTime: Boolean = false, index: Int) {
        val selectedTab = DiaryTab.entries[index]
        val uiHint = when (selectedTab) {
            DiaryTab.Notes -> UIHint.DiaryNotes
            DiaryTab.Areas -> UIHint.DiaryAreas
            DiaryTab.Tasks -> UIHint.DiaryTasks
        }

        if (firstTime) {
            launchJob {
                val shouldShow = when (selectedTab) {
                    DiaryTab.Notes -> diaryRepository.shouldShowNotesHint()
                    DiaryTab.Areas -> diaryRepository.shouldShowAreasHint()
                    DiaryTab.Tasks -> diaryRepository.shouldShowTasksHint()
                }

                if (shouldShow) {
                    uiHint.send()
                }
            }
        } else uiHint.send()
    }

    private fun obtainStartTrial() {
        launchJob {
            subscriptionRepository.startTrial() collectAndProcess {
                isLoading {
                    emit(viewState.value.copy(isTrialLoading = it))
                }

                onSuccess { result ->
                    result?.let {
                        val tasksViewState =
                            viewState.value.tasksViewState.copy(canUpdateList = it.isActive)
                        emit(
                            viewState.value.copy(
                                tasksViewState = tasksViewState,
                                showAds = !it.isActive || it.fake,
                            )
                        )
                    }
                }
            }
        }
    }

    private fun obtainNoteLikeClick(value: Note) {
        if (notesLikesJobsMap.containsKey(value.id)) return

        launchJob {
            val likeType = when (value.likesData.userLike) {
                LikeType.Positive -> LikeType.None
                else -> LikeType.Positive
            }

            likesRepository.addLike(
                entityType = EntityType.Note,
                entityId = value.id,
                likeType = likeType
            ) collectAndProcess {
                isLoading {
                    val itemLikesData = value.likesData.copy(isLikesLoading = true)

                    val indexOfChangedItem =
                        viewState.value.notesViewState.items.indexOfFirst { item -> item.id == value.id }

                    val allItems = viewState.value.notesViewState.items.toMutableList()

                    if (indexOfChangedItem != -1) {
                        allItems[indexOfChangedItem] = value.copy(likesData = itemLikesData)
                    }

                    val notesViewState = viewState.value.notesViewState.copy(items = allItems)
                    emit(viewState.value.copy(notesViewState = notesViewState))
                }
                onSuccess { entityLikes ->
                    entityLikes?.let {
                        val itemLikesData =
                            LikesData(
                                totalLikes = it.total,
                                userLike = it.userLikeType
                            )

                        val indexOfChangedItem =
                            viewState.value.notesViewState.items.indexOfFirst { item -> item.id == value.id }

                        val allItems = viewState.value.notesViewState.items.toMutableList()

                        if (indexOfChangedItem != -1) {
                            allItems[indexOfChangedItem] = value.copy(likesData = itemLikesData)
                        }

                        val notesViewState =
                            viewState.value.notesViewState.copy(items = allItems)
                        emit(viewState.value.copy(notesViewState = notesViewState))
                    }
                }
            }
        }.also {
            notesLikesJobsMap[value.id] = it
        }.invokeOnCompletion {
            notesLikesJobsMap.remove(value.id)
        }
    }

    private fun obtainTaskFavorite(value: TaskUI) {
        if (tasksFavoriteJobsMap.containsKey(value.id)) return

        launchJob {
            val tasksViewState = viewState.value.tasksViewState
            val request = if (tasksViewState.favoriteItems.any { it.id == value.id }) {
                tasksRepository.removeFromFavorite(taskId = value.id!!)
            } else tasksRepository.addToFavorite(taskId = value.id!!)

            request collectAndProcess {
                isLoading {
                    val indexOfChangedItemInFavorite =
                        viewState.value.tasksViewState.favoriteItems.indexOfFirst { item -> item.id == value.id }
                    val indexOfChangedItemInCurrent =
                        viewState.value.tasksViewState.currentItems.indexOfFirst { item -> item.id == value.id }

                    val favoriteItems = viewState.value.tasksViewState.favoriteItems.toMutableList()
                    val currentItems = viewState.value.tasksViewState.currentItems.toMutableList()

                    if (indexOfChangedItemInFavorite != -1) {
                        favoriteItems[indexOfChangedItemInFavorite] =
                            value.copy(isFavoriteLoading = it)
                    }

                    if (indexOfChangedItemInCurrent != -1) {
                        currentItems[indexOfChangedItemInCurrent] =
                            value.copy(isFavoriteLoading = it)
                    }

                    val updatedTasksViewState = viewState.value.tasksViewState.copy(
                        favoriteItems = favoriteItems,
                        currentItems = currentItems
                    )
                    emit(viewState.value.copy(tasksViewState = updatedTasksViewState))
                }
                onSuccess {
                    fetchTasks()
                }
            }
        }.also {
            tasksFavoriteJobsMap[value.id!!] = it
        }.invokeOnCompletion {
            tasksFavoriteJobsMap.remove(value.id)
        }
    }

    private fun obtainAreaLikeClick(value: Area) {
        if (areasLikesJobsMap.containsKey(value.id)) return

        launchJob {
            val likeType = when (value.likesData.userLike) {
                LikeType.Positive -> LikeType.None
                else -> LikeType.Positive
            }

            likesRepository.addLike(
                entityType = EntityType.Area,
                entityId = value.id,
                likeType = likeType
            ) collectAndProcess {
                isLoading {
                    val itemLikesData = value.likesData.copy(isLikesLoading = true)

                    val indexOfChangedItem =
                        viewState.value.areasViewState.items.indexOfFirst { item -> item.id == value.id }

                    val allItems = viewState.value.areasViewState.items.toMutableList()

                    if (indexOfChangedItem != -1) {
                        allItems[indexOfChangedItem] = value.copy(likesData = itemLikesData)
                    }

                    val areasViewState = viewState.value.areasViewState.copy(items = allItems)
                    emit(viewState.value.copy(areasViewState = areasViewState))
                }
                onSuccess { entityLikes ->
                    entityLikes?.let {
                        val itemLikesData =
                            LikesData(
                                totalLikes = it.total,
                                userLike = it.userLikeType
                            )

                        val indexOfChangedItem =
                            viewState.value.areasViewState.items.indexOfFirst { item -> item.id == value.id }

                        val allItems = viewState.value.areasViewState.items.toMutableList()

                        if (indexOfChangedItem != -1) {
                            allItems[indexOfChangedItem] = value.copy(likesData = itemLikesData)
                        }

                        val areasViewState =
                            viewState.value.areasViewState.copy(items = allItems)
                        emit(viewState.value.copy(areasViewState = areasViewState))
                    }
                }
            }
        }.also {
            areasLikesJobsMap[value.id] = it
        }.invokeOnCompletion {
            areasLikesJobsMap.remove(value.id)
        }
    }

    private fun refreshNotesState() {
        _notesPagingConfig.page = 0
        _notesPagingConfig.lastPageReached = false
        val notesViewState = viewState.value.notesViewState.copy(
            isLoading = true,
            items = emptyList()
        )
        emit(viewState.value.copy(notesViewState = notesViewState))
    }

    private fun dispatchCreateNewAreaAction(action: CreateNewAreaAction) {
        createNewAreaViewModel.value.dispatch(action)
    }

    private fun dispatchCreateNewNoteAction(action: CreateNewNoteAction) {
        createNewNoteViewModel.value.dispatch(action)
    }

    private fun obtainNoteClick(value: Note) {
        launchJob {
            diaryRepository.saveUpdatedNoteId(value.id)
            emit(NavigateToNoteDetail(value))
        }
    }

    private fun obtainTaskClick(value: TaskUI) {
        launchJob {
            emit(NavigateToTaskDetail(value))
        }
    }

    private fun checkUpdatedNote() {
        launchJob {
            diaryRepository.getUpdatedNoteId()
                .collect { updatedNoteResult ->
                    updatedNoteResult.onSuccess { noteId ->
                        noteId?.let { refreshNoteData(it) }
                    }
                }
        }
    }

    private fun refreshNoteData(noteId: Int) {
        launchJob {
            notesRepository.getNoteDetails(noteId) collectAndProcess {
                onSuccess { note ->
                    note?.let {
                        val indexOfChangedItem =
                            viewState.value.notesViewState.items.indexOfFirst { it.id == note.id }
                        val allItems =
                            viewState.value.notesViewState.items.toMutableList()

                        if (indexOfChangedItem == -1 && note.allowEdit) {
                            allItems.add(0, note)
                        } else if (!note.isActive) {
                            allItems.removeAt(indexOfChangedItem)
                        } else {
                            allItems[indexOfChangedItem] = note
                        }

                        allItems.filter { it.area.id == note.area.id }.forEach {
                            it.area = note.area
                        }

                        val notesViewState =
                            viewState.value.notesViewState.copy(
                                isLoading = false,
                                items = allItems
                            )
                        emit(viewState.value.copy(notesViewState = notesViewState))
                    }
                }
            }
        }
    }

    private fun fetchAreas() {
        launchJob {
            areasRepository.fetchUserAreas() collectAndProcess {
                isLoading {
                    if (viewState.value.areasViewState.items.isEmpty()) {
                        val areasViewState =
                            viewState.value.areasViewState.copy(isLoading = it)
                        emit(viewState.value.copy(areasViewState = areasViewState))
                    }
                }
                onSuccess { list ->
                    list?.let {
                        val areasViewState = viewState.value.areasViewState.copy(items = it)
                        emit(viewState.value.copy(areasViewState = areasViewState))

                        createNewNoteViewModel.value.dispatch(
                            CreateNewNoteAction.InitAvailableAreas(it)
                        )
                    }

                }
            }
        }
    }

    private fun fetchTasks(forceUpdate: Boolean = false) {
        launchJob {
            tasksRepository.getCurrentList(forceUpdate) collectAndProcess {
                isLoading {
                    val tasksViewState = viewState.value.tasksViewState.copy(isLoading = it)
                    emit(viewState.value.copy(tasksViewState = tasksViewState))
                }
                onSuccess { list ->
                    list?.let {
                        val tasksViewState = viewState.value.tasksViewState.copy(
                            favoriteItems = list.filter { it.isFavorite },
                            currentItems = list.filter { !it.isFavorite }
                        )
                        emit(viewState.value.copy(tasksViewState = tasksViewState))

                        createNewNoteViewModel.value.dispatch(
                            CreateNewNoteAction.InitTasksList(list)
                        )
                    }
                }
            }
        }

        launchJob {
            tasksRepository.getCompletedTasks() collectAndProcess {
                onSuccess { list ->
                    list?.let {
                        val tasksViewState = viewState.value.tasksViewState.copy(
                            completedItems = list
                        )
                        emit(viewState.value.copy(tasksViewState = tasksViewState))
                    }
                }
            }
        }
    }

    private fun fetchNotes() {
        if (_notesPagingConfig.lastPageReached || notesLoadingJob?.isActive == true) return

        notesLoadingJob?.cancel()
        notesLoadingJob = launchJob {
            notesRepository.fetchUserNotes(
                page = _notesPagingConfig.page,
                perPage = _notesPagingConfig.pageSize
            ) collectAndProcess {
                isLoading {
                    val notesViewState = viewState.value.notesViewState.copy(isLoading = true)
                    emit(viewState.value.copy(notesViewState = notesViewState))
                }
                onSuccess { items ->
                    _notesPagingConfig.lastPageReached = items.isNullOrEmpty()
                    _notesPagingConfig.page++

                    items?.let {
                        val allItems = viewState.value.notesViewState.items.plus(it)
                        val notesViewState =
                            viewState.value.notesViewState.copy(
                                isLoading = false,
                                items = allItems
                            )
                        emit(viewState.value.copy(notesViewState = notesViewState))
                    }
                }
            }
        }
    }
}
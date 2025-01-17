package com.velkonost.getbetter.shared.features.diary.presentation.di

import com.velkonost.getbetter.shared.features.diary.presentation.DiaryViewModel
import org.koin.core.Koin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal actual val DiaryPresentationPlatformModule = module {
    singleOf(::DiaryViewModel)
}

@Suppress("unused")
val Koin.DiaryViewModel: DiaryViewModel
    get() = get()
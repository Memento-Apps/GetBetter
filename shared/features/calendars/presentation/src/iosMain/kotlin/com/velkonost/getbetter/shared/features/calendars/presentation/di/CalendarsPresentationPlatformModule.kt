package com.velkonost.getbetter.shared.features.calendars.presentation.di

import com.velkonost.getbetter.shared.features.calendars.presentation.CalendarsViewModel
import org.koin.core.Koin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal actual val CalendarsPresentationPlatformModule = module {
    singleOf(::CalendarsViewModel)
}

@Suppress("unused")
val Koin.CalendarsViewModel: CalendarsViewModel
    get() = get()
package com.velkonost.getbetter.shared.features.calendars.presentation.di

import com.velkonost.getbetter.shared.features.calendars.presentation.CalendarsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal actual val CalendarsPresentationPlatformModule = module {
    viewModelOf(::CalendarsViewModel)
}
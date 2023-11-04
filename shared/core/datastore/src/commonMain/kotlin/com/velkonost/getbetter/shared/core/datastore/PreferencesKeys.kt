package com.velkonost.getbetter.shared.core.datastore

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

val TOKEN_KEY = stringPreferencesKey("auth_token")

val UPDATED_NOTE_ID = intPreferencesKey("updated_note_id")
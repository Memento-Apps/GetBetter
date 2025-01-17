package com.velkonost.getbetter.shared.core.util.extension

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.decodeBase64
import okio.ByteString.Companion.encodeUtf8

@PublishedApi
internal val json = Json {
    encodeDefaults = true
    isLenient = true
    useArrayPolymorphism = true
}

inline fun <reified T> T.encodeToString() =
    json.encodeToString(this).encodeUtf8().base64()

inline fun <reified T> String.decodeFromString() =
    json.decodeFromString<T>(this.decodeBase64()?.utf8() ?: this)

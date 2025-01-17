package com.velkonost.getbetter.util

import java.util.Locale

/**
 * Util to check if the project run on Linux or Mac operating system
 *
 * @return true if the operating system is one of them
 */
fun isLinuxOrMacOs(): Boolean {
    val osName = System.getProperty("os.name").lowercase(Locale.ROOT)
    return listOf("linux", "mac os", "macos").contains(osName)
}

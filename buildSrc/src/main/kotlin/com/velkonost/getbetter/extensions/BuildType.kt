package com.velkonost.getbetter.extensions

internal interface BuildType {
    val isMinifyEnabled: Boolean
    val enableUnitTestCoverage: Boolean
    val enableAndroidTestCoverage: Boolean
}

internal object BuildTypeDebug : BuildType {
    override val isMinifyEnabled = false
    override val enableUnitTestCoverage = true
    override val enableAndroidTestCoverage = false

    const val applicationIdSuffix = ".debug"
    const val versionNameSuffix = "-DEBUG"
}

internal object BuildTypeRelease : BuildType {
    override val isMinifyEnabled = false
    override val enableUnitTestCoverage = false
    override val enableAndroidTestCoverage = false
}

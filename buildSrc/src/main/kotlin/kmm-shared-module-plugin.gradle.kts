@file:Suppress("UnstableApiUsage", "UNUSED_VARIABLE", "DSL_SCOPE_VIOLATION")

import com.velkonost.getbetter.util.libs

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("io.kotest.multiplatform")

    id("com.velkonost.getbetter.checks.detekt")
    id("com.velkonost.getbetter.checks.ktlint")
    id("com.velkonost.getbetter.checks.spotless")
}

version = libs.versions.project.version.get()

repositories {
    applyDefault()
}

kotlin {
    jvmToolchain(18)

    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets.all {
        languageSettings.apply {
            optIn("kotlin.RequiresOptIn")
            optIn("kotlin.time.ExperimentalTime")
            optIn("kotlinx.coroutines.FlowPreview")
            optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            optIn("kotlinx.serialization.ExperimentalSerializationApi")
            optIn("kotlin.experimental.ExperimentalObjCName")
        }
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.koin.test)
                implementation(libs.testing.turbine)
                implementation(libs.testing.kotest.framework.engine)
                implementation(libs.testing.kotest.framework.datatest)
                implementation(libs.testing.kotest.assertions.core)
                implementation(libs.testing.kotest.assertions.json)
                implementation(libs.testing.kotest.property)
            }
        }
        val androidMain by getting {
            dependencies {
                dependsOn(commonMain)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.testing.kotest.runner.junit5)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    filter {
        isFailOnNoMatchingTests = false
    }

    testLogging {
        showExceptions = true
        showStandardStreams = true
        events = setOf(
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

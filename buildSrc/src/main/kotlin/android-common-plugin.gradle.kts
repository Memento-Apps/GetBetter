@file:Suppress("UnstableApiUsage")

import com.velkonost.getbetter.util.libs

plugins {
    id("com.android.library")
    kotlin("android")

    id("com.velkonost.getbetter.checks.detekt")
    id("com.velkonost.getbetter.checks.ktlint")
    id("com.velkonost.getbetter.checks.spotless")
}

repositories {
    applyDefault()
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }

    sourceSets.all {
        java.srcDirs("src/$name/kotlin")
    }
}

kotlin {
    sourceSets.all {
        languageSettings.apply {
            optIn("kotlin.RequiresOptIn")
            optIn("androidx.compose.animation.ExperimentalAnimationApi")
            optIn("androidx.compose.material3.ExperimentalMaterial3Api")
            optIn("androidx.compose.ui.unit.ExperimentalUnitApi")
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

@file:Suppress("DSL_SCOPE_VIOLATION")

import com.velkonost.getbetter.SHARED_PACKAGE
import com.velkonost.getbetter.join

plugins {
    `kmm-shared-module-plugin`
    alias(libs.plugins.ksp)
    alias(libs.plugins.nativecoroutines)
}

android {
    namespace = SHARED_PACKAGE.join(
        projects.shared.features,
        projects.shared.features.profiledetail,
        projects.shared.features.profiledetail.presentation
    )
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.shared.core.vm)
                api(projects.shared.core.util)

                implementation(libs.koin.core)
                implementation(libs.kotlinx.coroutines.core)

                api(projects.shared.core.model)
                implementation(projects.shared.resources)

                implementation(projects.shared.features.notes.api)
                implementation(projects.shared.features.follows.api)
                implementation(projects.shared.features.userinfo.api)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.koin.androidx.compose)
            }
        }
    }
}
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
        projects.shared.features.wisdom,
        projects.shared.features.wisdom.presentation
    )
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.shared.core.vm)
                api(projects.shared.resources)

                implementation(libs.koin.core)
                implementation(libs.kotlinx.coroutines.core)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.koin.androidx.compose)
            }
        }
    }
}
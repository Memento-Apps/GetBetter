import com.velkonost.getbetter.ANDROID_PACKAGE
import com.velkonost.getbetter.join
import com.velkonost.getbetter.util.libs

plugins {
    `android-ui-plugin`
}

android {
    namespace = ANDROID_PACKAGE.join(
        projects.androidApp.features,
        projects.androidApp.features.subscription
    )
}

dependencies {
    implementation(projects.shared.core.util)
    implementation(projects.shared.core.model)
    implementation(projects.shared.resources)
    implementation(projects.shared.features.subscription.presentation)

    implementation(projects.androidApp.core.compose)
    implementation(libs.android.compose.lootie)
    implementation(libs.yookassa.android)
}

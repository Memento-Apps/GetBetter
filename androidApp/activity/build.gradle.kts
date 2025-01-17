import com.velkonost.getbetter.ANDROID_PACKAGE
import com.velkonost.getbetter.join

plugins {
    `android-ui-plugin`
}

android {
    namespace = ANDROID_PACKAGE.join(projects.androidApp.activity)
}

dependencies {
    implementation(projects.androidApp.core.compose)

    implementation(projects.androidApp.features.auth)
    implementation(projects.androidApp.features.splash)
    implementation(projects.androidApp.features.onboarding)
    implementation(projects.androidApp.features.social)
    implementation(projects.androidApp.features.diary)
    implementation(projects.androidApp.features.addarea)
    implementation(projects.androidApp.features.calendars)
    implementation(projects.androidApp.features.wisdom)
    implementation(projects.androidApp.features.profile)
    implementation(projects.androidApp.features.notedetail)
    implementation(projects.androidApp.features.taskdetail)
    implementation(projects.androidApp.features.feedback)
    implementation(projects.androidApp.features.settings)
    implementation(projects.androidApp.features.abilities)
    implementation(projects.androidApp.features.abilitydetails)
    implementation(projects.androidApp.features.subscription)

    implementation(projects.shared.core.vm)
    implementation(projects.shared.core.util)
    implementation(projects.shared.resources)

    implementation(libs.firebase.android.analytics)
}

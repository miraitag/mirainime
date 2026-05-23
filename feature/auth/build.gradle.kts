plugins {
    id("mirainime.android.library")
}

android {
    namespace = "com.miraitag.mirainime.feature.auth"
    compileSdk {
        version = release(36)
    }
}

dependencies {
    // Unit Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

plugins {
    id("mirainime.android.library")
}

android {
    namespace = "com.miraitag.mirainime.feature.auth"
}

dependencies {
    // Unit Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

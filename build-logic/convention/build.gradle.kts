plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "mirainime.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "mirainime.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("firebase") {
            id = "mirainime.android.firebase"
            implementationClass = "AndroidFirebaseConventionPlugin"
        }
    }
}
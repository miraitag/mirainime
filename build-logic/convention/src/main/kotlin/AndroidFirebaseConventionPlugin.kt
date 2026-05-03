import com.miraitag.mirainime.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.gms.google-services")
                apply("com.google.firebase.crashlytics")
            }

            dependencies {
                add("implementation", platform(libs.findLibrary("firebase-bom").get()))
                add("implementation", libs.findLibrary("firebase-analytics").get())
                add("implementation", libs.findLibrary("firebase-auth").get())
                add("implementation", libs.findLibrary("firebase-crashlytics").get())
            }
        }
    }
}
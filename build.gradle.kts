// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
//    kotlin("jvm") version Dependencies.Kotlin.version apply false
//    id("org.jetbrains.dokka") version Dependencies.Kotlin.dokkaVersion apply false
//    id("com.android.library") version "4.1.1" apply false
//    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
    id("com.vanniktech.maven.publish") version "0.25.3" apply false
}

subprojects {

    pluginManager.withPlugin("java") {
        configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }

}
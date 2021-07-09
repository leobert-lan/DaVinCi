plugins {
    id("com.android.library")
    id("kotlin-android")
    kotlin("kapt")
}

android {
//configure<com.android.build.gradle.internal.dsl.BaseAppModuleExtension> {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    defaultConfig {
        minSdkVersion(26)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        this.dataBinding = true
    }

}

dependencies {

    compileOnly(project(":davinci"))
//    implementation 'osp.leobert.android:davinci:0.0.1'


    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:${Dependencies.Kotlin.version}")
    compileOnly("androidx.appcompat:appcompat:1.3.0")
    compileOnly("androidx.core:core-ktx:1.3.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}

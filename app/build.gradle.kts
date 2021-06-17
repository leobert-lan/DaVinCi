plugins {
    id("com.android.application")
    id("com.google.devtools.ksp") //version Dependencies.Kotlin.Ksp.version
    id("kotlin-android")
//    kotlin("jvm")
    kotlin("kapt")
}

android {
//configure<com.android.build.gradle.internal.dsl.BaseAppModuleExtension> {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    defaultConfig {
        applicationId = "com.example.simpletest"
        minSdkVersion(26)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        this.dataBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
    }
}

ksp {
//    arg("autoserviceKsp.verify", "true")
    arg("daVinCi.verbose", "true")
    arg("daVinCi.pkg", "com.examole.simpletest")
    arg("daVinCi.module", "App")
}

dependencies {
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")

    implementation(project(":davinci"))
//    implementation 'osp.leobert.android:davinci:0.0.1'


    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Dependencies.Kotlin.version}")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.core:core-ktx:1.3.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")

    ksp(project(":anno_ksp"))
    implementation(project(":annotation"))

//    ksp("dev.zacsweers.autoservice:auto-service-ksp:0.5.2")
}

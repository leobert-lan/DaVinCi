plugins {
    id("com.android.library")
    id("kotlin-android")
    kotlin("kapt")
//    id("osp.leobert.maven.publish")
}

android {
//configure<com.android.build.gradle.internal.dsl.BaseAppModuleExtension> {
    compileSdkVersion(32)
//    buildToolsVersion("30.0.3")
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    defaultConfig {
        minSdkVersion(26)
        targetSdkVersion(32)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        this.dataBinding = true
    }

}

dependencies {

    implementation("com.google.android.material:material:1.4.0")
    compileOnly(project(":davinci"))
    compileOnly(project(":annotation"))


    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:${Dependencies.Kotlin.version}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0-native-mt")
    compileOnly("androidx.core:core-ktx:1.6.0")

    implementation("io.github.leobert-lan:pandora:0.0.9")
    implementation("io.github.leobert-lan:pandorarv_kt:0.0.4")
    implementation("androidx.recyclerview:recyclerview:1.2.0")

    implementation("androidx.appcompat:appcompat:1.4.0")
}

//EasyPublish {
//    sourceSet = android.sourceSets.named("main").get().java.srcDirs
////    docClassPathAppend = project.files(android.bootClasspath.joinToString(
////        separator = File.pathSeparator
////    ) {
////        it.name
////    }).asPath
//
//    docExcludes = arrayListOf("osp/leobert/android/davinci/*")
//
//    artifact {
//        value = "build/outputs/aar/davinci_styles_viewer-release.aar"
//    }
//
//    description =
//        "An Android library to help create background drawable and ColorStateList without xml"
//
//    developer {
//
//        this.id = "leobert"
//        this.name = "leobert"
//        this.email = "leobert.l@hotmail.com"
//    }
//
//    groupId = "io.github.leobert-lan"
//    artifactId = "davinci-style-viewer"
//    version = "0.0.2"
//    packaging = "aar"
//    siteUrl = "https://github.com/leobert-lan/DaVinCi"
//    gitUrl = "https://github.com/leobert-lan/DaVinCi.git"
//    licenseName = "MIT"
//    licenseUrl = "https://github.com/leobert-lan/DaVinCi/blob/master/LICENSE"
//
//    mavenRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
//
//}
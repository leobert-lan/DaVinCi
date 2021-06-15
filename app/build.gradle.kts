plugins {
    id("com.android.application")
    id("com.google.devtools.ksp") version Dependencies.Kotlin.Ksp.version
    kotlin("jvm")
    kotlin("kapt")
    id("kotlin-android")
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.2")

    defaultConfig {
        applicationId = "com.example.simpletest"
        minSdkVersion(26)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
    }
}

//android {
//
//    dataBinding {
//        enabled = true
//    }
//
//    compileSdkVersion 29
//    buildToolsVersion "29.0.2"
//    defaultConfig {
//        applicationId "com.example.simpletest"
//        minSdkVersion 26
//        targetSdkVersion 29
//        versionCode 1
//        versionName "1.0"
//        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
//    }
//    buildTypes {
//        release {
//            minifyEnabled false
//            proguardFiles getDefaultProguardFile ('proguard-android-optimize.txt'), 'proguard-rules.pro'
//        }
//    }
//}

dependencies {
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")

    implementation(project(":davinci"))
//    implementation 'osp.leobert.android:davinci:0.0.1'


    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Dependencies.Kotlin.version}")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.core:core-ktx:1.3.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")

//    ksp project(':anno_ksp')
}

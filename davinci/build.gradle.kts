plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
//    id("osp.leobert.maven.publish")
}


android {
    compileSdk = 34

    buildFeatures {
        this.dataBinding = true
    }
    defaultConfig {
        minSdk = 14
        namespace = "osp.leobert.android.davinci"
//        targetSdk = 34
//        versionCode = 1
//        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        unitTests {
            this.isIncludeAndroidResources = true
            this.isReturnDefaultValues = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

kapt {
    arguments {
//        arg("includeCompileClasspath", true)

        arg("module", "DaVinCi-")
        arg("mode", "mode_file")
        arg("active_reporter", "on")
    }
}

dependencies {
    implementation(libs.jetbrains.kotlin.stdlib)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)

    implementation(libs.jetbrains.kotlinx.coroutines.core)
    implementation(libs.jetbrains.kotlinx.coroutines.android)


    testImplementation(libs.junit)
    testImplementation(libs.com.google.guava.guava.testlib)
    testImplementation(libs.robolectric.robolectric)
    testImplementation(libs.mockito.mockito.core)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.io.github.leobert.lan.reporter.review)
    kapt(libs.report.anno.compiler)
    kapt(libs.io.github.leobert.lan.reporter.review)

    implementation(libs.io.github.leobert.lan.clz.diagram.reporter)
    kapt(libs.io.github.leobert.lan.clz.diagram.reporter)
}

apply(plugin = "com.vanniktech.maven.publish")

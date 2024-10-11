plugins {
    id("com.android.application")
//    id("com.google.devtools.ksp") //version Dependencies.Kotlin.Ksp.version
    id("kotlin-android")
    kotlin("kapt")
}


android {
    compileSdk = 34
//    buildToolsVersion("30.0.3")
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    defaultConfig {
        configurations.all {
            resolutionStrategy {
                force("androidx.core:core-ktx:1.6.0")
            }
        }
        applicationId = "com.example.simpletest"
        namespace = "com.example.sinpletest"
//        minSdkVersion(26)
        minSdk = 26
//        targetSdkVersion(32)
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        flavorDimensions.add("versionCode")
    }
    buildFeatures {
        this.dataBinding = true
    }

    buildTypes {
        getByName("release") {
            sourceSets {
                getByName("main") {
                    java.srcDir(File("build/generated/ksp/release/kotlin"))
                }
            }
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            kapt {
                this.arguments {
                    this.arg("daVinCi.verbose", "true")
                    this.arg("daVinCi.pkg", "com.example.simpletest")
                    this.arg("daVinCi.module", "App")
                    this.arg("daVinCi.preview", "false")
                }
            }
        }

        getByName("debug").apply {

            sourceSets {
                getByName("main") {
                    java.srcDir(File("build/generated/ksp/debug/kotlin"))
                    res {
                        srcDirs(File("src/main/res/preview"))
                    }
                }
            }

            kapt {
                this.arguments {
                    this.arg("daVinCi.verbose", "true")
                    this.arg("daVinCi.pkg", "com.example.simpletest")
                    this.arg("daVinCi.module", "App")
                    this.arg("daVinCi.preview", "true")
                }
            }
        }
    }
}

//ksp {
//    arg("daVinCi.verbose", "true")
//    arg("daVinCi.pkg", "com.examole.simpletest")
//    arg("daVinCi.module", "App")
//    arg("daVinCi.preview", "true")
//}

val dev = true

dependencies {
    implementation(libs.androidx.constraintlayout)

    //fix: Duplicate class com.google.common.util.concurrent.ListenableFuture found in modules
    // jetified-guava-23.5-jre (com.google.guava:guava:23.5-jre)
    // and jetified-listenablefuture-1.0 (com.google.guava:listenablefuture:1.0)
    implementation(libs.listenablefuture)


    implementation(libs.jetbrains.kotlin.stdlib)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)

    testImplementation(libs.junit)
    testImplementation(libs.com.google.testing.compile.compile.testing9)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    if (dev) {
        debugImplementation(project(":davinci_styles_viewer"))
        implementation(project(":davinci"))
    } else {
        debugImplementation(libs.davinci.style.viewer)
        implementation(libs.davinci)
    }

    implementation(libs.davinci.anno)
//    ksp(Dependencies.DaVinVi.ksp)
    kapt(libs.davinci.anno.ksp)

//    implementation(project(":ide-preview"))
}

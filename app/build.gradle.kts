plugins {
    id("com.android.application")
//    id("com.google.devtools.ksp") //version Dependencies.Kotlin.Ksp.version
    id("kotlin-android")
    kotlin("kapt")
}


android {
    compileSdkVersion(31)
    buildToolsVersion("30.0.3")
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
        minSdkVersion(26)
        targetSdkVersion(30)
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
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")

    //fix: Duplicate class com.google.common.util.concurrent.ListenableFuture found in modules
    // jetified-guava-23.5-jre (com.google.guava:guava:23.5-jre)
    // and jetified-listenablefuture-1.0 (com.google.guava:listenablefuture:1.0)
    implementation("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")


    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Dependencies.Kotlin.version}")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("androidx.core:core-ktx:1.6.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    if (dev) {
        debugImplementation(project(":davinci_styles_viewer"))
        implementation(project(":davinci"))
    } else {
        implementation(Dependencies.DaVinCi.api)
        debugImplementation(Dependencies.DaVinCi.viewer)
    }

    implementation(Dependencies.DaVinCi.annotation)
//    ksp(Dependencies.DaVinVi.ksp)
    kapt(Dependencies.DaVinCi.ksp)

    implementation(project(":ide-preview"))
}

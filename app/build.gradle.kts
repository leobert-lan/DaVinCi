plugins {
    id("com.android.application")
    id("com.google.devtools.ksp") //version Dependencies.Kotlin.Ksp.version
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
        }

        getByName("debug").apply {

            sourceSets {
                getByName("main") {
                    java.srcDir(File("build/generated/ksp/debug/kotlin"))
                }
            }

            dependencies {
//                ksp(project(":anno_ksp"))
            }
        }
    }
}

ksp {
    arg("daVinCi.verbose", "true")
    arg("daVinCi.pkg", "com.examole.simpletest")
    arg("daVinCi.module", "App")
}

dependencies {
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")



    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Dependencies.Kotlin.version}")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.core:core-ktx:1.3.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")



    implementation(Dependencies.DaVinVi.annotation)
    ksp(Dependencies.DaVinVi.ksp)
    implementation(Dependencies.DaVinVi.api)
    
//    implementation(project(":davinci"))
//    ksp(project(":anno_ksp"))
    //kotlin注解
//    implementation(project(":annotation"))
    //java注解
//    implementation(project(":annotation-java"))
}

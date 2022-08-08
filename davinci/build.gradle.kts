plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
//    id("osp.leobert.maven.publish")
}


android {
    compileSdkVersion(32)

    buildFeatures {
        this.dataBinding = true
    }
    defaultConfig {
        minSdkVersion(14)
        targetSdkVersion(32)
        versionCode = 1
        versionName = "1.0"
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Dependencies.Kotlin.version}")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("androidx.core:core-ktx:1.6.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0-native-mt")


    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.guava:guava-testlib:18.0")
    testImplementation("org.robolectric:robolectric:3.8")
    testImplementation("org.mockito:mockito-core:2.7.14")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    implementation("io.github.leobert-lan:reporter-review:1.0.1")
    kapt("io.github.leobert-lan:report-anno-compiler:1.1.4")
    kapt("io.github.leobert-lan:reporter-review:1.0.1")

    implementation("io.github.leobert-lan:class-diagram-reporter:1.0.1")
    kapt("io.github.leobert-lan:class-diagram-reporter:1.0.1")
}


//EasyPublish {
//    sourceSet = android.sourceSets.named("main").get().java.srcDirs
//    docClassPathAppend = project.files(android.bootClasspath.joinToString(
//        separator = File.pathSeparator
//    ) {
//        it.name
//    }).asPath
//
//    docExcludes = arrayListOf("osp/leobert/android/davinci/*")
//
//    artifact {
//        value = "build/outputs/aar/davinci-release.aar"
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
//    artifactId = "davinci"
//    version = "0.0.8-alpha1"
//    packaging = "aar"
//    siteUrl = "https://github.com/leobert-lan/DaVinCi"
//    gitUrl = "https://github.com/leobert-lan/DaVinCi.git"
//    licenseName = "MIT"
//    licenseUrl = "https://github.com/leobert-lan/DaVinCi/blob/master/LICENSE"
//
//    mavenRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
//}
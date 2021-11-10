import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("osp.leobert.maven.publish")
}


android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    buildFeatures {
        this.dataBinding = true
    }
    defaultConfig {
        minSdkVersion(14)
        targetSdkVersion(30)
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
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.core:core-ktx:1.6.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")

    implementation("io.github.leobert-lan:reporter-review:1.0.1")
    kapt("io.github.leobert-lan:report-anno-compiler:1.1.4")
    kapt("io.github.leobert-lan:reporter-review:1.0.1")
}


EasyPublish {
    sourceSet = android.sourceSets.named("main").get().java.srcDirs
    docClassPathAppend = project.files(android.bootClasspath.joinToString(
        separator = File.pathSeparator
    ) {
        it.name
    }).asPath

    docExcludes = arrayListOf("osp/leobert/android/davinci/*")

    artifact {
        value = "build/outputs/aar/davinci-release.aar"
    }

    description =
        "An Android library to help create background drawable and ColorStateList without xml"

    developer {

        this.id = "leobert"
        this.name = "leobert"
        this.email = "leobert.l@hotmail.com"
    }

    groupId = "io.github.leobert-lan"
    artifactId = "davinci"
    version = "0.0.7"
    packaging = "aar"
    siteUrl = "https://github.com/leobert-lan/DaVinCi"
    gitUrl = "https://github.com/leobert-lan/DaVinCi.git"
    licenseName = "MIT"
    licenseUrl = "https://github.com/leobert-lan/DaVinCi/blob/master/LICENSE"

    mavenRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
}
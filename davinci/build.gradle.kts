plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
//    id("osp.leobert.maven.publish")
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
//        consumerProguardFiles = "consumer-rules.pro"
//        consumerProguardFiles.add()
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Dependencies.Kotlin.version}")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.core:core-ktx:1.3.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}

//configure<osp.leobert.maven.publish.bean.EasyPublish> {
////EasyPublish {
//    sourceSet = android.sourceSets.main.java.srcDirs
//    docClassPathAppend = project.files(android.getBootClasspath().join(File.pathSeparator)).asPath
//    docExcludes = ["osp/leobert/android/davinci/*"]
//    artifact {
//        value = "build/outputs/aar/davinci-release.aar"
//    }
//
//    description =
//        'An Android library to help create background drawable and ColorStateList without xml'
//    developer(object: groovy.lang.Closure<osp.leobert.maven.publish.bean.EasyPublish.Developer>() {
//
//    })
//    developer { it ->
//        it.id = 'leobert'
//        it.name = 'leobert'
//       it. email = 'leobert.l@hotmail.com'
//    }
//
//
//    groupId = "io.github.leobert-lan"
//    artifactId = "davinci"
//    version = "0.0.2-beta"
//    packaging = "aar"
//    siteUrl = "https://github.com/leobert-lan/DaVinCi"
//    gitUrl = "https://github.com/leobert-lan/DaVinCi.git"
//    licenseName = 'MIT'
//    licenseUrl = 'https://github.com/leobert-lan/DaVinCi/blob/master/LICENSE'
//
//    mavenRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
//}
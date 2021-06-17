plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
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


//todo change to use https://github.com/vanniktech/gradle-maven-publish-plugin
////osp.leobert.maven.publish.bean.EasyPublish {
//configure<EasyPublish> {
//    sourceSet = android.sourceSets.named("main").get().java.srcDirs
//    docClassPathAppend = project.files(android.bootClasspath.joinToString(
//        separator = File.pathSeparator
//    ) {
//        it.name
//    }).asPath
//
//    docExcludes = arrayListOf("osp/leobert/android/davinci/*")
//
//    Tmp().apply {
//        value = "build/outputs/aar/davinci-release.aar"
//    }.let {
//        if (!artifactsAppend.contains(it))
//            artifactsAppend.add(it)
//    }
//
//    description =
//        "An Android library to help create background drawable and ColorStateList without xml"
//    Developer().apply {
//        this.id = "leobert"
//        this.name = "leobert"
//        this.email = "leobert.l@hotmail.com"
//    }.let { developer ->
//        if (!mDevelopers.contains(developer)) mDevelopers.add(developer)
//    }
//
//
//    groupId = "io.github.leobert-lan"
//    artifactId = "davinci"
//    version = "0.0.3-beta"
//    packaging = "aar"
//    siteUrl = "https://github.com/leobert-lan/DaVinCi"
//    gitUrl = "https://github.com/leobert-lan/DaVinCi.git"
//    licenseName = "MIT"
//    licenseUrl = "https://github.com/leobert-lan/DaVinCi/blob/master/LICENSE"
//
//    mavenRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
//}
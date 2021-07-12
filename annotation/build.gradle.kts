plugins {
    id("java-library")
    id("kotlin")
    id("osp.leobert.maven.publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Dependencies.Kotlin.version}")
}

EasyPublish {
    sourceSet = sourceSets.main.get().java.srcDirs
//    notStandardJavaComponent = false

    docExcludes = arrayListOf("osp/leobert/android/davinci/*")

    artifact {
        value = "build/libs/annotation.jar"
    }

    description =
        "An Android library to help create background drawable and ColorStateList without xml"

    developer {

        this.id = "leobert"
        this.name = "leobert"
        this.email = "leobert.l@hotmail.com"
    }

    groupId = "io.github.leobert-lan"
    artifactId = "davinci-anno"
    version = "0.0.2"
    packaging = "jar"
    siteUrl = "https://github.com/leobert-lan/DaVinCi"
    gitUrl = "https://github.com/leobert-lan/DaVinCi.git"
    licenseName = "MIT"
    licenseUrl = "https://github.com/leobert-lan/DaVinCi/blob/master/LICENSE"

    mavenRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
}
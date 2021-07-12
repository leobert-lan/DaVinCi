plugins {
  id("com.google.devtools.ksp")
  kotlin("jvm")
  id("osp.leobert.maven.publish")
}

dependencies {
  compileOnly(Dependencies.Kotlin.Ksp.api)

  implementation(Dependencies.AutoService.annotations)
  ksp("dev.zacsweers.autoservice:auto-service-ksp:0.5.2")
  implementation(Dependencies.KotlinPoet.kotlinPoet)
  implementation(Dependencies.guava)

  testImplementation(Dependencies.Kotlin.Ksp.api)
  testImplementation(Dependencies.Testing.truth)
  testImplementation(Dependencies.Testing.junit)

  testImplementation(Dependencies.Kotlin.Ksp.ksp)
  testImplementation(Dependencies.Testing.compileTesting)
  testImplementation(Dependencies.Kotlin.compilerEmbeddable)

  implementation(Dependencies.DaVinCi.annotation)

//  todo use stable version when release
//  implementation(project(":annotation"))

}


EasyPublish {
  sourceSet = sourceSets.main.get().java.srcDirs

  docExcludes = arrayListOf("osp/leobert/android/davinci/*")

  artifact {
    value = "build/libs/anno_ksp.jar"
  }

  description =
    "An Android library to help create background drawable and ColorStateList without xml"

  developer {

    this.id = "leobert"
    this.name = "leobert"
    this.email = "leobert.l@hotmail.com"
  }

  groupId = "io.github.leobert-lan"
  artifactId = "davinci-anno-ksp"
  version = "0.0.2"
  packaging = "jar"
  siteUrl = "https://github.com/leobert-lan/DaVinCi"
  gitUrl = "https://github.com/leobert-lan/DaVinCi.git"
  licenseName = "MIT"
  licenseUrl = "https://github.com/leobert-lan/DaVinCi/blob/master/LICENSE"

  mavenRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
}

plugins {
  id("com.google.devtools.ksp")
  kotlin("jvm")
//  id("osp.leobert.maven.publish")
}

dependencies {
  compileOnly(libs.symbol.processing.api)

  implementation(libs.auto.service.annotations)
  ksp(libs.auto.service.ksp)
  implementation(libs.com.squareup.kotlinpoet)
  implementation(libs.google.guava)

  testImplementation(libs.symbol.processing.api)
  testImplementation(libs.truth)
  testImplementation(libs.junit)

  testImplementation(libs.symbol.processing)
  testImplementation(libs.kotlin.compile.testing)
  testImplementation(libs.kotlin.compiler.embeddable)

  implementation(libs.davinci.anno)

//  todo use stable version when release
//  implementation(project(":annotation"))

}
//
//
//EasyPublish {
//  sourceSet = sourceSets.main.get().java.srcDirs
//
//  docExcludes = arrayListOf("osp/leobert/android/davinci/*")
//
//  artifact {
//    value = "build/libs/anno_ksp.jar"
//  }
//
//  description =
//    "An Android library to help create background drawable and ColorStateList without xml"
//
//  developer {
//
//    this.id = "leobert"
//    this.name = "leobert"
//    this.email = "leobert.l@hotmail.com"
//  }
//
//  groupId = "io.github.leobert-lan"
//  artifactId = "davinci-anno-ksp"
//  version = "0.0.2"
//  packaging = "jar"
//  siteUrl = "https://github.com/leobert-lan/DaVinCi"
//  gitUrl = "https://github.com/leobert-lan/DaVinCi.git"
//  licenseName = "MIT"
//  licenseUrl = "https://github.com/leobert-lan/DaVinCi/blob/master/LICENSE"
//
//  mavenRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
//}

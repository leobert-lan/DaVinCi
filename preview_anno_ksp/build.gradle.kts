plugins {
  id("com.google.devtools.ksp")
  kotlin("jvm")
//  id("osp.leobert.maven.publish")
}

dependencies {
  compileOnly("com.google.devtools.ksp:symbol-processing-api:1.6.10-1.0.2")

  implementation("com.google.auto.service:auto-service-annotations:1.0")
  ksp("dev.zacsweers.autoservice:auto-service-ksp:0.5.2")
  implementation("com.squareup:kotlinpoet:1.8.0")
  implementation("com.google.guava:guava:30.1.1-jre")

  testImplementation("com.google.devtools.ksp:symbol-processing-api:1.6.10-1.0.2")
  testImplementation("com.google.truth:truth:1.1.2")
  testImplementation("junit:junit:4.13.2")

  // TODO re-enable with new release
//  testImplementation(Dependencies.Testing.kspCompileTesting)
  testImplementation("com.google.devtools.ksp:symbol-processing:1.6.10-1.0.2")
  testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.4.0")
  testImplementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.22")

//  todo use stable version when release
  implementation(project(":annotation"))

}


//EasyPublish {
//  sourceSet = sourceSets.main.get().java.srcDirs
//  notStandardJavaComponent = false
//
//  docExcludes = arrayListOf("osp/leobert/android/davinci/*")
//
//  artifact {
//    value = "build/libs/preview_anno_ksp.jar"
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
//  artifactId = "davinci-anno-ksp-preview"
//  version = "0.0.1"
//  packaging = "jar"
//  siteUrl = "https://github.com/leobert-lan/DaVinCi"
//  gitUrl = "https://github.com/leobert-lan/DaVinCi.git"
//  licenseName = "MIT"
//  licenseUrl = "https://github.com/leobert-lan/DaVinCi/blob/master/LICENSE"
//
//  mavenRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
//}

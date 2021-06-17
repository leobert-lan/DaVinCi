plugins {
  id("com.google.devtools.ksp")
  kotlin("jvm")
//  kotlin("kapt")
}

dependencies {
  compileOnly(Dependencies.Kotlin.Ksp.api)

//  kapt(Dependencies.AutoService.compiler) 总是出幺蛾子，手动建立算了
  implementation(Dependencies.AutoService.annotations)
  ksp("dev.zacsweers.autoservice:auto-service-ksp:0.5.2")
  implementation(Dependencies.KotlinPoet.kotlinPoet)
  implementation(Dependencies.guava)

  testImplementation(Dependencies.Kotlin.Ksp.api)
  testImplementation(Dependencies.Testing.truth)
  testImplementation(Dependencies.Testing.junit)

  // TODO re-enable with new release
//  testImplementation(Dependencies.Testing.kspCompileTesting)
  testImplementation(Dependencies.Kotlin.Ksp.ksp)
  testImplementation(Dependencies.Testing.compileTesting)
  testImplementation(Dependencies.Kotlin.compilerEmbeddable)

//  todo use stable version when release
  implementation(project(":annotation"))

}

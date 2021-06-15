plugins {
//  id("com.google.devtools.ksp")
  kotlin("jvm")
  kotlin("kapt")
}

dependencies {
  // Can't entirely rely on this until KSP is stable
//  ksp(Dependencies.AutoService.ksp)
  compileOnly(Dependencies.Kotlin.Ksp.api)

  kapt(Dependencies.AutoService.compiler)
  implementation(Dependencies.AutoService.annotations)
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
}

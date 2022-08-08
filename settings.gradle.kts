pluginManagement {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
  }
}

include(":app")
include(":davinci")
include(":annotation")
include(":anno_ksp")

include(":annotation-java")
include(":davinci_styles_viewer")
//include(":preview_anno_ksp")
include(":ide-preview")

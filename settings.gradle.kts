pluginManagement {
  repositories {
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()

    mavenLocal()

    maven { setUrl("https://jitpack.io") }
    maven { setUrl("https://plugins.gradle.org/m2/") }
  }
}


include(":app")
include(":davinci")
include(":annotation")
include(":anno_ksp")

include(":davinci_styles_viewer")
//include(":preview_anno_ksp")
//include(":ide-preview")


//include(":annotation-java") //仅测试ksp是否对Java注解生效，已验证无需再使用

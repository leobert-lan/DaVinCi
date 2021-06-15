import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = Dependencies.Kotlin.version))
        classpath("com.android.tools.build:gradle:4.0.1")
        classpath("io.github.leobert-lan:easy-publish:1.1.1")
    }
}

plugins {
    id("com.google.devtools.ksp") version Dependencies.Kotlin.Ksp.version apply false
    kotlin("jvm") version Dependencies.Kotlin.version apply false
    id("org.jetbrains.dokka") version Dependencies.Kotlin.dokkaVersion  apply false
    id("com.vanniktech.maven.publish") version "0.15.1" apply false
}

subprojects {
    repositories {
        mavenCentral()
        google()
        // Required for Dokka
        exclusiveContent {
            forRepository {
                maven {
                    name = "JCenter"
                    setUrl("https://jcenter.bintray.com/")
                }
            }
            filter {
                includeModule("org.jetbrains.kotlinx", "kotlinx-html-jvm")
                includeGroup("org.jetbrains.dokka")
                includeModule("org.jetbrains", "markdown")
            }
        }
    }

    pluginManager.withPlugin("java") {
        configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }

    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
        configure<KotlinProjectExtension> {
            explicitApi()
        }

        tasks.withType<KotlinCompile>().configureEach {
            kotlinOptions {
                jvmTarget = Dependencies.Kotlin.jvmTarget
                @Suppress("SuspiciousCollectionReassignment")
                freeCompilerArgs += Dependencies.Kotlin.defaultFreeCompilerArgs
            }
        }

        apply(plugin = "org.jetbrains.dokka")
        tasks.named<DokkaTask>("dokkaHtml") {
            outputDirectory.set(rootProject.rootDir.resolve("docs/0.x"))
            dokkaSourceSets.configureEach {
                skipDeprecated.set(true)
                // TODO Dokka can't parse javadoc.io yet
                //    externalDocumentationLink {
                //      url.set(URL("https://javadoc.io/doc/com.google.auto.service/auto-service-annotations/latest/index.html"))
                //    }
            }
        }
    }
}
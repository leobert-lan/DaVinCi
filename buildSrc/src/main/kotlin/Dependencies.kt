import org.gradle.kotlin.dsl.DependencyHandlerScope

object Dependencies {

    const val guava = "com.google.guava:guava:30.1.1-jre"

    object AutoService {
        private const val version = "1.0"
        const val annotations = "com.google.auto.service:auto-service-annotations:$version"
        const val compiler = "com.google.auto.service:auto-service:$version"
//    const val ksp = "dev.zacsweers.autoservice:auto-service-ksp:0.4.2"
    }

    object Kotlin {
        const val version = "1.6.0"
        const val dokkaVersion = "1.4.32"
        const val jvmTarget = "1.8"
        val defaultFreeCompilerArgs = listOf("-Xjsr305=strict", "-progressive")
        const val compilerEmbeddable = "org.jetbrains.kotlin:kotlin-compiler-embeddable:$version"

        object Ksp {
            const val version = "1.6.10-1.0.2"
            const val api = "com.google.devtools.ksp:symbol-processing-api:$version"
            const val ksp = "com.google.devtools.ksp:symbol-processing:$version"
        }
    }

    object DaVinCi {
        const val annotation = "io.github.leobert-lan:davinci-anno:0.0.2"
        const val ksp = "io.github.leobert-lan:davinci-anno-ksp:0.0.2"
        const val api = "io.github.leobert-lan:davinci:0.0.5"
        const val viewer = "io.github.leobert-lan:davinci-style-viewer:0.0.1"
    }

    object KotlinPoet {
        private const val version = "1.8.0"
        const val kotlinPoet = "com.squareup:kotlinpoet:$version"
    }

    object Testing {
        const val compileTesting = "com.github.tschuchortdev:kotlin-compile-testing:1.4.0"
        const val kspCompileTesting = "com.github.tschuchortdev:kotlin-compile-testing-ksp:1.4.0"
        const val junit = "junit:junit:4.13.2"
        const val truth = "com.google.truth:truth:1.1.2"
    }

    object Asm {
        val asm6Bundle = arrayListOf(
            "org.ow2.asm:asm:6.0",
            "org.ow2.asm:asm-commons:6.0",
            "org.ow2.asm:asm-analysis:6.0",
            "org.ow2.asm:asm-util:6.0",
            "org.ow2.asm:asm-tree:6.0"
        )

        fun implementAsm6(scope: DependencyHandlerScope) {
            asm6Bundle.forEach { deps ->
                scope.dependencies.add("implementation", deps)
            }
        }
    }
}

package osp.leobert.android.davinci.ksp

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.*
import osp.leobert.android.davinci.annotation.DaVinCiStyle
import osp.leobert.android.davinci.annotation.DaVinCiStyleFactory
import osp.leobert.android.davinci.meta.StyleMetaInfo
import kotlin.concurrent.thread

internal class DaVinCiSymbolProcessor(
    environment: SymbolProcessorEnvironment,
) : SymbolProcessor {

    private val codeGenerator = environment.codeGenerator
    private val logger = environment.logger


    private val styleProviders: MutableMap<String, StyleMetaInfo> = hashMapOf()

    private val styleFactoryProviders: MutableMap<String, StyleMetaInfo> = hashMapOf()

    private val verbose = environment.options["daVinCi.verbose"]?.toBoolean() == true

    private val packageName = environment.options["daVinCi.pkg"]

    private val moduleName = environment.options["daVinCi.module"]

    companion object {
        val DAVINCI_STYLE_NAME = requireNotNull(DaVinCiStyle::class.qualifiedName)
        val DAVINCI_STYLE_FACTORY_NAME = requireNotNull(DaVinCiStyleFactory::class.qualifiedName)
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        log("hello, this is DaVinCiSymbolProcessor")


        val styleNotated = resolver.getClassDeclarationByName(
            resolver.getKSNameFromString(DAVINCI_STYLE_NAME)
        )?.asType(emptyList())

        val factoryNotated = resolver.getClassDeclarationByName(
            resolver.getKSNameFromString(DAVINCI_STYLE_FACTORY_NAME)
        )?.asType(emptyList())


        if (styleNotated == null && factoryNotated == null) {
            log("@DaVinCiStyle or @DaVinCiStyleFactory not found")
            return emptyList()
        }

        styleProviders.clear()
        styleFactoryProviders.clear()

        styleNotated?.let {
            handleDaVinCiStyle(resolver = resolver, notationType = it)
        }

        factoryNotated?.let {
            handleDaVinCiStyleFactory(resolver = resolver, notationType = it)
        }

        generateAndClearConfigFiles()


        return emptyList()
    }

    private fun log(message: String) {
        if (verbose) {
            logger.warn("[DaVinCi-ksp]:$message")
        }
    }

    private fun logE(message: String, node: KSNode?) {
        logger.error("[DaVinCi-ksp]:$message", node)
    }

    private fun handleDaVinCiStyle(resolver: Resolver, notationType: KSType) {
        resolver.getSymbolsWithAnnotation(DAVINCI_STYLE_NAME)
            .asSequence()
            .filterIsInstance<KSClassDeclaration>()
            .forEach { style ->

                val annotation =
                    style.annotations.find { it.annotationType.resolve() == notationType }
                        ?: run {
                            logE("@DaVinCiStyle annotation not found", style)
                            return@forEach
                        }

                //structure: DaVinCiStyle(val styleName: String, val parent: String = "")

                val styleName = annotation.arguments.find {
                    it.name?.getShortName() == "styleName"
                }?.value?.toString() ?: kotlin.run {
                    logE("missing styleName? version not matched?", style)
                    return@forEach
                }


                val constName = generateConstOfStyleName(styleName)
                if (styleProviders.containsKey(constName)) {
                    logE(
                        "duplicated style name:${styleName}, original register:${styleProviders[constName]?.clzNode}",
                        style
                    )
                    return@forEach
                }

                styleProviders[constName] = StyleMetaInfo.Style(
                    constName = constName,
                    styleName = styleName,
                    clzNode = style
                )

                /*end @forEach*/
            }
    }

    private fun handleDaVinCiStyleFactory(resolver: Resolver, notationType: KSType) {
        resolver.getSymbolsWithAnnotation(DAVINCI_STYLE_FACTORY_NAME)
            .asSequence()
            .filterIsInstance<KSClassDeclaration>()
            .forEach { style ->

                val annotation =
                    style.annotations.find { it.annotationType.resolve() == notationType }
                        ?: run {
                            logE("@DaVinCiStyleFactory annotation not found", style)
                            return@forEach
                        }

                //structure: DaVinCiStyle(val styleName: String, val parent: String = "")

                val styleName = annotation.arguments.find {
                    it.name?.getShortName() == "styleName"
                }?.value?.toString() ?: kotlin.run {
                    logE("missing styleName? version not matched?", style)
                    return@forEach
                }


                val constName = generateConstOfStyleName(styleName)
                if (styleFactoryProviders.containsKey(constName)) {
                    logE(
                        "duplicated style name:${styleName}, original register:${styleFactoryProviders[constName]?.clzNode}",
                        style
                    )
                    return@forEach
                }

                styleFactoryProviders[constName] = StyleMetaInfo.Factory(
                    constName = constName,
                    styleName = styleName,
                    clzNode = style
                )

                /*end @forEach*/
            }
    }

    private fun generateConstOfStyleName(styleName: String): String =
        styleName.split(".").joinToString("_").apply {
            log("create style const: $this from $styleName")
        }


    private fun generateAndClearConfigFiles() {
        log("generate register styles logic")

        val properties = HashSet<StyleMetaInfo>().apply {
            addAll(styleProviders.values)
            addAll(styleFactoryProviders.values)
        }.map {
            it.propertySpec()
        }.toSet()

        val daVinCiStylesSpec = TypeSpec.objectBuilder("${moduleName ?: ""}DaVinCiStyles")
            .addKdoc(
                CodeBlock.of("auto-generated by DaVinCi, do not modify")
            ).apply {
                this.addProperties(properties)
                this.addFunction(
                    FunSpec.builder("register")
                        .addKdoc("register all styles and styleFactories")
                        .addModifiers(arrayListOf(KModifier.PUBLIC))
                        .addCode(
                            """
                               registerStyles()
                               registerStyleFactories()
                            """.trimIndent()
                        )
                        .build()
                )
                this.addFunction(
                    FunSpec.builder("registerStyles")
                        .addModifiers(arrayListOf(KModifier.PRIVATE))
                        .apply {
                            styleProviders.forEach {
                                this.addCode(it.value.registerBlock())
                            }
                        }

                        .build()
                )

                this.addFunction(
                    FunSpec.builder("registerStyleFactories")
                        .addModifiers(arrayListOf(KModifier.PRIVATE))
                        .apply {
                            styleFactoryProviders.forEach {
                                this.addCode(it.value.registerBlock())
                            }
                        }
                        .build()
                )
            }
            .build()
        val fileSpec = FileSpec.get(packageName ?: "", daVinCiStylesSpec)

        val dependencies = Dependencies(true)
        thread(true) {
            synchronized(codeGenerator) {
                log(fileSpec.toString())

                codeGenerator.createNewFile(
                    dependencies = dependencies,
                    packageName = packageName ?: "",
                    fileName = "${moduleName ?: ""}DaVinCiStyles",
                    extensionName = "kt"
                ).bufferedWriter().use { writer ->
                    try {
                        fileSpec.writeTo(writer)
                    } catch (e: Exception) {
                        logE(e.message ?: "", null)
                    } finally {
//                        writer.flush()
//                        writer.close()
                    }
                }
            }
        }

    }

}
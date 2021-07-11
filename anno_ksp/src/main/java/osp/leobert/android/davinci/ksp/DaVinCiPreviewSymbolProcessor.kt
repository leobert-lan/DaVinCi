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
import osp.leobert.android.davinci.annotation.StyleViewer
import kotlin.concurrent.thread

/**
 * <p><b>Package:</b> osp.leobert.android.davinci.ksp </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> DaVinCiPreviewSymbolProcessor </p>
 * Created by leobert on 2021/7/9.
 */
public class DaVinCiPreviewSymbolProcessor(
    environment: SymbolProcessorEnvironment,
) : SymbolProcessor {

    private val codeGenerator = environment.codeGenerator
    private val logger = environment.logger

    private val verbose = environment.options["daVinCi.verbose"]?.toBoolean() == true

    private val packageName = environment.options["daVinCi.pkg"]

    private val moduleName = environment.options["daVinCi.module"]

    private val preview = environment.options["daVinCi.preview"]?.toBoolean() == true

    public companion object {
        public val DAVINCI_STYLE_NAME: String = requireNotNull(DaVinCiStyle::class.qualifiedName)
        public val DAVINCI_STYLE_FACTORY_NAME: String =
            requireNotNull(DaVinCiStyleFactory::class.qualifiedName)

        public val PREVIEW_NAME: String = requireNotNull(StyleViewer::class.qualifiedName)
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        log("hello, this is DaVinCiPreviewSymbolProcessor")
//        if (!preview)
//            return emptyList()


        val styleNotated = resolver.getClassDeclarationByName(
            resolver.getKSNameFromString(DAVINCI_STYLE_NAME)
        )?.asType(emptyList())

        val factoryNotated = resolver.getClassDeclarationByName(
            resolver.getKSNameFromString(DAVINCI_STYLE_FACTORY_NAME)
        )?.asType(emptyList())

        val previewNotated = resolver.getClassDeclarationByName(
            resolver.getKSNameFromString(PREVIEW_NAME)
        )?.asType(emptyList())


        if (previewNotated == null) {
            log("@StyleViewer not found")
            return emptyList()
        }

        val configsProviders: MutableMap<String, StyleMetaInfo> = hashMapOf()

        handleDaVinCiStyleViewer(
            configsProviders = configsProviders,
            resolver = resolver,
            notationType = previewNotated,
            styleNotated = styleNotated,
            factoryNotated = factoryNotated)


        generateAndClearConfigFiles(configsProviders = configsProviders)


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

    private fun handleDaVinCiStyleViewer(
        configsProviders: MutableMap<String, StyleMetaInfo>,
        resolver: Resolver,
        notationType: KSType,
        styleNotated: KSType?,
        factoryNotated: KSType?,
    ) {
        resolver.getSymbolsWithAnnotation(PREVIEW_NAME)
            .asSequence()
            .filterIsInstance<KSClassDeclaration>()
            .forEach { style ->

                val annotation =
                    style.annotations.find { it.annotationType.resolve() == notationType }
                        ?: run {
                            logE("@StyleViewer annotation not found", style)
                            return@forEach
                        }

                val styleOrFactoryNotation = style.annotations.find {
                    it.annotationType.resolve() == styleNotated ||
                            it.annotationType.resolve() == factoryNotated
                } ?: run {
                    logE("@StyleViewer annotation must used with @DaVinCiStyle or @DaVinCiStyleFactory ",
                        style)
                    return@forEach
                }

                // val height: Int = 40,
                //    val width: Int = 100,
                //    val background: String = "#ffffff",
                //    val type: Int = FLAG_BG or FLAG_CSL,

                val styleName = styleOrFactoryNotation.arguments.find {
                    it.name?.getShortName() == "styleName"
                }?.value?.toString() ?: kotlin.run {
                    logE("missing styleName? version not matched?", style)
                    return@forEach
                }

                val height = annotation.arguments.find {
                    it.name?.getShortName() == "height"
                }?.value?.toString()?.toIntOrNull() ?: kotlin.run {
                    logE("missing height? version not matched?", style)
                    return@forEach
                }

                val width = annotation.arguments.find {
                    it.name?.getShortName() == "width"
                }?.value?.toString()?.toIntOrNull() ?: kotlin.run {
                    logE("missing width? version not matched?", style)
                    return@forEach
                }

                val type = annotation.arguments.find {
                    it.name?.getShortName() == "type"
                }?.value?.toString()?.toIntOrNull() ?: kotlin.run {
                    logE("missing type? version not matched?", style)
                    return@forEach
                }

                val background = annotation.arguments.find {
                    it.name?.getShortName() == "background"
                }?.value?.toString() ?: kotlin.run {
                    logE("missing background? version not matched?", style)
                    return@forEach
                }

                configsProviders[styleName] = StyleMetaInfo(
                    styleName = styleName,
                    clzNode = style,
                    width = width, height = height, background = background, type = type
                )

                /*end @forEach*/
            }
    }

    private fun generateAndClearConfigFiles(configsProviders: MutableMap<String, StyleMetaInfo>) {

        val daVinCiStylesSpec =
            TypeSpec.objectBuilder("${moduleName ?: ""}DaVinCiStylePreviewInjector")
                .addKdoc(
                    CodeBlock.of("auto-generated by DaVinCi, do not modify")
                ).apply {
                    this.addFunction(
                        FunSpec.builder("register")
                            .addKdoc("register preview configurations")
                            .addModifiers(arrayListOf(KModifier.PUBLIC))
                            .addCode(
                                """
                               registerConfigs()
                            """.trimIndent()
                            )
                            .build()
                    )
                    this.addFunction(
                        FunSpec.builder("registerConfigs")
                            .addModifiers(arrayListOf(KModifier.PRIVATE))
                            .apply {
                                if (preview) {
                                    configsProviders.forEach {
                                        this.addCode(it.value.registerBlock())
                                    }
                                } else {
                                    this.addComment("not preview mode, ignore")
                                }
                            }

                            .build()
                    )


                }
                .build()

        val dependencies = Dependencies(true)
        val fileSpec = FileSpec.get(packageName ?: "", daVinCiStylesSpec)

        thread(true) {
            synchronized(codeGenerator) {
                log(fileSpec.toString())


                codeGenerator.createNewFile(
                    dependencies = dependencies,
                    packageName = packageName ?: "",
                    fileName = "${moduleName ?: ""}DaVinCiStylePreviewInjector",
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


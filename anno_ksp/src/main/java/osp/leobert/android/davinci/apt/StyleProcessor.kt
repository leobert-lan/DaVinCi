package osp.leobert.android.davinci.apt

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import osp.leobert.android.davinci.annotation.DaVinCiStyle
import osp.leobert.android.davinci.annotation.DaVinCiStyleFactory
import osp.leobert.android.davinci.meta.NodeWrapper
import osp.leobert.android.davinci.meta.StyleMetaInfo
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Types
import javax.tools.Diagnostic

/**
 * <p><b>Package:</b> osp.leobert.android.davinci.apt </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> StyleProcessor </p>
 * Created by leobert on 7/9/21.
 */
@AutoService(Processor::class)
@SupportedAnnotationTypes(value = [
    "osp.leobert.android.davinci.annotation.DaVinCiStyle",
    "osp.leobert.android.davinci.annotation.DaVinCiStyleFactory"
])
public class StyleProcessor : AbstractProcessor() {


    public lateinit var processingEnv: ProcessingEnvironment

    private lateinit var filer: Filer

    private lateinit var messager: Messager

    private lateinit var typeUtils: Types

    private val styleProviders: MutableMap<String, StyleMetaInfo> = hashMapOf()

    private val styleFactoryProviders: MutableMap<String, StyleMetaInfo> = hashMapOf()


    private val verbose by lazy {
        processingEnv.options["daVinCi.verbose"]?.toBoolean() == true
    }

    private val packageName by lazy {
        processingEnv.options["daVinCi.pkg"]
    }

    private val moduleName by lazy {
        processingEnv.options["daVinCi.module"]
    }


    private fun log(message: String) {
        if (verbose) {
            messager.printMessage(Diagnostic.Kind.NOTE, "[DaVinCi-apt]:$message\r\n")
        }
    }

    private fun logE(message: String, node: Element?) {
        messager.printMessage(Diagnostic.Kind.ERROR, "[DaVinCi-apt]:$message\r\n", node)
    }

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        this.processingEnv = requireNotNull(processingEnv)

        this.filer = this.processingEnv.filer
        this.messager = this.processingEnv.messager
        this.typeUtils = this.processingEnv.typeUtils

        this.processingEnv.options
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?,
    ): Boolean {
        processDavinci(annotations, roundEnv)
        return false
    }

    private fun processDavinci(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?,
    ) {

        annotations ?: return
        roundEnv ?: return


        val styles = roundEnv.getElementsAnnotatedWith(DaVinCiStyle::class.java)
            ?.filterIsInstance<TypeElement>()

        val styleFactories = roundEnv.getElementsAnnotatedWith(DaVinCiStyleFactory::class.java)
            ?.filterIsInstance<TypeElement>()

        if (styles.isNullOrEmpty() && styleFactories.isNullOrEmpty()) {
            log("@DaVinCiStyle or @DaVinCiStyleFactory not found")
            return
        }

        styleProviders.clear()
        styleFactoryProviders.clear()

        styles?.let {
            handleDaVinCiStyle(elements = it)
        }

        styleFactories?.let {
            handleDaVinCiStyleFactory(elements = it)
        }

        generateAndClearConfigFiles()
    }


    private fun handleDaVinCiStyleFactory(elements: List<TypeElement>) {
        elements.forEach { style ->
            val annotation = style.getAnnotation(DaVinCiStyleFactory::class.java)

            val styleName = annotation.styleName


            val constName = generateConstOfStyleName(styleName)
            if (styleFactoryProviders.containsKey(constName)) {
                logE(
                    "duplicated style name:${styleName}, original register:${styleFactoryProviders[constName]?.clzNode}",
                    style
                )
                return@forEach
            }

            style.asType().toString()

            styleFactoryProviders[constName] = StyleMetaInfo.Factory(
                constName = constName,
                styleName = styleName,
                clzNode = NodeWrapper.JavaNodeWrapper(style)
            )
        }

    }

    private fun handleDaVinCiStyle(elements: List<TypeElement>) {
        elements.forEach { style ->
            val annotation = style.getAnnotation(DaVinCiStyle::class.java)

            val styleName = annotation.styleName


            val constName = generateConstOfStyleName(styleName)
            if (styleProviders.containsKey(constName)) {
                logE(
                    "duplicated style name:${styleName}, original register:${styleProviders[constName]?.clzNode}",
                    style
                )
                return@forEach
            }

            style.asType().toString()

            styleProviders[constName] = StyleMetaInfo.Style(
                constName = constName,
                styleName = styleName,
                clzNode = NodeWrapper.JavaNodeWrapper(style)
            )
        }
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

        fileSpec.writeTo(filer)
    }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    private fun generateConstOfStyleName(styleName: String): String =
        styleName.split(".").joinToString("_").apply {
            log("create style const: $this from $styleName")
        }

}
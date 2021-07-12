package osp.leobert.android.davinci.apt

import com.google.auto.service.AutoService
import osp.leobert.android.davinci.annotation.DaVinCiStyle
import osp.leobert.android.davinci.annotation.DaVinCiStyleFactory
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Types

/**
 * <p><b>Package:</b> osp.leobert.android.davinci.apt </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> StyleProcessor </p>
 * <p><b>Description:</b> TODO </p>
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

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        this.processingEnv = requireNotNull(processingEnv)

        this.filer = this.processingEnv.filer
        this.messager = this.processingEnv.messager
        this.typeUtils = this.processingEnv.typeUtils
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?,
    ): Boolean {

        return false
    }

    private fun processDavinci(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?,
    ) {

        annotations ?: return
        roundEnv ?: return


        val styles = roundEnv.getElementsAnnotatedWith(DaVinCiStyle::class.java)

        val styleFactories = roundEnv.getElementsAnnotatedWith(DaVinCiStyleFactory::class.java)
    }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()
}
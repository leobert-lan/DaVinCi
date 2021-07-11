package osp.leobert.android.davinci.apt

import com.google.auto.service.AutoService
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

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
public class StyleProcessor: AbstractProcessor() {


    public lateinit var processingEnv: ProcessingEnvironment

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        this.processingEnv = requireNotNull(processingEnv)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {

        return false
    }

    override fun getSupportedSourceVersion(): SourceVersion  = SourceVersion.latestSupported()
}
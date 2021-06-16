package osp.leobert.android.davinci.ksp

import com.google.auto.service.AutoService
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFile
import osp.leobert.android.davinci.annotation.DaVinCiStyle
import osp.leobert.android.davinci.annotation.DaVinCiStyleFactory

@AutoService(SymbolProcessorProvider::class)
public class DaVinCiSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        DaVinCiSymbolProcessor(environment)
}

private class DaVinCiSymbolProcessor(
    environment: SymbolProcessorEnvironment,
) : SymbolProcessor {

    private val codeGenerator = environment.codeGenerator
    private val logger = environment.logger

    /**
     * Maps the class names of service provider interfaces to the
     * class names of the concrete classes which implement them plus their KSFile (for incremental
     * processing).
     *
     * For example,
     * ```
     * "com.google.apphosting.LocalRpcService" -> "com.google.apphosting.datastore.LocalDatastoreService"
     * ```
     */
    private val providers: Multimap<String, Pair<String, KSFile>> = HashMultimap.create()

    //    private val verify = environment.options["autoserviceKsp.verify"]?.toBoolean() == true
    private val verbose = environment.options["daVinCi.verbose"]?.toBoolean() == true

    companion object {
        val DAVINCI_STYLE_NAME = requireNotNull(DaVinCiStyle::class.qualifiedName)
        val DAVINCI_STYLE_FACTORY_NAME = requireNotNull(DaVinCiStyleFactory::class.qualifiedName)
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        log("hello, this is DaVinCiSymbolProcessor")
        return emptyList()
    }

    private fun log(message: String) {
        if (verbose) {
            logger.warn("[DaVinCi-ksp]:$message")
        }
    }

}
package osp.leobert.android.davinci.ksp

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/**
 * <p><b>Package:</b> osp.leobert.android.davinci.ksp </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> DaVinCiPreviewSymbolProcessorProvider </p>
 * Created by leobert on 2021/7/9.
 */
@AutoService(SymbolProcessorProvider::class)
public class DaVinCiPreviewSymbolProcessorProvider: SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        DaVinCiPreviewSymbolProcessor(environment)
}

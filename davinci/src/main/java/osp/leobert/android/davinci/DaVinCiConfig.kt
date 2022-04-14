package osp.leobert.android.davinci

import android.content.Context
import android.util.Log
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import osp.leobert.android.davinci.lookup.IApplierTagLookup
import osp.leobert.android.davinci.lookup.IColorLookup
import osp.leobert.android.davinci.lookup.IDimensionLookup
import osp.leobert.android.davinci.lookup.IResourceLookup
import osp.leobert.android.davinci.res.DaVinCiResource

/**
 * Created by leobert on 2022/1/28.
 */
@Suppress("WeakerAccess")
object DaVinCiConfig : IDimensionLookup, IColorLookup, IApplierTagLookup, IResourceLookup {

    val customDimensionLookups = arrayListOf<IDimensionLookup.Custom>()

    val customColorLookups = arrayListOf<IColorLookup.Custom>()

    val customApplierTagLookup = arrayListOf<IApplierTagLookup.Custom>()

    private val resourceLookupStrategy = strategyOf<Class<*>, IResourceLookup>()
        .register(DaVinCiResource.ColorIntRes::class.java, IResourceLookup.DefaultColorIntResourceLookup())


    @Dimension(unit = Dimension.PX)
    override fun lookupDimension(str: String, context: Context, daVinCi: DaVinCi?): Int? {
        return (customDimensionLookups.find {
            it.canLookup(str, context)
        } ?: IDimensionLookup.InternalLookup).lookupDimension(str, context, daVinCi)
    }

    @ColorInt
    override fun lookupColor(resNameOrColorStr: String?, context: Context?, daVinCi: DaVinCi?): Int? {
        return (customColorLookups.find {
            it.canLookup(resNameOrColorStr, context)
        } ?: IColorLookup.InternalLookup).lookupColor(resNameOrColorStr, context, daVinCi)
    }

    override fun lookupTag(resName: String?, context: Context?, daVinCi: DaVinCi?): String? {
        return (customApplierTagLookup.find {
            it.canLookup(resName, context)
        } ?: IApplierTagLookup.InternalLookup).lookupTag(resName, context, daVinCi)
    }

    fun <T> registerResourceLookup(clazz: Class<T>, lookup: IResourceLookup) {
        resourceLookupStrategy.register(clazz, lookup)
    }


    override fun lookupResource(clazz: Class<*>, str: String?, context: Context?, daVinCi: DaVinCi?): Any? {
        val lookuper = resourceLookupStrategy.fetch(clazz)

        return if (lookuper == null) {
            if (DaVinCi.enableDebugLog) Log.e(DaVinCiExpression.sLogTag, "none configuration for lookup ${clazz.name}")
            null
        } else {
            lookuper.lookupResource(clazz, str, context, daVinCi)
        }
    }

}
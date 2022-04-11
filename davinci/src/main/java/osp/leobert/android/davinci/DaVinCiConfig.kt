package osp.leobert.android.davinci

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import osp.leobert.android.davinci.lookup.IApplierTagLookup
import osp.leobert.android.davinci.lookup.IColorLookup
import osp.leobert.android.davinci.lookup.IDimensionLookup

/**
 * Created by leobert on 2022/1/28.
 */
object DaVinCiConfig : IDimensionLookup, IColorLookup, IApplierTagLookup {

    val customDimensionLookups = arrayListOf<IDimensionLookup.Custom>()

    val customColorLookups = arrayListOf<IColorLookup.Custom>()

    val customApplierTagLookup = arrayListOf<IApplierTagLookup.Custom>()


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


}
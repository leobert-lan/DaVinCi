package osp.leobert.android.davinci

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import osp.leobert.android.davinci.lookup.IColorLookup
import osp.leobert.android.davinci.lookup.IDimensionLookup

/**
 * Created by leobert on 2022/1/28.
 */
object DaVinCiConfig : IDimensionLookup, IColorLookup {

    val customDimensionParsers = arrayListOf<IDimensionLookup.Custom>()

    val customColorParsers = arrayListOf<IColorLookup.Custom>()


    @Dimension(unit = Dimension.PX)
    override fun lookupDimension(str: String, context: Context): Int? {
        return (customDimensionParsers.find {
            it.canLookup(str, context)
        } ?: IDimensionLookup.InternalLookup).lookupDimension(str, context)
    }

    @ColorInt
    override fun lookupColor(resName: String?, context: Context?): Int? {
        return (customColorParsers.find {
            it.canLookup(resName, context)
        } ?: IColorLookup.InternalLookup).lookupColor(resName, context)
    }


}
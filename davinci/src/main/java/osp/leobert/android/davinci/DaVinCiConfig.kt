package osp.leobert.android.davinci

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import osp.leobert.android.davinci.parser.IColorParser
import osp.leobert.android.davinci.parser.IDimensionParser

/**
 * Created by leobert on 2022/1/28.
 */
object DaVinCiConfig : IDimensionParser, IColorParser {

    val customDimensionParsers = arrayListOf<IDimensionParser.Custom>()

    val customColorParsers = arrayListOf<IColorParser.Custom>()


    @Dimension(unit = Dimension.PX)
    override fun parseDimension(str: String, context: Context): Int? {
        return (customDimensionParsers.find {
            it.canParse(str, context)
        } ?: IDimensionParser.InternalParser).parseDimension(str, context)
    }

    @ColorInt
    override fun parseColor(resName: String?, context: Context?): Int? {
        return (customColorParsers.find {
            it.canParse(resName, context)
        } ?: IColorParser.InternalParser).parseColor(resName, context)
    }


}
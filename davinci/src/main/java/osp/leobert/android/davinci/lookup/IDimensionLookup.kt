package osp.leobert.android.davinci.lookup

import android.content.Context
import android.util.TypedValue
import android.view.ViewGroup
import androidx.annotation.Dimension
import osp.leobert.android.davinci.DaVinCi

/**
 * parse dimension
 *
 * Created by leobert on 2022/1/28.
 */
interface IDimensionLookup {
    @Dimension(unit = Dimension.PX)
    fun lookupDimension(str: String, context: Context,daVinCi: DaVinCi?): Int?

    companion object {
        val InternalLookup: IDimensionLookup = object : IDimensionLookup {
            override fun lookupDimension(str: String, context: Context, daVinCi: DaVinCi?): Int? {
                return when {
                    str == "w" -> {
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                    str == "m" -> {
                        ViewGroup.LayoutParams.MATCH_PARENT
                    }
                    str.endsWith("dp") -> {
                        dp2px(
                            context,
                            (str.substring(0, str.length - 2).toFloatOrNull() ?: 0f)
                        )
                    }
                    str.endsWith("sp") -> {
                        sp2px(
                            context,
                            (str.substring(0, str.length - 2).toFloatOrNull() ?: 0f)
                        )
                    }
                    str.endsWith("mm") -> {
                        mm2px(
                            context,
                            (str.substring(0, str.length - 2).toFloatOrNull() ?: 0f)
                        )
                    }
                    str.endsWith("in") -> {
                        in2px(
                            context,
                            (str.substring(0, str.length - 2).toFloatOrNull() ?: 0f)
                        )
                    }
                    str.endsWith("pt") -> {
                        pt2px(
                            context,
                            (str.substring(0, str.length - 2).toFloatOrNull() ?: 0f)
                        )
                    }
                    else -> str.toIntOrNull()
                }
            }

            fun dp2px(context: Context, value: Float): Int {
                return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics) + 0.5f).toInt()
            }

            fun sp2px(context: Context, value: Float): Int {
                return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.resources.displayMetrics) + 0.5f).toInt()
            }

            fun pt2px(context: Context, value: Float): Int {
                return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, value, context.resources.displayMetrics) + 0.5f).toInt()
            }

            fun in2px(context: Context, value: Float): Int {
                return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN, value, context.resources.displayMetrics) + 0.5f).toInt()
            }

            fun mm2px(context: Context, value: Float): Int {
                return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, value, context.resources.displayMetrics) + 0.5f).toInt()
            }
        }
    }

    interface Custom : IDimensionLookup {
        fun canLookup(str: String, context: Context): Boolean
    }
}
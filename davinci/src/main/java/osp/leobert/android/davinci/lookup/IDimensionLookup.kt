package osp.leobert.android.davinci.lookup

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.Dimension
import osp.leobert.android.reporter.review.TODO

/**
 * parse dimension
 *
 * Created by leobert on 2022/1/28.
 */
interface IDimensionLookup {
    @Dimension(unit = Dimension.PX)
    fun lookupDimension(str: String, context: Context): Int?

    companion object {
        @TODO(desc = "添加PT等单位处理")
        val InternalLookup: IDimensionLookup = object : IDimensionLookup {
            override fun lookupDimension(str: String, context: Context): Int? {
                return when {
                    str.endsWith("dp") -> {
                        val scale: Float = context.resources.displayMetrics.density
                        val dipValue = (str.substring(0, str.length - 2).toIntOrNull() ?: 0)
                        (dipValue * scale + 0.5f).toInt()
                    }
                    str == "w" -> {
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                    str == "m" -> {
                        ViewGroup.LayoutParams.MATCH_PARENT
                    }
                    else -> str.toIntOrNull()
                }
            }
        }
    }

    interface Custom : IDimensionLookup {
        fun canLookup(str: String, context: Context): Boolean
    }
}
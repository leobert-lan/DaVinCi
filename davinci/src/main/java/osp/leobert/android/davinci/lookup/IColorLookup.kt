package osp.leobert.android.davinci.lookup

import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.DaVinCiExpression

/**
 * Created by leobert on 2022/1/28.
 */
interface IColorLookup {
    @ColorInt
    fun lookupColor(resName: String?, context: Context?): Int?

    companion object {
        /**
         * 内部实现中处理的情况：称之为颜色资源TOKEN，需要有识别特征
         * * #ffffffff等直接定义的色
         * * color-res name
         *
         * 使用者可以自行扩展诸如网络资源的获取和解析。
         *
         * 前置流程为解析资源定义类型和资源TOKEN
         * */
        val InternalLookup: IColorLookup = object : IColorLookup {
            override fun lookupColor(resName: String?, context: Context?): Int? {
                try {
                    if (resName.isNullOrEmpty()) return null

                    if (resName.startsWith("#")) {
                        return Color.parseColor(resName)
                    }
                    if (context == null) return null
                    val resources = context.resources
                    val id = resources.getIdentifier(resName, "color", context.packageName)
                    return if (id == 0) {
                        if (DaVinCi.enableDebugLog) Log.d(DaVinCiExpression.sLogTag, "no color resource named $resName")
                        null
                    } else ContextCompat.getColor(context, id)
                } catch (e: Exception) {
                    if (DaVinCi.enableDebugLog) Log.e(DaVinCiExpression.sLogTag, "parse color exception", e)
                    return null
                }
            }

        }
    }

    interface Custom : IColorLookup {
        fun canLookup(resName: String?, context: Context?): Boolean
    }
}
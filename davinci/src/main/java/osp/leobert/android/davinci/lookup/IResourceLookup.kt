package osp.leobert.android.davinci.lookup

import android.content.Context
import android.util.Log
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.DaVinCi.Companion.takeIfInstance
import osp.leobert.android.davinci.DaVinCiConfig
import osp.leobert.android.davinci.DaVinCiExpression
import osp.leobert.android.davinci.IHandler
import osp.leobert.android.davinci.res.DaVinCiResource

/**
 * <p><b>Package:</b> osp.leobert.android.davinci.lookup </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> IResourceLookup </p>
 * <p><b>Description:</b> 资源查询interface & 内置规则默认实现 </p>
 * Created by leobert on 2022/4/14.
 */
interface IResourceLookup : IHandler {

    fun lookupResource(clazz: Class<*>, str: String?, context: Context?, daVinCi: DaVinCi?): Any?

    class DefaultColorIntResourceLookup : IResourceLookup {
        override fun lookupResource(clazz: Class<*>, str: String?, context: Context?, daVinCi: DaVinCi?): Any? {

            if (str.isNullOrEmpty()) return null

            if (DaVinCiResource.ColorIntRes::class.java != clazz) {
                return null
            }

            //分两步走的：
            // 1. 解析怎么定义的资源 可进一步扩展
            // 2. 解析（获取）资源对应的ColorInt --> 已完成
            return when {
                str.startsWith("@") -> {
                    getColor(
                        daVinCi?.context,
                        daVinCi,
                        /*colorResName*/daVinCi?.lookupTag(str.substring(1), daVinCi.context, daVinCi)
                    )
                }
                str.startsWith(DaVinCiExpression.sResourceColor) -> {
                    getColor(daVinCi?.context, daVinCi, /*colorResName*/str.substring(DaVinCiExpression.sResourceColor_length))
                }
                else -> {
                    getColor(daVinCi?.context, daVinCi, /*colorStr*/str)
                }
            }.run { DaVinCiResource.ColorIntRes(this) }
        }

        private fun getColor(context: Context?, daVinCi: DaVinCi?, colorResNameOrColorStr: String?): Int? {
            val lookup: IColorLookup = daVinCi ?: DaVinCiConfig.apply {
                if (DaVinCi.enableDebugLog) Log.e(DaVinCiExpression.sLogTag, "daVinCi is null.use default config to parse $colorResNameOrColorStr")
            }

            return lookup.lookupColor(colorResNameOrColorStr, context, daVinCi)
        }


    }

}
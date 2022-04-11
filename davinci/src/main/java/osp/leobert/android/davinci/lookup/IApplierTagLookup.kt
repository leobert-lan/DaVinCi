package osp.leobert.android.davinci.lookup

import android.content.Context
import osp.leobert.android.davinci.DaVinCi

/**
 * <p><b>Package:</b> osp.leobert.android.davinci.lookup </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> IApplierTagLookup </p>
 * Created by leobert on 2022/4/11.
 */
interface IApplierTagLookup {

    fun lookupTag(resName: String?, context: Context?, daVinCi: DaVinCi?): String?

    companion object {
        /**
         * Android View Tag 查询
         * */
        val InternalLookup: IApplierTagLookup = object : IApplierTagLookup {
            override fun lookupTag(resName: String?, context: Context?, daVinCi: DaVinCi?): String? {
                if (context == null) return null
                val resources = context.resources
                val id = resources.getIdentifier(resName, "id", context.packageName)
                return if (id == 0) {
                    null
                } else (daVinCi?.applier?.getTag(id) ?: "").toString()
            }
        }
    }

    interface Custom : IApplierTagLookup {
        fun canLookup(resName: String?, context: Context?): Boolean
    }
}
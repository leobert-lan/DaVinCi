package osp.leobert.android.davinci

/**
 * <p><b>Package:</b> osp.leobert.android.davinci </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> StateDrawableAdapter </p>
 * Created by leobert on 2021/5/31.
 */
interface StateDrawableAdapter {
    fun adapt(daVinCi: DaVinCi, daVinCiTemporal: DaVinCi, expression: DaVinCiExpression)
}
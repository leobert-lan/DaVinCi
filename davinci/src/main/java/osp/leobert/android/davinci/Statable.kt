package osp.leobert.android.davinci

/**
 * <p><b>Package:</b> osp.leobert.android.davinci </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> Statable </p>
 * Created by leobert on 7/28/21.
 */
interface Statable<T> {

    fun states(vararg states: State): T

    fun states(vararg states: String): T

}
package osp.leobert.android.davinci.pool

/**
 * Created by leobert on 2022/2/17.
 */
interface Poolable {
    fun reset()

    fun release()
}
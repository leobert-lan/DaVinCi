package osp.leobert.android.davinci

import androidx.core.util.Pools

/**
 * <p><b>Package:</b> osp.leobert.android.davinci </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> Pools </p>
 * Created by leobert on 2021/7/29.
 */
object DPools {

    private const val DEFAULT_POOL_SIZE = 3

    val cslSyntacticPool by lazy { simple(1, DaVinCiExpression.CslSyntactic.factory , DaVinCiExpression.CslSyntactic.resetter) }

    private val EMPTY_RESETTER: Resetter<Any> = object : Resetter<Any> {
        override fun reset(target: Any) {
            //do nothing
        }
    }

    private fun <T> emptyResetter(): Resetter<T> {
        return EMPTY_RESETTER as Resetter<T>
    }

    fun <T> simple(factory: Factory<T>): Pools.Pool<T> {
        return build(Pools.SimplePool(DEFAULT_POOL_SIZE), factory)
    }

    fun <T> simple(size: Int, factory: Factory<T>): Pools.Pool<T> {
        return build(Pools.SimplePool(size), factory)
    }

    fun <T> simple(size: Int, factory: Factory<T>, resetter: Resetter<T>): Pools.Pool<T> {
        return build(Pools.SimplePool(size), factory, resetter)
    }

    private fun <T> build(pool: Pools.Pool<T>, factory: Factory<T>): Pools.Pool<T> {
        return build(pool, factory, emptyResetter())
    }

    private fun <T> build(
        pool: Pools.Pool<T>, factory: Factory<T>, resetter: Resetter<T>,
    ): Pools.Pool<T> {
        return FactoryPool(pool, factory, resetter)
    }

    interface Factory<T> {
        fun create(): T
    }

    interface Resetter<T> {
        fun reset(target: T)
    }


    private class FactoryPool<T>(
        private val pool: Pools.Pool<T>, private val factory: Factory<T>, private val resetter: Resetter<T>,
    ) : Pools.Pool<T> {

        override fun acquire(): T? {
            var result = pool.acquire()
            if (result == null) {
                result = factory.create()
            }

            return result
        }

        override fun release(instance: T): Boolean {
            if (instance == null) return false

            resetter.reset(instance)
            return pool.release(instance)
        }
    }

}
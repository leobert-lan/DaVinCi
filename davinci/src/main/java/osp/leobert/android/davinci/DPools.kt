package osp.leobert.android.davinci

import androidx.core.util.Pools
import org.jetbrains.annotations.NotNull
import osp.leobert.android.davinci.syntactic.CslSyntactic
import osp.leobert.android.davinci.syntactic.SldSyntactic

/**
 * <p><b>Package:</b> osp.leobert.android.davinci </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> Pools </p>
 * Created by leobert on 2021/7/29.
 */
object DPools {

    private const val DEFAULT_POOL_SIZE = 3

    val cslSyntacticPool by lazy { simple(1, CslSyntactic.factory, CslSyntactic.resetter) }

    val sldSyntacticPool by lazy { simple(1, SldSyntactic.factory, SldSyntactic.resetter) }

    val dvcCoreSyntacticPool by lazy { simple(1, DaVinCiCore.DaVinCiCoreSyntactic.factory, DaVinCiCore.DaVinCiCoreSyntactic.resetter) }

    val daVinCiPool by lazy { simple(3, DaVinCi.factory, DaVinCi.resetter) }


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

        override fun release(@NotNull instance: T): Boolean {
            if (instance == null) return false

            resetter.reset(instance)
            return pool.release(instance)
        }
    }

}
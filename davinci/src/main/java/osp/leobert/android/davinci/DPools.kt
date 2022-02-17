package osp.leobert.android.davinci

import androidx.core.util.Pools
import org.jetbrains.annotations.NotNull
import osp.leobert.android.davinci.DaVinCiExpression.Companion.cast
import osp.leobert.android.davinci.expressions.*
import osp.leobert.android.davinci.expressions.Corners
import osp.leobert.android.davinci.expressions.Padding
import osp.leobert.android.davinci.expressions.Size
import osp.leobert.android.davinci.expressions.Solid
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

    internal val cslSyntacticPool by lazy { simple(DEFAULT_POOL_SIZE, CslSyntactic.factory, CslSyntactic.resetter) }

    internal val sldSyntacticPool by lazy { simple(DEFAULT_POOL_SIZE, SldSyntactic.factory, SldSyntactic.resetter) }

    internal val dvcCoreSyntacticPool by lazy {
        simple(
            DEFAULT_POOL_SIZE,
            DaVinCiCore.DaVinCiCoreSyntactic.factory,
            DaVinCiCore.DaVinCiCoreSyntactic.resetter
        )
    }

    val daVinCiPool by lazy { simple(DEFAULT_POOL_SIZE, DaVinCi.factory, DaVinCi.resetter) }

//    @Deprecated("CommandExpression 将修改为抽象类")
//    internal val commandExpPool by lazy { simple(DEFAULT_POOL_SIZE, CommandExpression.factory, DaVinCiExpression.resetter.cast()) }

    internal val cornersExpPool by lazy { simple(10, Corners.factory, DaVinCiExpression.resetter.cast()) }
    internal val solidExpPool by lazy { simple(10, Solid.factory, DaVinCiExpression.resetter.cast()) }
    internal val sizeExpPool by lazy { simple(10, Size.factory, DaVinCiExpression.resetter.cast()) }
    internal val shapeTypeExpPool by lazy { simple(10, ShapeType.factory, DaVinCiExpression.resetter.cast()) }
    internal val paddingExpPool by lazy { simple(10, Padding.factory, DaVinCiExpression.resetter.cast()) }
    internal val strokeExpPool by lazy { simple(10, Stroke.factory, DaVinCiExpression.resetter.cast()) }
    internal val gradientExpPool by lazy { simple(10, Gradient.factory, DaVinCiExpression.resetter.cast()) }
    internal val dStateExpPool by lazy { simple(10, DState.factory, DaVinCiExpression.resetter.cast()) }
    internal val listExpPool by lazy { simple(10, ListExpression.factory, DaVinCiExpression.resetter.cast()) }
    internal val statedColorExpPool by lazy { simple(10, StatedColor.factory, DaVinCiExpression.resetter.cast()) }

    internal val sldStubPool by lazy { simple(10, SldStub.factory, DaVinCiExpression.resetter.cast()) }
    internal val statedStubPool by lazy { simple(10, StatedStub.factory, DaVinCiExpression.resetter.cast()) }


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
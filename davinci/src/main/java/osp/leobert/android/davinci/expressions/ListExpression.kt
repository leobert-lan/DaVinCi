package osp.leobert.android.davinci.expressions

import android.annotation.SuppressLint
import osp.leobert.android.davinci.pool.DPools
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.DaVinCiExpression
import osp.leobert.android.davinci.uml.ExpDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import java.util.*

/**
 * 仅用于：直接使用序列化的DSL，且这一DSL（可能）同时包含了 StateListDrawable 和 ColorStateList
 *
 * 注意，它是语义无约束的，所以无法建立健全的校验机制，毕竟没有明确的语义，这意味着可能抛出异常，或者无法跳过'不支持的局部'而解析失败
 * */
@NotTerminal
@ExpDiagram
@GenerateClassDiagram
internal class ListExpression private constructor() : DaVinCiExpression() {

    companion object {

        val factory: DPools.Factory<ListExpression> = object : DPools.Factory<ListExpression> {
            override fun create(): ListExpression {
                return ListExpression()
            }
        }

        fun of(daVinCi: DaVinCi? = null): ListExpression {
            return requireNotNull(DPools.listExpPool.acquire()).apply {
                this.daVinCi = daVinCi
            }
        }
    }


    private val list: ArrayList<DaVinCiExpression> = ArrayList()

    override fun reset() {
        super.reset()
        list.clear()
    }


    override fun release() {
        super.release()
        list.forEach { it.release() }
        DPools.listExpPool.release(this)
    }

    fun append(exp: DaVinCiExpression) {
        list.add(exp)
    }

    @SuppressLint("all")
    override fun injectThenParse(daVinCi: DaVinCi?) {
        this.daVinCi = daVinCi
        list.forEach { it.injectThenParse(daVinCi) }
    }

    @SuppressLint("all")
    override fun interpret() {
        list.forEach { it.interpret() }
    }

    override fun toString(): String {
        val b = StringBuilder()

        val iMax: Int = list.size - 1
        if (iMax == -1) return b.toString()
        var i = 0
        while (true) {
            b.append(list[i].toString())
            if (i == iMax) return b.toString()
            b.append("; ")
            i++
        }
    }

}
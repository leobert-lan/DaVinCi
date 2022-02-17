package osp.leobert.android.davinci.expressions

import android.annotation.SuppressLint
import osp.leobert.android.davinci.DPools
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.DaVinCiExpression
import osp.leobert.android.davinci.uml.ExpDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import java.util.*

@NotTerminal
@ExpDiagram
@GenerateClassDiagram
@Deprecated("除了组合shape和statedcolorList外，目前无任何用途了")
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
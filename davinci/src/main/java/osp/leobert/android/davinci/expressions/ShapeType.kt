package osp.leobert.android.davinci.expressions

import osp.leobert.android.davinci.DPools
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.DaVinCiCore
import osp.leobert.android.reporter.review.TODO

// shape:[ st:[reactangle] ]
// Rectangle(0), Oval(1), Line(2), Ring(3);
@NotTerminal
@TODO(desc = "text的内容应当交由上下文lookUp，")
internal class ShapeType private constructor() : CommandExpression() {

    companion object {
        const val tag = "st:["

        const val Rectangle = "Rectangle"
        const val Oval = "Oval"
        const val Line = "Line"
        const val Ring = "Ring"

        val factory: DPools.Factory<ShapeType> = object : DPools.Factory<ShapeType> {
            override fun create(): ShapeType {
                return ShapeType()
            }
        }

        fun of(daVinCi: DaVinCi? = null, manual: Boolean = false):ShapeType {
            return requireNotNull(DPools.shapeTypeExpPool.acquire()).apply {
                this.manual = manual
                injectThenParse(daVinCi)
            }
        }
    }

    override fun release() {
        onRelease()
        DPools.shapeTypeExpPool.release(this)
    }

    override fun injectThenParse(daVinCi: DaVinCi?) {
        this.daVinCi = daVinCi
        if (manual) return
        asPrimitiveParse(tag, daVinCi)
    }

    override fun interpret() {
        if (tag == tokenName || manual) {
            daVinCi?.let {
                when {
                    Oval.equals(text, true) -> {
                        it.core.setShape(DaVinCiCore.Shape.Oval)
                    }
                    Line.equals(text, true) -> {
                        it.core.setShape(DaVinCiCore.Shape.Line)
                    }
                    Ring.equals(text, true) -> {
                        it.core.setShape(DaVinCiCore.Shape.Ring)
                    }
                    else -> {
                        it.core.setShape(DaVinCiCore.Shape.Rectangle)
                    }
                }
            }
        }
    }

    override fun toString(): String {
        return "$tag $text $END"
    }
}
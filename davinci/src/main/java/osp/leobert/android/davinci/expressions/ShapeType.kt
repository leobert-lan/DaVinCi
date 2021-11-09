package osp.leobert.android.davinci.expressions

import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.DaVinCiCore

// shape:[ st:[reactangle] ]
// Rectangle(0), Oval(1), Line(2), Ring(3);
internal class ShapeType private constructor(manual: Boolean = false) : CommandExpression(null, manual) {

    companion object {
        const val tag = "st:["

        const val Rectangle = "Rectangle"
        const val Oval = "Oval"
        const val Line = "Line"
        const val Ring = "Ring"

        fun of(daVinCi: DaVinCi? = null, manual: Boolean = false):ShapeType {
            return ShapeType(manual).apply {
                injectThenParse(daVinCi)
            }
        }
    }

//    init {
//        injectThenParse(daVinCi)
//    }

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
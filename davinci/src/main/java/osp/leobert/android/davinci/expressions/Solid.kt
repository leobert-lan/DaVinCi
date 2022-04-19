package osp.leobert.android.davinci.expressions

import androidx.annotation.ColorInt
import osp.leobert.android.davinci.pool.DPools
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.uml.ExpDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram

//region Solid
@NotTerminal
@ExpDiagram
@GenerateClassDiagram
internal class Solid private constructor() : ShapeSpecExpression() {
    @ColorInt
    internal var colorInt: Int? = null //这是解析出来的，不要乱赋值

    companion object {
        const val tag = "solid:["

        val factory: DPools.Factory<Solid> = object : DPools.Factory<Solid> {
            override fun create(): Solid {
                return Solid()
            }
        }

        fun of(daVinCi: DaVinCi? = null, manual: Boolean = false):Solid {
            return requireNotNull(DPools.solidExpPool.acquire()).apply {
                this.manual = manual
                injectThenParse(daVinCi)
            }
        }
    }

//    init {
//        injectThenParse(daVinCi)
//    }

    override fun reset() {
        super.reset()
        colorInt = null
    }

    override fun release() {
        super.release()
        DPools.solidExpPool.release(this)
    }

    override fun injectThenParse(daVinCi: DaVinCi?) {
        this.daVinCi = daVinCi

        if (manual) {
            if (parseFromText)
                colorInt = lookupColorInt(text)
            return
        }
        colorInt = null
        asPrimitiveParse(tag, daVinCi)
        colorInt = lookupColorInt(text)

    }

    override fun interpret() {
        if (tag == tokenName || manual) {
            daVinCi?.let {
                colorInt?.let { color ->
                    it.core.setSolidColor(color)
                }
            }
        }
    }

    override fun toString(): String {
        return "$tag ${if (parseFromText) text else colorInt?.run { text }} $END"
    }
}
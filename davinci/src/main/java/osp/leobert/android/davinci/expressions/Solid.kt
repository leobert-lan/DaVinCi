package osp.leobert.android.davinci.expressions

import androidx.annotation.ColorInt
import osp.leobert.android.davinci.DaVinCi

//region Solid
internal class Solid private constructor(manual: Boolean = false) : CommandExpression(manual) {
    @ColorInt
    internal var colorInt: Int? = null //这是解析出来的，不要乱赋值

    companion object {
        const val tag = "solid:["

        fun of(daVinCi: DaVinCi? = null, manual: Boolean = false):Solid {
            return Solid(manual).apply {
                injectThenParse(daVinCi)
            }
        }
    }

//    init {
//        injectThenParse(daVinCi)
//    }

    override fun injectThenParse(daVinCi: DaVinCi?) {
        this.daVinCi = daVinCi

        if (manual) {
            if (parseFromText)
                colorInt = parseColor(text)
            return
        }
        colorInt = null
        asPrimitiveParse(tag, daVinCi)
        colorInt = parseColor(text)

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
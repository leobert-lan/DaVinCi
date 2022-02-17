package osp.leobert.android.davinci.expressions

import androidx.annotation.Px
import osp.leobert.android.davinci.DPools
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.uml.ExpDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram

/**
 * Padding 一般不用, 使用它的View，在没有设置Padding的情况下，可使用此时设置的Padding
 *  <!--<padding-->
 *  <!--android:left="integer"-->
 *  <!--android:top="integer"-->
 *  <!--android:right="integer"-->
 *  <!--android:bottom="integer" />-->
 * */
@NotTerminal
@ExpDiagram
@GenerateClassDiagram
internal class Padding private constructor() : ShapeSpecExpression() {

    override fun startTag(): String = tag

    companion object {
        const val tag = "padding:["

        const val prop_left = "left:"
        const val prop_top = "top:"
        const val prop_right = "right:"
        const val prop_bottom = "bottom:"

        val factory: DPools.Factory<Padding> = object : DPools.Factory<Padding> {
            override fun create(): Padding {
                return Padding()
            }
        }

        fun of(daVinCi: DaVinCi? = null, manual: Boolean = false): Padding {
            return requireNotNull(DPools.paddingExpPool.acquire()).apply {
                this.manual = manual
                injectThenParse(daVinCi)
            }
        }
    }

    override fun reset() {
        super.reset()
        left = null
        top = null
        right = null
        bottom = null
    }

    override fun release() {
        onRelease()
        DPools.paddingExpPool.release(this)
    }

//    init {
//        injectThenParse(daVinCi)
//    }

    @Px
    var left: Int? = null

    @Px
    var top: Int? = null

    @Px
    var right: Int? = null

    @Px
    var bottom: Int? = null

    override fun injectThenParse(daVinCi: DaVinCi?) {
        this.daVinCi = daVinCi
        if (manual) {
            if (parseFromText)
                parse(daVinCi)
            return
        }
        asPrimitiveParse(tag, daVinCi)
        parse(daVinCi)
    }

    private fun parse(daVinCi: DaVinCi?) {
        text?.let { it ->
            left = null
            top = null
            right = null
            bottom = null

            //避免无效逻辑
            val dvc = daVinCi ?: return@let

            it.split(";").forEach { e ->
                when {
                    e.startsWith(prop_left) -> {
                        left = toPx(e.replace(prop_left, ""), dvc.context)
                    }

                    e.startsWith(prop_top) -> {
                        top = toPx(e.replace(prop_top, ""), dvc.context)
                    }

                    e.startsWith(prop_right) -> {
                        right = toPx(e.replace(prop_right, ""), dvc.context)
                    }

                    e.startsWith(prop_bottom) -> {
                        bottom = toPx(e.replace(prop_bottom, ""), dvc.context)
                    }
                }
            }
        }
    }

    override fun interpret() {
        if (tag == tokenName || manual) {
            daVinCi?.core?.setPadding(
                left?.toFloat() ?: 0f, top?.toFloat() ?: 0f,
                right?.toFloat() ?: 0f, bottom?.toFloat() ?: 0f
            )
        }
    }


    override fun toString(): String {
        return if (parseFromText)
            "$tag $text $END"
        else ("$tag ${left?.run { ";$prop_left$this" }}" +
                "${top?.run { ";$prop_top$this" }}" +
                "${right?.run { ";$prop_right$this" }}" +
                "${bottom?.run { ";$prop_bottom$this" }} $END").replaceFirst(";", "")
    }
}
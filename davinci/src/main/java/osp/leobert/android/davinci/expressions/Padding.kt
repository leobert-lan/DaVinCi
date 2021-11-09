package osp.leobert.android.davinci.expressions

import androidx.annotation.Px
import osp.leobert.android.davinci.DaVinCi

//region Padding 一般不用
//        <!--<padding-->
//        <!--android:left="integer"-->
//        <!--android:top="integer"-->
//        <!--android:right="integer"-->
//        <!--android:bottom="integer" />-->
internal class Padding(daVinCi: DaVinCi? = null, manual: Boolean = false) : CommandExpression(daVinCi, manual) {

    override fun startTag(): String = tag

    companion object {
        const val tag = "padding:["

        const val prop_left = "left:"
        const val prop_top = "top:"
        const val prop_right = "right:"
        const val prop_bottom = "bottom:"
    }

    init {
        injectThenParse(daVinCi)
    }

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

            //避免无效逻辑
            val dvc = daVinCi?:return@let

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
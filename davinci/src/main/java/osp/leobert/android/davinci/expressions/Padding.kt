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
            it.split(";").forEach { e ->
                when {
                    e.startsWith(prop_left) -> {
                        if (daVinCi != null)
                            left = toPx(e.replace(prop_left, ""), daVinCi.context)
                    }

                    e.startsWith(prop_top) -> {
                        if (daVinCi != null)
                            top = toPx(e.replace(prop_top, ""), daVinCi.context)
                    }

                    e.startsWith(prop_right) -> {
                        if (daVinCi != null)
                            right = toPx(e.replace(prop_right, ""), daVinCi.context)
                    }

                    e.startsWith(prop_bottom) -> {
                        if (daVinCi != null)
                            bottom = toPx(e.replace(prop_bottom, ""), daVinCi.context)
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
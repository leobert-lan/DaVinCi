package osp.leobert.android.davinci.expressions

import androidx.annotation.Px
import osp.leobert.android.davinci.DaVinCi

//region Size 一般不用
//        <!--<size-->
//        <!--android:width="integer"-->
//        <!--android:height="integer" />-->
internal class Size private constructor() : CommandExpression() {

    override fun startTag(): String = tag

    companion object {
        const val tag = "size:["

        const val prop_width = "width:"
        const val prop_height = "height:"

        fun of(daVinCi: DaVinCi? = null, manual: Boolean = false): Size {
            return Size().apply {
                this.manual = manual
                injectThenParse(daVinCi)
            }
        }
    }

//    init {
//        injectThenParse(daVinCi)
//    }

    @Px
    var width: Int? = null

    @Px
    var height: Int? = null

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
            width = null
            height = null
            it.split(";").forEach { e ->
                when {
                    e.startsWith(prop_width) -> {
                        if (daVinCi != null)
                            width = toPx(e.replace(prop_width, ""), daVinCi.context)
                    }

                    e.startsWith(prop_height) -> {
                        if (daVinCi != null)
                            height = toPx(e.replace(prop_height, ""), daVinCi.context)
                    }
                }
            }
        }
    }

    override fun interpret() {
        if (tag == tokenName || manual) {
            daVinCi?.let { d ->
                width?.let {
                    d.core.setSizeWidth(it.toFloat())
                }
                height?.let {
                    d.core.setSizeHeight(it.toFloat())
                }
            }
        }
    }


    override fun toString(): String {
        return if (parseFromText)
            "$tag $text $END"
        else ("$tag ${width?.run { ";$prop_width$this" }}" +
                "${height?.run { ";$prop_height$this" }} $END").replaceFirst(";", "")
    }
}
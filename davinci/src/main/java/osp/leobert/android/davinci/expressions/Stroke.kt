package osp.leobert.android.davinci.expressions

import androidx.annotation.ColorInt
import androidx.annotation.Px
import osp.leobert.android.davinci.DaVinCi

//region Stroke
//        <!--<stroke-->
//        <!--android:width="integer"-->
//        <!--android:color="color"-->
//        <!--android:dashWidth="integer"-->
//        <!--android:dashGap="integer" />-->
//shape:[ stroke:[ width:1dp;color:#aaaaaa;dashWidth:4;dashGap:6dp ] ]
internal class Stroke(daVinCi: DaVinCi? = null, manual: Boolean = false) : CommandExpression(daVinCi, manual) {

    override fun startTag(): String = tag

    companion object {
        const val tag = "stroke:["

        const val prop_width = "width:"
        const val prop_color = "color:"
        const val prop_dash_width = "dashWidth:"
        const val prop_dash_gap = "dashGap:"
    }

    init {
        injectThenParse(daVinCi)
    }

    @Px
    var width: Int? = null

    @ColorInt
    var color: Int? = null

    @Px
    var dash_width: Int? = null

    @Px
    var dash_gap: Int? = null

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
            color = null
            dash_gap = null
            dash_width = null
            it.split(";").forEach { e ->
                when {
                    e.startsWith(prop_width) -> {
                        if (daVinCi != null)
                            width = toPx(e.replace(prop_width, ""), daVinCi.context)
                    }
                    e.startsWith(prop_color) -> {
                        color = parseColor(e.replace(prop_color, ""))
                    }
                    e.startsWith(prop_dash_gap) -> {
                        if (daVinCi != null)
                            dash_gap = toPx(e.replace(prop_dash_gap, ""), daVinCi.context)
                    }
                    e.startsWith(prop_dash_width) -> {
                        if (daVinCi != null)
                            dash_width = toPx(e.replace(prop_dash_width, ""), daVinCi.context)
                    }
                }
            }
        }
    }

    override fun interpret() {
        if (tag == tokenName || manual) {
            daVinCi?.let { d ->
                color?.let {
                    d.core.setStrokeColor(it)
                }
                width?.let {
                    d.core.setStrokeWidth(it.toFloat())
                }
                dash_width?.let {
                    d.core.setStrokeDashWidth(it.toFloat())
                }
                dash_gap?.let {
                    d.core.setStrokeDashGap(it.toFloat())
                }
            }
        }
    }


    override fun toString(): String {
        return if (parseFromText)
            "$tag $text $END"
        else ("$tag ${width?.run { ";$prop_width$this" }}" +
                "${color?.run { ";$prop_color$this" }}" +
                "${dash_width?.run { ";$prop_dash_width$this" }}" +
                "${dash_gap?.run { ";$prop_dash_gap$this" }} $END").replaceFirst(";", "")
    }
}
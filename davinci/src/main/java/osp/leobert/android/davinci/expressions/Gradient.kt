package osp.leobert.android.davinci.expressions

import android.util.Log
import androidx.annotation.ColorInt
import osp.leobert.android.davinci.DPools
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.DaVinCiCore
import osp.leobert.android.davinci.DaVinCiExpression
import osp.leobert.android.davinci.uml.ExpDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram

//    <gradient
//    android:type=["linear" | "radial" | "sweep"]    //共有3中渐变类型，线性渐变（默认）/放射渐变/扫描式渐变
//    android:angle="integer"     //渐变角度，必须为45的倍数，0为从左到右，90为从上到下
//    android:centerX="float"     //渐变中心X的相当位置，范围为0～1
//    android:centerY="float"     //渐变中心Y的相当位置，范围为0～1
//    android:startColor="color"   //渐变开始点的颜色
//    android:centerColor="color"  //渐变中间点的颜色，在开始与结束点之间
//    android:endColor="color"    //渐变结束点的颜色
//    android:gradientRadius="float"  //渐变的半径，只有当渐变类型为radial时才能使用
//    android:useLevel=["true" | "false"] />  //使用LevelListDrawable时就要设置为true。设为false时才有渐变效果
@NotTerminal
@ExpDiagram
@GenerateClassDiagram
internal class Gradient private constructor() : ShapeSpecExpression() {

    override fun startTag(): String = tag

    companion object {
        const val tag = "gradient:["

        const val prop_start_color = "startColor:"
        const val prop_center_color = "centerColor:"
        const val prop_end_color = "endColor:"

        const val prop_type = "type:"

        const val prop_angle = "angle:"

        const val prop_center_x = "centerY:"
        const val prop_center_y = "centerX:"
        const val prop_use_level = "useLevel:"
        const val prop_gradient_radius = "gradientRadius:"

        const val TYPE_LINEAR = "linear"
        const val TYPE_RADIAL = "radial"
        const val TYPE_SWEEP = "sweep"

        val factory: DPools.Factory<Gradient> = object : DPools.Factory<Gradient> {
            override fun create(): Gradient {
                return Gradient()
            }
        }

        fun of(daVinCi: DaVinCi? = null, manual: Boolean = false): Gradient {
            return requireNotNull(DPools.gradientExpPool.acquire()).apply {
                this.manual = manual
                injectThenParse(daVinCi)
            }
        }
    }

    @ColorInt
    var startColor: Int? = null

    @ColorInt
    var centerColor: Int? = null

    @ColorInt
    var endColor: Int? = null

    var type: String? = TYPE_LINEAR //默认线性

    var angle: Int = 0

    var centerX: Float? = 0f
    var centerY: Float? = 0f
    var gradientRadius: Int? = null
    var useLevel: Boolean = false

    override fun reset() {
        super.reset()
          startColor = null
          centerColor = null
          endColor = null
          type= TYPE_LINEAR
          angle= 0
          centerX = 0f
          centerY= 0f
          gradientRadius= null
          useLevel = false
    }

    override fun release() {
        super.release()
        DPools.gradientExpPool.release(this)
    }

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
        val text = text
        if (text == null) {
            if (DaVinCi.enableDebugLog) {
                Log.e(sLogTag,"${javaClass.simpleName} invoke parse but text is null")
            }
            return
        }
        text.let { it ->
            startColor = null
            centerColor = null
            endColor = null
            type = TYPE_LINEAR
            angle = 0
            centerX = 0f
            centerY = 0f
            gradientRadius = null
            useLevel = false

            it.split(";").forEach { e ->
                when {
                    e.startsWith(prop_start_color) -> {
                        if (daVinCi != null)
                            startColor = lookupColorInt(e.replace(prop_start_color, ""))
                    }

                    e.startsWith(prop_center_color) -> {
                        if (daVinCi != null)
                            centerColor = lookupColorInt(e.replace(prop_center_color, ""))
                    }

                    e.startsWith(prop_end_color) -> {
                        if (daVinCi != null)
                            endColor = lookupColorInt(e.replace(prop_end_color, ""))
                    }

                    e.startsWith(prop_type) -> {
                        type = e.replace(prop_type, "")
                    }

                    e.startsWith(prop_angle) -> {
                        angle = parseInt(e.replace(prop_angle, ""), 0) ?: 0
                    }

                    e.startsWith(prop_center_x) -> {
                        centerX = parseFloat(e.replace(prop_center_x, ""), 0f) ?: 0f
                    }
                    e.startsWith(prop_center_y) -> {
                        centerY = parseFloat(e.replace(prop_center_y, ""), 0f) ?: 0f
                    }

                    e.startsWith(prop_use_level) -> {
                        // TODO: check why remove it
    //                            useLevel = parseFloat(e.replace(prop_center_x, ""), 0f) ?: 0f
                    }
                    e.startsWith(prop_gradient_radius) -> {
                        if (daVinCi != null)
                            gradientRadius =
                                toPx(e.replace(prop_gradient_radius, ""), daVinCi.context)
                    }

                    else -> {
                        if (DaVinCi.enableDebugLog) Log.d(sLogTag, "Gradient暂未支持解析:$e")
                    }
                }
            }
        }
    }

    override fun interpret() {
        if (tag == tokenName || manual) {
            daVinCi?.let { d ->
                d.core.setGradient(
                    when (type) {
                        TYPE_SWEEP -> DaVinCiCore.Gradient.Sweep
                        TYPE_RADIAL -> DaVinCiCore.Gradient.Radial
                        else -> DaVinCiCore.Gradient.Linear
                    }
                )
                d.core.setGradientCenterXY(centerX, centerY)
                d.core.setGradientColor(startColor, centerColor, endColor)
                d.core.setGradientAngle(angle)
                d.core.setGradientRadius(gradientRadius?.toFloat() ?: 0f)
            }
        }
    }


    override fun toString(): String {
        return if (parseFromText)
            "$tag $text $END"
        else ("$tag " +
                type.run { ";$prop_type$this" } +
                startColor.run { ";$prop_start_color$this" } +
                centerColor.run { ";$prop_center_color$this" } +
                endColor.run { ";$prop_end_color$this" } +
                centerX.run { ";$prop_center_x$this" } +
                centerY.run { ";$prop_center_y$this" } +
                angle.run { ";$prop_angle$this" } +
                gradientRadius.run { ";$prop_gradient_radius$this" }
                + " $END").replaceFirst(";", "")
    }
}
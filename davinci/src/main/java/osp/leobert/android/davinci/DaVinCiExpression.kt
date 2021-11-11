package osp.leobert.android.davinci

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.ColorInt
import androidx.annotation.Px
import osp.leobert.android.davinci.Applier.Companion.csl
import osp.leobert.android.davinci.Applier.Companion.viewBackground
import osp.leobert.android.davinci.expressions.*
import osp.leobert.android.davinci.syntactic.CslSyntactic
import osp.leobert.android.davinci.syntactic.SldSyntactic

@Suppress("WeakerAccess", "unused")
abstract class DaVinCiExpression {

    protected fun <T> log(str: String, any: T?): T? {
        if (DaVinCi.enableDebugLog) Log.d(sLogTag, "${javaClass.simpleName}:$str")
        return any
    }

    protected var daVinCi: DaVinCi? = null

    // 节点名称
    protected var tokenName: String? = null

    // 文本内容
    protected var text: String? = null
        set(value) {
            field = value
            onTextContentSet(value)
        }

    //属性是否需要从text中解析，一般手动创建的（manual == true，即非语法式解析）会直接赋予正确的属性值，不需要在此解析
    protected var parseFromText = true

    protected open fun onTextContentSet(text: String?) {
    }

    @CallSuper
    internal open fun reset() {
        daVinCi = null
        tokenName = null
        text = null
        parseFromText = true
    }

    open fun release() {
        //let child realize it
    }

    @CallSuper
    internal open fun onRelease() {

    }


    //一定会植入，手动创建的不解析
    abstract fun injectThenParse(daVinCi: DaVinCi?)

    /*
     * 执行方法
     */
    abstract fun interpret()

    open fun startTag(): String = ""

    internal open fun containsState(dState: DState): Boolean {
        return false
    }

    companion object {

        val resetter: DPools.Resetter<DaVinCiExpression> = object : DPools.Resetter<DaVinCiExpression> {
            override fun reset(target: DaVinCiExpression) {
                target.reset()
            }
        }

        fun <T : DaVinCiExpression> DPools.Resetter<DaVinCiExpression>.cast(): DPools.Resetter<T> {
            return resetter as DPools.Resetter<T>
        }

        @JvmStatic
        fun stateListDrawable(): StateListDrawable = StateListDrawable(manual = true)

        @JvmStatic
        fun shape(): Shape = Shape.of(manual = true)

        @JvmStatic
        fun stateColor(): ColorStateList = ColorStateList(manual = true)

        @JvmStatic
        fun shapeAndStateColor(
            shape: Shape,
            stateColor: ColorStateList,
        ): DaVinCiExpression = ListExpression.of(manual = true).apply {
            append(shape)
            append(stateColor)
        }

        @JvmStatic
        fun stateArray(vararg state: State) = state

        const val sLogTag = "DaVinCi"

        const val END = "]"

        const val NEXT = "];"

        const val sResourceColor = "rc/"
    }


    //region StateListDrawable
    class StateListDrawable internal constructor(val manual: Boolean = false) : DaVinCiExpression() {

        private var expressions: ShapeListExpression? = null
        override fun startTag(): String = tag

        internal fun shapeListExpression(): ShapeListExpression = expressions ?: ShapeListExpression.of(daVinCi, manual).apply {
            expressions = this
        }

        fun shape(exp: Shape): SldSyntactic {
            return SldSyntactic.of(this, exp)
        }

        companion object {
            const val tag = "sld:["
        }

        override fun injectThenParse(daVinCi: DaVinCi?) {
            this.daVinCi = daVinCi
            if (manual) return
            daVinCi?.next()
        }

        override fun interpret() {
            daVinCi?.let {
                if (manual) {
                    this.expressions = shapeListExpression().apply {
                        this.injectThenParse(it)
                        this.interpret()
                    }
                } else if (!it.equalsWithCommand(tag)) {
                    if (DaVinCi.enableDebugLog) Log.e(
                        sLogTag,
                        "The {$tag} is Excepted For Start When Not Manual! but got {${it.currentToken}}"
                    )
                } else {
                    //解析型
                    it.next()
                    this.expressions = shapeListExpression().apply {
                        this.injectThenParse(it)
                        this.interpret()
                    }
                }
            }
        }

        override fun toString(): String {
            return "$tag $expressions $END"
        }

        fun applyInto(view: View) {
            val daVinCi = DaVinCi.of(null, view.viewBackground())
            daVinCi.applySld(this) {
                daVinCi.release()
            }
        }
    }
    //endregion


    //region Shape
    class Shape internal constructor(val manual: Boolean = false) : DaVinCiExpression() {

        companion object {
            const val tag = "shape:["

            fun of(daVinCi: DaVinCi? = null, manual: Boolean = false): Shape {
                return Shape(manual).apply {
                    this.daVinCi = daVinCi
                }
            }
        }

        private var expressions: ListExpression? = null
        override fun startTag(): String = tag

        internal fun listExpression(): ListExpression {
            return expressions ?: ListExpression.of(daVinCi, manual).apply {
                expressions = this
            }
        }

        //region apis
        fun states(vararg states: State): Shape {
            val exp: ListExpression = listExpression()
            exp.appendState(*states)
            return this
        }

        internal fun statesCollect(): MutableCollection<State>? {
            return listExpression().states()
        }

        internal fun statesArray(): Array<State>? {
            return listExpression().statesArray()
        }

        fun type(str: String): Shape {
            ShapeType.of(manual = true).apply {
                this.text = str
                listExpression().append(this)
            }
            return this
        }

        fun rectAngle(): Shape {
            ShapeType.of(manual = true).apply {
                this.text = ShapeType.Rectangle
                parseFromText = false
                listExpression().append(this)
            }
            return this
        }

        fun oval(): Shape {
            ShapeType.of(manual = true).apply {
                this.text = ShapeType.Oval
                parseFromText = false
                listExpression().append(this)
            }
            return this
        }

        fun ring(): Shape {
            ShapeType.of(manual = true).apply {
                this.text = ShapeType.Ring
                parseFromText = false
                listExpression().append(this)
            }
            return this
        }

        fun line(): Shape {
            ShapeType.of(manual = true).apply {
                this.text = ShapeType.Line
                parseFromText = false
                listExpression().append(this)
            }
            return this
        }


        fun corner(@Px r: Int): Shape {
            Corners.of(manual = true).apply {
                this.conners = arrayListOf(r, r, r, r)
                parseFromText = false
                listExpression().append(this)
            }
            return this
        }

        fun corner(str: String): Shape {
            Corners.of(manual = true).apply {
                this.text = str
                parseFromText = true
                listExpression().append(this)
            }
            return this
        }

        fun corners(@Px lt: Int, @Px rt: Int, @Px rb: Int, @Px lb: Int): Shape {
            Corners.of(manual = true).apply {
                this.conners = arrayListOf(lt, rt, rb, lb)
                parseFromText = false
                listExpression().append(this)
            }
            return this
        }

        //e.g. "@tagid","#e5332c"
        fun solid(str: String): Shape {
            Solid.of(manual = true).apply {
                text = str
                listExpression().append(this)
            }
            return this
        }

        fun solid(@ColorInt color: Int): Shape {
            Solid.of(manual = true).apply {
                text = "#" + String.format("%8x", color)
                this.colorInt = color
                parseFromText = false
                listExpression().append(this)
            }
            return this
        }

        //e.g. color "@tagid","#e5332c"
        //width "4"->4px "3dp"->3dp
        fun stroke(width: String, color: String): Shape {
            Stroke.of(manual = true).apply {
                text = Stroke.prop_width + width + ";" + Stroke.prop_color + color
                listExpression().append(this)
            }
            return this
        }

        fun stroke(width: String, color: String, dashGap: String, dashWidth: String): Shape {
            Stroke.of(manual = true).apply {
                text =
                    "${Stroke.prop_width}$width;${Stroke.prop_color}$color;${Stroke.prop_dash_gap}$dashGap;${Stroke.prop_dash_width}$dashWidth"
                listExpression().append(this)
            }
            return this
        }

        fun stroke(@Px width: Int, @ColorInt colorInt: Int): Shape {
            Stroke.of(manual = true).apply {
                color = colorInt
                this.width = width
                parseFromText = false
                text = Stroke.prop_width + width + ";" + Stroke.prop_color + "#" + String.format(
                    "%8x",
                    colorInt
                )
                listExpression().append(this)
            }
            return this
        }

        fun gradient(
            type: String = Gradient.TYPE_LINEAR, @ColorInt startColor: Int, @ColorInt endColor: Int,
            angle: Int = 0,
        ): Shape {
            return gradient(type, startColor, null, endColor, 0f, 0f, angle)
        }

        //        @JvmOverloads 不适合使用kotlin的重载
        fun gradient(
            type: String = Gradient.TYPE_LINEAR, @ColorInt startColor: Int,
            @ColorInt centerColor: Int?, @ColorInt endColor: Int,
            centerX: Float,
            centerY: Float,
            angle: Int = 0,
        ): Shape {
            Gradient.of(manual = true).apply {
                this.type = type
                this.startColor = startColor
                this.centerColor = centerColor
                this.endColor = endColor

                this.centerX = centerX
                this.centerY = centerY
                this.angle = angle

                parseFromText = false
                //犯懒了，不想手拼了
                listExpression().append(this)
            }
            return this
        }

        fun gradient(startColor: String, endColor: String, angle: Int): Shape {
            return gradient(Gradient.TYPE_LINEAR, startColor, null, endColor, 0f, 0f, angle)
        }

        fun gradient(
            type: String = Gradient.TYPE_LINEAR,
            startColor: String,
            endColor: String,
            angle: Int = 0,
        ): Shape {
            return gradient(type, startColor, null, endColor, 0f, 0f, angle)
        }

        fun gradient(
            type: String = Gradient.TYPE_LINEAR, startColor: String,
            centerColor: String?, endColor: String, centerX: Float, centerY: Float, angle: Int,
        ): Shape {
            Gradient.of(manual = true).apply {
                text = type.run { "${Gradient.prop_type}$this" } +
                        startColor.run { ";${Gradient.prop_start_color}$this" } +
                        centerColor.run { ";${Gradient.prop_center_color}$this" } +
                        endColor.run { ";${Gradient.prop_end_color}$this" } +
                        centerX.run { ";${Gradient.prop_center_x}$this" } +
                        centerY.run { ";${Gradient.prop_center_y}$this" } +
                        angle.run { ";${Gradient.prop_angle}$this" }
                listExpression().append(this)
            }
            return this
        }
        //endregion


        override fun injectThenParse(daVinCi: DaVinCi?) {
            this.daVinCi = daVinCi
            if (manual) return
            daVinCi?.next()
        }

        override fun interpret() {
            daVinCi?.let {
                if (manual) {
                    this.expressions = listExpression().apply {
                        this.injectThenParse(it)
                        this.interpret()
                    }
                } else if (!it.equalsWithCommand(tag)) {
                    if (DaVinCi.enableDebugLog) Log.e(
                        sLogTag,
                        "The {$tag} is Excepted For Start When Not Manual! but got {${it.currentToken}}"
                    )
                } else {
                    //解析型
                    it.next()
                    this.expressions = listExpression().apply {
                        this.injectThenParse(it)
                        this.interpret()
                    }
                }
            }
        }

        override fun toString(): String {
            return "$tag $expressions $END"
        }

        fun applyInto(view: View) {
            val daVinCi = DaVinCi.of(null, view.viewBackground())
            daVinCi.applyShape(this) {
                daVinCi.release()
            }
        }
    }
    //endregion


    //region Color State List
    class ColorStateList internal constructor(private val manual: Boolean = false) : DaVinCiExpression() {

        private var expressions: ListExpression? = null
        override fun startTag(): String = tag


        internal fun listExpression(): ListExpression = expressions ?: ListExpression.of(daVinCi, manual).apply {
            expressions = this
        }


        companion object {
            const val tag = "csl:["
        }

        fun color(color: String): Statable<ColorStateList> {
            return CslSyntactic.of(this, color)
        }

        fun color(@ColorInt color: Int): Statable<ColorStateList> {
            return CslSyntactic.of(this, color)
        }

        override fun injectThenParse(daVinCi: DaVinCi?) {
            this.daVinCi = daVinCi
            if (manual) return
            daVinCi?.next()
        }

        override fun interpret() {
            daVinCi?.let {
                if (manual) {
                    this.expressions = listExpression().apply {
                        this.injectThenParse(it)
                        this.interpret()
                    }
                } else if (!it.equalsWithCommand(tag)) {
                    if (DaVinCi.enableDebugLog) Log.e(
                        sLogTag,
                        "The {$tag} is Excepted For Start When Not Manual! but got {${it.currentToken}}"
                    )
                } else {
                    //解析型
                    it.next()
                    this.expressions = listExpression().apply {
                        this.injectThenParse(it)
                        this.interpret()
                    }
                }
            }
        }

        override fun toString(): String {
            return "$tag $expressions $END"
        }

        fun applyInto(view: TextView) {
            val daVinCi = DaVinCi.of(null, view.csl())
            daVinCi.applyCsl(this) {
                daVinCi.release()
            }
        }
    }
    //endregion

}
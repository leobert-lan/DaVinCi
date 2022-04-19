package osp.leobert.android.davinci

import android.annotation.TargetApi
import android.content.res.ColorStateList
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.Px
import osp.leobert.android.davinci.pool.DPools

/*
 * Created by leobert on 2020-02-23.
 */
@Suppress("unused")
class DaVinCiCore {
    enum class Shape(var value: Int) {
        Rectangle(0), Oval(1), Line(2), Ring(3);
    }

    enum class Gradient(var value: Int) {
        Linear(0), Radial(1), Sweep(2);
    }

    enum class DrawablePosition {
        Left, Right, Top, Bottom
    }

    //region 普通shape属性
    private var shape = Shape.Rectangle

    @ColorInt
    private var solidColor: Int? = null

    @Px
    private var cornersRadius: Float? = null

    @Px
    private var cornersBottomLeftRadius: Float? = null

    @Px
    private var cornersBottomRightRadius: Float? = null

    @Px
    private var cornersTopLeftRadius: Float? = null

    @Px
    private var cornersTopRightRadius: Float? = null

    private var gradientAngle = -1

    @Px
    private var gradientCenterX: Float? = null

    @Px
    private var gradientCenterY: Float? = null

    @ColorInt
    private var gradientCenterColor: Int? = null

    @ColorInt
    private var gradientEndColor: Int? = null

    @ColorInt
    private var gradientStartColor: Int? = null

    @Px
    private var gradientRadius: Float? = null
    private var gradient = Gradient.Linear
    private var useLevel = false

    private val padding = Rect()

    @Px
    private var sizeWidth: Float? = null

    @Px
    private var sizeHeight: Float? = null

    private var strokeWidth: Float? = null

    @ColorInt
    private var strokeColor: Int? = null

    @Px
    private var strokeDashWidth = 0f

    @Px
    private var strokeDashGap = 0f

    private var rippleEnable = false

    @ColorInt
    private var rippleColor: Int? = null
    //endregion


    /**
     * used to build [ColorStateList]
     * */
    private val csl: MutableList<StateItem.ColorItem> by lazy { arrayListOf() }

    /**
     * used to build [StateListDrawable]
     * */
    private val sld: MutableList<StateItem.DrawableItem> by lazy { arrayListOf() }

    private var baseGradientDrawable: GradientDrawable? = null

    private var baseStateListDrawable: StateListDrawable? = null

    fun clearSimpleDrawable() {
        shape = Shape.Rectangle
        solidColor = null
        cornersRadius = null
        cornersBottomLeftRadius = null
        cornersBottomRightRadius = null
        cornersTopLeftRadius = null
        cornersTopRightRadius = null
        gradientAngle = -1
        gradientCenterX = null
        gradientCenterY = null
        gradientCenterColor = null
        gradientEndColor = null
        gradientStartColor = null

        gradientRadius = null
        gradient = Gradient.Linear
        useLevel = false
        padding.set(0, 0, 0, 0)
        sizeWidth = null
        sizeHeight = null
        strokeWidth = null
        strokeColor = null
        strokeDashWidth = 0f
        strokeDashGap = 0f
        rippleEnable = false
        rippleColor = null
    }

    fun clear() {
        clearSimpleDrawable()

        sld.clear()

        csl.clear()

        baseGradientDrawable = null
        baseStateListDrawable = null
    }

    fun setShape(shape: Shape): DaVinCiCore {
        this.shape = shape
        return this
    }

    fun setSolidColor(@ColorInt solidColor: Int): DaVinCiCore {
        this.solidColor = solidColor
        return this
    }

    fun setCornersRadius(@Px cornersRadius: Float): DaVinCiCore {
        this.cornersRadius = cornersRadius
        return this
    }

    fun setCornersRadius(
        @Px cornersBottomLeftRadius: Float,
        @Px cornersBottomRightRadius: Float,
        @Px cornersTopLeftRadius: Float,
        @Px cornersTopRightRadius: Float,
    ): DaVinCiCore {
        this.cornersBottomLeftRadius = cornersBottomLeftRadius
        this.cornersBottomRightRadius = cornersBottomRightRadius
        this.cornersTopLeftRadius = cornersTopLeftRadius
        this.cornersTopRightRadius = cornersTopRightRadius
        return this
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun setGradientAngle(gradientAngle: Int): DaVinCiCore {
        this.gradientAngle = gradientAngle
        return this
    }

    fun setGradientCenterXY(gradientCenterX: Float?, gradientCenterY: Float?): DaVinCiCore {
        this.gradientCenterX = gradientCenterX
        this.gradientCenterY = gradientCenterY
        return this
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun setGradientColor(@ColorInt startColor: Int?, @ColorInt endColor: Int?): DaVinCiCore {
        gradientStartColor = startColor
        gradientEndColor = endColor
        return this
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun setGradientColor(
        @ColorInt startColor: Int?,
        @ColorInt centerColor: Int?,
        @ColorInt endColor: Int?,
    ): DaVinCiCore {
        gradientStartColor = startColor
        gradientCenterColor = centerColor
        gradientEndColor = endColor
        return this
    }

    fun setGradientRadius(@Px gradientRadius: Float): DaVinCiCore {
        this.gradientRadius = gradientRadius
        return this
    }

    fun setGradient(gradient: Gradient): DaVinCiCore {
        this.gradient = gradient
        return this
    }

    fun setUseLevel(useLevel: Boolean): DaVinCiCore {
        this.useLevel = useLevel
        return this
    }

    fun setPadding(
        @Px paddingLeft: Float,
        @Px paddingTop: Float,
        @Px paddingRight: Float,
        @Px paddingBottom: Float,
    ): DaVinCiCore {
        padding.left = paddingLeft.toInt()
        padding.top = paddingTop.toInt()
        padding.right = paddingRight.toInt()
        padding.bottom = paddingBottom.toInt()
        return this
    }

    fun setSizeWidth(@Px sizeWidth: Float): DaVinCiCore {
        this.sizeWidth = sizeWidth
        return this
    }

    fun setSizeHeight(@Px sizeHeight: Float): DaVinCiCore {
        this.sizeHeight = sizeHeight
        return this
    }

    fun setStrokeWidth(@Px strokeWidth: Float): DaVinCiCore {
        this.strokeWidth = strokeWidth
        return this
    }

    fun setStrokeColor(@ColorInt strokeColor: Int): DaVinCiCore {
        this.strokeColor = strokeColor
        return this
    }

    fun setStrokeDashWidth(@Px strokeDashWidth: Float): DaVinCiCore {
        this.strokeDashWidth = strokeDashWidth
        return this
    }

    fun setStrokeDashGap(@Px strokeDashGap: Float): DaVinCiCore {
        this.strokeDashGap = strokeDashGap
        return this
    }

    fun setRipple(rippleEnable: Boolean, @ColorInt rippleColor: Int): DaVinCiCore {
        this.rippleEnable = rippleEnable
        this.rippleColor = rippleColor
        return this
    }

    fun setBaseGradientDrawable(baseGradientDrawable: GradientDrawable?): DaVinCiCore {
        this.baseGradientDrawable = baseGradientDrawable
        return this
    }

    fun setBaseStateListDrawable(baseStateListDrawable: StateListDrawable?): DaVinCiCore {
        this.baseStateListDrawable = baseStateListDrawable
        return this
    }

    /**
     * 构建[GradientDrawable]
     * */
    fun buildSimpleDrawable(): Drawable {
        val drawable: GradientDrawable = gradientDrawable

        val rippleColor = rippleColor ?: return drawable

        return if (rippleEnable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                RippleDrawable(
                    ColorStateList.valueOf(rippleColor),
                    drawable,
                    drawable
                )
            } else {
                val resultDrawable = StateListDrawable()
                val unPressDrawable = gradientDrawable
                unPressDrawable.setColor(rippleColor)
                resultDrawable.addState(intArrayOf(-android.R.attr.state_pressed), drawable)
                resultDrawable.addState(intArrayOf(android.R.attr.state_pressed), unPressDrawable)
                resultDrawable
            }
        } else drawable
    }

    /**
     * 如果[sld]有信息，则构建StateListDrawable，否则构建 [GradientDrawable]
     * */
    fun buildDrawable():Drawable? {
        var drawable: GradientDrawable? = null
        var stateListDrawable: StateListDrawable? = null
        if (sld.size > 0) {
            stateListDrawable = this.stateListDrawable
        } else {
            drawable = gradientDrawable
        }

        val rippleColor = rippleColor ?: return drawable ?: stateListDrawable

        return if (rippleEnable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val contentDrawable = stateListDrawable ?: drawable
                RippleDrawable(
                    ColorStateList.valueOf(rippleColor),
                    contentDrawable,
                    contentDrawable
                )
            } else {
                val resultDrawable = StateListDrawable()
                val unPressDrawable = gradientDrawable
                unPressDrawable.setColor(rippleColor)
                resultDrawable.addState(intArrayOf(-android.R.attr.state_pressed), drawable)
                resultDrawable.addState(intArrayOf(android.R.attr.state_pressed), unPressDrawable)
                resultDrawable
            }
        } else drawable ?: stateListDrawable
    }

//    @Deprecated("含义不够准确", ReplaceWith("buildDrawable()"))
//    fun build(): Drawable? {
//        return buildDrawable()
//    }

    /**
     * 利用[csl]信息，构建[ColorStateList]
     * */
    fun buildTextColor(): ColorStateList? {
        val csl = csl.takeUnless { it.isEmpty() } ?: return null
        val states = arrayOfNulls<IntArray>(csl.size)
        val colors = IntArray(csl.size)

        csl.forEachIndexed { index, colorItem: StateItem.ColorItem ->
            states[index] = colorItem.getStatesArray()
            colors[index] = colorItem.colorInt
        }
        return ColorStateList(states, colors)
    }

    private val stateListDrawable: StateListDrawable?
        get() {
            val sld = sld.takeUnless { it.isEmpty() } ?: return baseStateListDrawable
            val stateListDrawable = getStateListDrawable(baseStateListDrawable)

            sld.forEach { drawableItem ->
                stateListDrawable.addState(drawableItem.getStatesArray(), drawableItem.drawable)
            }

            return stateListDrawable
        }

    private val gradientDrawable: GradientDrawable
        get() {
            var drawable = baseGradientDrawable
            if (drawable == null) {
                drawable = GradientDrawable()
            }
            drawable.shape = shape.value
            cornersRadius?.let {
                drawable.cornerRadius = it
            }

            val blr = cornersBottomLeftRadius
            val brr = cornersBottomRightRadius
            val tlr = cornersTopLeftRadius
            val trr = cornersTopRightRadius
            if (blr != null && brr != null && tlr != null && trr != null) {
                val cornerRadius = FloatArray(8)
                cornerRadius[0] = tlr
                cornerRadius[1] = tlr
                cornerRadius[2] = trr
                cornerRadius[3] = trr
                cornerRadius[4] = brr
                cornerRadius[5] = brr
                cornerRadius[6] = blr
                cornerRadius[7] = blr
                drawable.cornerRadii = cornerRadius
            }
            if (gradient == Gradient.Linear && gradientAngle != -1) {
                gradientAngle %= 360
                if (gradientAngle % 45 == 0) {
                    var mOrientation =
                        GradientDrawable.Orientation.LEFT_RIGHT
                    when (gradientAngle) {
                        0 -> mOrientation =
                            GradientDrawable.Orientation.LEFT_RIGHT
                        45 -> mOrientation =
                            GradientDrawable.Orientation.BL_TR
                        90 -> mOrientation =
                            GradientDrawable.Orientation.BOTTOM_TOP
                        135 -> mOrientation =
                            GradientDrawable.Orientation.BR_TL
                        180 -> mOrientation =
                            GradientDrawable.Orientation.RIGHT_LEFT
                        225 -> mOrientation =
                            GradientDrawable.Orientation.TR_BL
                        270 -> mOrientation =
                            GradientDrawable.Orientation.TOP_BOTTOM
                        315 -> mOrientation =
                            GradientDrawable.Orientation.TL_BR
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        drawable.orientation = mOrientation
                    }
                }
            }

            val gcx = gradientCenterX
            val gcy = gradientCenterY
            if (gcx != null && gcy != null) {
                drawable.setGradientCenter(gcx, gcy)
            }

            val gsc = gradientStartColor
            val gec = gradientEndColor
            if (gsc != null && gec != null) {
                val colors: IntArray
                val gcc = gradientCenterColor
                if (gcc != null) {
                    colors = IntArray(3)
                    colors[0] = gsc
                    colors[1] = gcc
                    colors[2] = gec
                } else {
                    colors = IntArray(2)
                    colors[0] = gsc
                    colors[1] = gec
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    drawable.colors = colors
                }
            }
            gradientRadius?.let {
                drawable.gradientRadius = it
            }
            drawable.gradientType = gradient.value
            drawable.useLevel = useLevel
            if (!padding.isEmpty) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    drawable.setPadding(padding.left, padding.top, padding.right, padding.bottom)
                } else {
                    try {
                        val paddingField =
                            drawable.javaClass.getDeclaredField("mPadding")
                        paddingField.isAccessible = true
                        paddingField[drawable] = padding
                    } catch (e: NoSuchFieldException) {
                        e.printStackTrace()
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    }
                }
            }
            val sw = sizeWidth
            val sh = sizeHeight
            if (sw != null && sh != null) {
                drawable.setSize(sw.toInt(), sh.toInt())
            }
            strokeWidth?.takeIf { it > 0 }?.let { strokeWidth ->
                strokeColor?.let { strokeColor ->
                    drawable.setStroke(
                        strokeWidth.toInt(),
                        strokeColor,
                        strokeDashWidth,
                        strokeDashGap
                    )

                }
            }
            solidColor?.let {
                drawable.setColor(it)
            }
            return drawable
        }

    private fun getStateListDrawable(stateListDrawable: StateListDrawable?): StateListDrawable {
        return stateListDrawable ?: StateListDrawable()
    }

    fun addDrawableItem(stateItem: StateItem.DrawableItem) {
        sld.add(stateItem)
    }

    fun addColorItem(stateItem: StateItem.ColorItem) {
        csl.add(stateItem)
    }

    fun asOneStateInStateListDrawable(): DaVinCiCoreSyntactic {
        return DaVinCiCoreSyntactic.of(this)
    }


    class DaVinCiCoreSyntactic private constructor() : Statable<DaVinCiCore> {
        private var host: DaVinCiCore? = null

        companion object {
            val factory: DPools.Factory<DaVinCiCoreSyntactic> = object : DPools.Factory<DaVinCiCoreSyntactic> {
                override fun create(): DaVinCiCoreSyntactic {
                    return DaVinCiCoreSyntactic()
                }
            }

            val resetter: DPools.Resetter<DaVinCiCoreSyntactic> = object : DPools.Resetter<DaVinCiCoreSyntactic> {
                override fun reset(target: DaVinCiCoreSyntactic) {
                    target.host = null
                }
            }

            fun of(host: DaVinCiCore): DaVinCiCoreSyntactic {
                return requireNotNull(DPools.dvcCoreSyntacticPool.acquire()).apply {
                    this.host = host
                }
            }
        }

        override fun states(vararg states: State): DaVinCiCore {

            val host = requireNotNull(host)
            host.buildSimpleDrawable().let {
                host.clearSimpleDrawable()
                host.addDrawableItem(StateItem.of(it).applyState(states = states.toList()))
            }

            DPools.dvcCoreSyntacticPool.release(this)
            return host
        }

        override fun states(vararg states: String): DaVinCiCore {
            val host = requireNotNull(host)
            val allStates = states.map { State.valueOf(it) }
            host.buildSimpleDrawable().let {
                host.clearSimpleDrawable()
                host.addDrawableItem(StateItem.of(it).applyState(states = allStates))
            }
            DPools.dvcCoreSyntacticPool.release(this)
            return host
        }

        fun states(states: Collection<State>): DaVinCiCore {
            val host = requireNotNull(host)
            host.buildSimpleDrawable().let {
                host.clearSimpleDrawable()
                host.addDrawableItem(StateItem.of(it).applyState(states = states))
            }

            DPools.dvcCoreSyntacticPool.release(this)
            return host
        }
    }
}

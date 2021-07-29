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
import java.util.*

/*
 * <p><b>Package:</b> com.example.simpletest.background </p>
 * <p><b>Classname:</b> Expression </p>
 * Created by leobert on 2020-02-23.
 */


@Suppress("unused", "ObsoleteSdkInt")
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

    //region 状态色，方便一次性构造 deprecated
    @ColorInt
    private var checkableStrokeColor: Int? = null

    @ColorInt
    private var checkedStrokeColor: Int? = null

    @ColorInt
    private var enabledStrokeColor: Int? = null

    @ColorInt
    private var selectedStrokeColor: Int? = null

    @ColorInt
    private var pressedStrokeColor: Int? = null

    @ColorInt
    private var focusedStrokeColor: Int? = null

    @ColorInt
    private var unCheckableStrokeColor: Int? = null

    @ColorInt
    private var unCheckedStrokeColor: Int? = null

    @ColorInt
    private var unEnabledStrokeColor: Int? = null

    @ColorInt
    private var unSelectedStrokeColor: Int? = null

    @ColorInt
    private var unPressedStrokeColor: Int? = null

    @ColorInt
    private var unFocusedStrokeColor: Int? = null

    @ColorInt
    private var checkableSolidColor: Int? = null

    @ColorInt
    private var checkedSolidColor: Int? = null

    @ColorInt
    private var enabledSolidColor: Int? = null

    @ColorInt
    private var selectedSolidColor: Int? = null

    @ColorInt
    private var pressedSolidColor: Int? = null

    @ColorInt
    private var focusedSolidColor: Int? = null

    @ColorInt
    private var unCheckableSolidColor: Int? = null

    @ColorInt
    private var unCheckedSolidColor: Int? = null

    @ColorInt
    private var unEnabledSolidColor: Int? = null

    @ColorInt
    private var unSelectedSolidColor: Int? = null

    @ColorInt
    private var unPressedSolidColor: Int? = null

    @ColorInt
    private var unFocusedSolidColor: Int? = null
    //endregion

    //region 状态drawable,分层组装含义明确
    private var checkableDrawable: Drawable? = null
    private var checkedDrawable: Drawable? = null
    private var enabledDrawable: Drawable? = null
    private var selectedDrawable: Drawable? = null
    private var pressedDrawable: Drawable? = null
    private var focusedDrawable: Drawable? = null
    private var focusedHovered: Drawable? = null
    private var focusedActivated: Drawable? = null
    private var unCheckableDrawable: Drawable? = null
    private var unCheckedDrawable: Drawable? = null
    private var unEnabledDrawable: Drawable? = null
    private var unSelectedDrawable: Drawable? = null
    private var unPressedDrawable: Drawable? = null
    private var unFocusedDrawable: Drawable? = null
    private var unFocusedHovered: Drawable? = null
    private var unFocusedActivated: Drawable? = null
    //endregion


    @ColorInt
    private var checkableTextColor: Int? = null

    @ColorInt
    private var checkedTextColor: Int? = null

    @ColorInt
    private var enabledTextColor: Int? = null

    @ColorInt
    private var selectedTextColor: Int? = null

    @ColorInt
    private var pressedTextColor: Int? = null

    @ColorInt
    private var focusedTextColor: Int? = null

    @ColorInt
    private var unCheckableTextColor: Int? = null

    @ColorInt
    private var unCheckedTextColor: Int? = null

    @ColorInt
    private var unEnabledTextColor: Int? = null

    @ColorInt
    private var unSelectedTextColor: Int? = null

    @ColorInt
    private var unPressedTextColor: Int? = null

    @ColorInt
    private var unFocusedTextColor: Int? = null

    private val csl: MutableList<StateItem.ColorItem> = arrayListOf()

    private var textColorCount = 0

    private var hasSelectDrawable = false
    private var baseGradientDrawable: GradientDrawable? = null
    private var baseStateListDrawable: StateListDrawable? = null

    fun clear() {
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


        //region 状态色，方便一次性构造

        checkableStrokeColor = null
        checkedStrokeColor = null
        enabledStrokeColor = null
        selectedStrokeColor = null
        pressedStrokeColor = null
        focusedStrokeColor = null
        unCheckableStrokeColor = null
        unCheckedStrokeColor = null
        unEnabledStrokeColor = null
        unSelectedStrokeColor = null
        unPressedStrokeColor = null
        unFocusedStrokeColor = null
        checkableSolidColor = null
        checkedSolidColor = null
        enabledSolidColor = null
        selectedSolidColor = null
        pressedSolidColor = null
        focusedSolidColor = null
        unCheckableSolidColor = null
        unCheckedSolidColor = null
        unEnabledSolidColor = null
        unSelectedSolidColor = null
        unPressedSolidColor = null
        unFocusedSolidColor = null


        //region 状态drawable,分层组装含义明确
        checkableDrawable = null
        checkedDrawable = null
        enabledDrawable = null
        selectedDrawable = null
        pressedDrawable = null
        focusedDrawable = null
        focusedHovered = null
        focusedActivated = null
        unCheckableDrawable = null
        unCheckedDrawable = null
        unEnabledDrawable = null
        unSelectedDrawable = null
        unPressedDrawable = null
        unFocusedDrawable = null
        unFocusedHovered = null
        unFocusedActivated = null

        checkableTextColor = null
        checkedTextColor = null
        enabledTextColor = null
        selectedTextColor = null
        pressedTextColor = null
        focusedTextColor = null
        unCheckableTextColor = null
        unCheckedTextColor = null
        unEnabledTextColor = null
        unSelectedTextColor = null
        unPressedTextColor = null
        unFocusedTextColor = null
        textColorCount = 0

        csl.clear()

        hasSelectDrawable = false
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
    fun setGradientColor(@ColorInt startColor: Int?, @ColorInt centerColor: Int?, @ColorInt endColor: Int?): DaVinCiCore {
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

    fun setPadding(@Px paddingLeft: Float, @Px paddingTop: Float, @Px paddingRight: Float, @Px paddingBottom: Float): DaVinCiCore {
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

    fun setCheckableStrokeColor(@ColorInt checkableStrokeColor: Int, @ColorInt unCheckableStrokeColor: Int): DaVinCiCore {
        this.checkableStrokeColor = checkableStrokeColor
        this.unCheckableStrokeColor = unCheckableStrokeColor
        return this
    }

    fun setCheckedStrokeColor(@ColorInt checkedStrokeColor: Int, @ColorInt unCheckedStrokeColor: Int): DaVinCiCore {
        this.checkedStrokeColor = checkedStrokeColor
        this.unCheckedStrokeColor = unCheckedStrokeColor
        return this
    }

    fun setEnabledStrokeColor(@ColorInt enabledStrokeColor: Int, @ColorInt unEnabledStrokeColor: Int): DaVinCiCore {
        this.enabledStrokeColor = enabledStrokeColor
        this.unEnabledStrokeColor = unEnabledStrokeColor
        return this
    }

    fun setSelectedStrokeColor(@ColorInt selectedStrokeColor: Int, @ColorInt unSelectedStrokeColor: Int): DaVinCiCore {
        this.selectedStrokeColor = selectedStrokeColor
        this.unSelectedStrokeColor = unSelectedStrokeColor
        return this
    }

    fun setPressedStrokeColor(@ColorInt pressedStrokeColor: Int, @ColorInt unPressedStrokeColor: Int): DaVinCiCore {
        this.pressedStrokeColor = pressedStrokeColor
        this.unPressedStrokeColor = unPressedStrokeColor
        return this
    }

    fun setFocusedStrokeColor(@ColorInt focusedStrokeColor: Int, @ColorInt unFocusedStrokeColor: Int): DaVinCiCore {
        this.focusedStrokeColor = focusedStrokeColor
        this.unFocusedStrokeColor = unFocusedStrokeColor
        return this
    }

    fun setCheckableSolidColor(@ColorInt checkableSolidColor: Int, @ColorInt unCheckableSolidColor: Int): DaVinCiCore {
        this.checkableSolidColor = checkableSolidColor
        this.unCheckableSolidColor = unCheckableSolidColor
        return this
    }

    fun setCheckedSolidColor(@ColorInt checkedSolidColor: Int, @ColorInt unCheckedSolidColor: Int): DaVinCiCore {
        this.checkedSolidColor = checkedSolidColor
        this.unCheckedSolidColor = unCheckedSolidColor
        return this
    }

    fun setEnabledSolidColor(@ColorInt enabledSolidColor: Int, @ColorInt unEnabledSolidColor: Int): DaVinCiCore {
        this.enabledSolidColor = enabledSolidColor
        this.unEnabledSolidColor = unEnabledSolidColor
        return this
    }

    fun setSelectedSolidColor(@ColorInt selectedSolidColor: Int, @ColorInt unSelectedSolidColor: Int): DaVinCiCore {
        this.selectedSolidColor = selectedSolidColor
        this.unSelectedSolidColor = unSelectedSolidColor
        return this
    }

    fun setPressedSolidColor(@ColorInt pressedSolidColor: Int, @ColorInt unPressedSolidColor: Int): DaVinCiCore {
        this.pressedSolidColor = pressedSolidColor
        this.unPressedSolidColor = unPressedSolidColor
        return this
    }

    fun setFocusedSolidColor(@ColorInt focusedSolidColor: Int, @ColorInt unFocusedSolidColor: Int): DaVinCiCore {
        this.focusedSolidColor = focusedSolidColor
        this.unFocusedSolidColor = unFocusedSolidColor
        return this
    }

    fun setCheckableDrawable(checkableDrawable: Drawable?): DaVinCiCore {
        hasSelectDrawable = true
        this.checkableDrawable = checkableDrawable
        return this
    }

    fun setCheckedDrawable(checkedDrawable: Drawable?): DaVinCiCore {
        hasSelectDrawable = true
        this.checkedDrawable = checkedDrawable
        return this
    }

    fun setEnabledDrawable(enabledDrawable: Drawable?): DaVinCiCore {
        hasSelectDrawable = true
        this.enabledDrawable = enabledDrawable
        return this
    }

    fun setSelectedDrawable(selectedDrawable: Drawable?): DaVinCiCore {
        hasSelectDrawable = true
        this.selectedDrawable = selectedDrawable
        return this
    }

    fun setPressedDrawable(pressedDrawable: Drawable?): DaVinCiCore {
        hasSelectDrawable = true
        this.pressedDrawable = pressedDrawable
        return this
    }

    fun setFocusedDrawable(focusedDrawable: Drawable?): DaVinCiCore {
        hasSelectDrawable = true
        this.focusedDrawable = focusedDrawable
        return this
    }

    fun setFocusedHovered(focusedHovered: Drawable?): DaVinCiCore {
        hasSelectDrawable = true
        this.focusedHovered = focusedHovered
        return this
    }

    fun setFocusedActivated(focusedActivated: Drawable?): DaVinCiCore {
        hasSelectDrawable = true
        this.focusedActivated = focusedActivated
        return this
    }

    fun setUnCheckableDrawable(unCheckableDrawable: Drawable?): DaVinCiCore {
        hasSelectDrawable = true
        this.unCheckableDrawable = unCheckableDrawable
        return this
    }

    fun setUnCheckedDrawable(unCheckedDrawable: Drawable?): DaVinCiCore {
        hasSelectDrawable = true
        this.unCheckedDrawable = unCheckedDrawable
        return this
    }

    fun setUnEnabledDrawable(unEnabledDrawable: Drawable?): DaVinCiCore {
        hasSelectDrawable = true
        this.unEnabledDrawable = unEnabledDrawable
        return this
    }

    fun setUnSelectedDrawable(unSelectedDrawable: Drawable?): DaVinCiCore {
        hasSelectDrawable = true
        this.unSelectedDrawable = unSelectedDrawable
        return this
    }

    fun setUnPressedDrawable(unPressedDrawable: Drawable?): DaVinCiCore {
        hasSelectDrawable = true
        this.unPressedDrawable = unPressedDrawable
        return this
    }

    fun setUnFocusedDrawable(unFocusedDrawable: Drawable?): DaVinCiCore {
        hasSelectDrawable = true
        this.unFocusedDrawable = unFocusedDrawable
        return this
    }

    fun setUnFocusedHovered(unFocusedHovered: Drawable?): DaVinCiCore {
        hasSelectDrawable = true
        this.unFocusedHovered = unFocusedHovered
        return this
    }

    fun setUnFocusedActivated(unFocusedActivated: Drawable?): DaVinCiCore {
        hasSelectDrawable = true
        this.unFocusedActivated = unFocusedActivated
        return this
    }

    fun setCheckableTextColor(checkableTextColor: Int): DaVinCiCore {
        this.checkableTextColor = checkableTextColor
        textColorCount++
        return this
    }

    fun setCheckedTextColor(checkedTextColor: Int): DaVinCiCore {
        this.checkedTextColor = checkedTextColor
        textColorCount++
        return this
    }

    fun setEnabledTextColor(enabledTextColor: Int): DaVinCiCore {
        this.enabledTextColor = enabledTextColor
        textColorCount++
        return this
    }

    fun setSelectedTextColor(selectedTextColor: Int): DaVinCiCore {
        this.selectedTextColor = selectedTextColor
        textColorCount++
        return this
    }

    fun setPressedTextColor(pressedTextColor: Int): DaVinCiCore {
        this.pressedTextColor = pressedTextColor
        textColorCount++
        return this
    }

    fun setFocusedTextColor(focusedTextColor: Int): DaVinCiCore {
        this.focusedTextColor = focusedTextColor
        textColorCount++
        return this
    }

    fun setUnCheckableTextColor(unCheckableTextColor: Int): DaVinCiCore {
        this.unCheckableTextColor = unCheckableTextColor
        textColorCount++
        return this
    }

    fun setUnCheckedTextColor(unCheckedTextColor: Int): DaVinCiCore {
        this.unCheckedTextColor = unCheckedTextColor
        textColorCount++
        return this
    }

    fun setUnEnabledTextColor(unEnabledTextColor: Int): DaVinCiCore {
        this.unEnabledTextColor = unEnabledTextColor
        textColorCount++
        return this
    }

    fun setUnSelectedTextColor(unSelectedTextColor: Int): DaVinCiCore {
        this.unSelectedTextColor = unSelectedTextColor
        textColorCount++
        return this
    }

    fun setUnPressedTextColor(unPressedTextColor: Int): DaVinCiCore {
        this.unPressedTextColor = unPressedTextColor
        textColorCount++
        return this
    }

    fun setUnFocusedTextColor(unFocusedTextColor: Int): DaVinCiCore {
        this.unFocusedTextColor = unFocusedTextColor
        textColorCount++
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

    fun build(): Drawable? {
        var drawable: GradientDrawable? = null
        var stateListDrawable: StateListDrawable? = null
        if (hasSelectDrawable) {
            stateListDrawable = this.stateListDrawable
        } else {
            drawable = gradientDrawable
        }

        val rippleColor = rippleColor ?: return drawable ?: stateListDrawable

        return if (rippleEnable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val contentDrawable = stateListDrawable ?: drawable
                RippleDrawable(ColorStateList.valueOf(rippleColor), contentDrawable, contentDrawable)
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

    fun buildTextColor(): ColorStateList? {
        val csl = csl.takeUnless { it.isEmpty() } ?: return null
        val states = arrayOfNulls<IntArray>(csl.size)
        val colors = IntArray(csl.size)

        csl.forEachIndexed { index, colorItem: StateItem.ColorItem ->
            states[index] = colorItem.getStatesArray()
            colors[index] = colorItem.colorInt
        }
        return ColorStateList(states, colors)

//
//        return if (textColorCount > 0) {
//            colorStateList
//        } else {
//            null
//        }
    }

//    private val colorStateList: ColorStateList
//        get() {
//            val states = arrayOfNulls<IntArray>(textColorCount)
//            val colors = IntArray(textColorCount)
//            var index = 0
//
//            checkableTextColor?.let {
//                states[index] = intArrayOf(android.R.attr.state_checkable)
//                colors[index] = it
//                index++
//            }
//
//            unCheckableTextColor?.let {
//                states[index] = intArrayOf(-android.R.attr.state_checkable)
//                colors[index] = it
//                index++
//            }
//
//            checkedTextColor?.let {
//                states[index] = intArrayOf(android.R.attr.state_checked)
//                colors[index] = it
//                index++
//            }
//
//            unCheckedTextColor?.let {
//                states[index] = intArrayOf(-android.R.attr.state_checked)
//                colors[index] = it
//                index++
//            }
//
//            enabledTextColor?.let {
//                states[index] = intArrayOf(android.R.attr.state_enabled)
//                colors[index] = it
//                index++
//            }
//
//            unEnabledTextColor?.let {
//                states[index] = intArrayOf(-android.R.attr.state_enabled)
//                colors[index] = it
//                index++
//            }
//
//            selectedTextColor?.let {
//                states[index] = intArrayOf(android.R.attr.state_selected)
//                colors[index] = it
//                index++
//            }
//
//            unSelectedTextColor?.let {
//                states[index] = intArrayOf(-android.R.attr.state_selected)
//                colors[index] = it
//                index++
//            }
//
//            pressedTextColor?.let {
//                states[index] = intArrayOf(android.R.attr.state_pressed)
//                colors[index] = it
//                index++
//            }
//
//            unPressedTextColor?.let {
//                states[index] = intArrayOf(-android.R.attr.state_pressed)
//                colors[index] = it
//                index++
//            }
//
//            focusedTextColor?.let {
//                states[index] = intArrayOf(android.R.attr.state_focused)
//                colors[index] = it
//                index++
//            }
//
//            unFocusedTextColor?.let {
//                states[index] = intArrayOf(-android.R.attr.state_focused)
//                colors[index] = it
//                //no necessary to increase index if no more to be add
//            }
//
//            return ColorStateList(states, colors)
//        }

    private val stateListDrawable: StateListDrawable?
        get() {
            var stateListDrawable = baseStateListDrawable
            if (checkableDrawable != null) {
                stateListDrawable = getStateListDrawable(stateListDrawable)
                stateListDrawable.addState(intArrayOf(android.R.attr.state_checkable), checkableDrawable)
            }
            if (unCheckableDrawable != null) {
                stateListDrawable = getStateListDrawable(stateListDrawable)
                stateListDrawable.addState(intArrayOf(-android.R.attr.state_checkable), unCheckableDrawable)
            }
            if (checkedDrawable != null) {
                stateListDrawable = getStateListDrawable(stateListDrawable)
                stateListDrawable.addState(intArrayOf(android.R.attr.state_checked), checkedDrawable)
            }
            if (unCheckedDrawable != null) {
                stateListDrawable = getStateListDrawable(stateListDrawable)
                stateListDrawable.addState(intArrayOf(-android.R.attr.state_checked), unCheckedDrawable)
            }

            if (enabledDrawable != null) {
                stateListDrawable = getStateListDrawable(stateListDrawable)
                stateListDrawable.addState(intArrayOf(android.R.attr.state_enabled), enabledDrawable)
            }
            if (unEnabledDrawable != null) {
                stateListDrawable = getStateListDrawable(stateListDrawable)
                stateListDrawable.addState(intArrayOf(-android.R.attr.state_enabled), unEnabledDrawable)
            }


            if (selectedDrawable != null) {
                stateListDrawable = getStateListDrawable(stateListDrawable)
                stateListDrawable.addState(intArrayOf(android.R.attr.state_selected), selectedDrawable)
            }
            if (unSelectedDrawable != null) {
                stateListDrawable = getStateListDrawable(stateListDrawable)
                stateListDrawable.addState(intArrayOf(-android.R.attr.state_selected), unSelectedDrawable)
            }

            if (pressedDrawable != null) {
                stateListDrawable = getStateListDrawable(stateListDrawable)
                stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), pressedDrawable)
            }
            if (unPressedDrawable != null) {
                stateListDrawable = getStateListDrawable(stateListDrawable)
                stateListDrawable.addState(intArrayOf(-android.R.attr.state_pressed), unPressedDrawable)
            }

            if (focusedDrawable != null) {
                stateListDrawable = getStateListDrawable(stateListDrawable)
                stateListDrawable.addState(intArrayOf(android.R.attr.state_focused), focusedDrawable)
            }
            if (unFocusedDrawable != null) {
                stateListDrawable = getStateListDrawable(stateListDrawable)
                stateListDrawable.addState(intArrayOf(-android.R.attr.state_focused), unFocusedDrawable)
            }
            if (focusedHovered != null) {
                stateListDrawable = getStateListDrawable(stateListDrawable)
                stateListDrawable.addState(intArrayOf(android.R.attr.state_hovered), focusedHovered)
            }
            if (unFocusedHovered != null) {
                stateListDrawable = getStateListDrawable(stateListDrawable)
                stateListDrawable.addState(intArrayOf(-android.R.attr.state_hovered), unFocusedHovered)
            }
            if (focusedActivated != null) {
                stateListDrawable = getStateListDrawable(stateListDrawable)
                stateListDrawable.addState(intArrayOf(android.R.attr.state_activated), focusedActivated)
            }
            if (unFocusedActivated != null) {
                stateListDrawable = getStateListDrawable(stateListDrawable)
                stateListDrawable.addState(intArrayOf(-android.R.attr.state_activated), unFocusedActivated)
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
            if (cornersRadius != null) {
                drawable.cornerRadius = cornersRadius!!
            }
            if (cornersBottomLeftRadius != null && cornersBottomRightRadius != null
                && cornersTopLeftRadius != null && cornersTopRightRadius != null
            ) {
                val cornerRadius = FloatArray(8)
                cornerRadius[0] = cornersTopLeftRadius!!
                cornerRadius[1] = cornersTopLeftRadius!!
                cornerRadius[2] = cornersTopRightRadius!!
                cornerRadius[3] = cornersTopRightRadius!!
                cornerRadius[4] = cornersBottomRightRadius!!
                cornerRadius[5] = cornersBottomRightRadius!!
                cornerRadius[6] = cornersBottomLeftRadius!!
                cornerRadius[7] = cornersBottomLeftRadius!!
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
            if (gradientCenterX != null && gradientCenterY != null) {
                drawable.setGradientCenter(gradientCenterX!!, gradientCenterY!!)
            }
            if (gradientStartColor != null && gradientEndColor != null) {
                val colors: IntArray
                if (gradientCenterColor != null) {
                    colors = IntArray(3)
                    colors[0] = gradientStartColor!!
                    colors[1] = gradientCenterColor!!
                    colors[2] = gradientEndColor!!
                } else {
                    colors = IntArray(2)
                    colors[0] = gradientStartColor!!
                    colors[1] = gradientEndColor!!
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    drawable.colors = colors
                }
            }
            if (gradientRadius != null) {
                drawable.gradientRadius = gradientRadius!!
            }
            drawable.gradientType = gradient.value
            drawable.useLevel = useLevel
            if (!padding.isEmpty) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    drawable.setPadding(
                        padding.left,
                        padding.top,
                        padding.right,
                        padding.bottom
                    )
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
            if (sizeWidth != null && sizeHeight != null) {
                drawable.setSize(sizeWidth!!.toInt(), sizeHeight!!.toInt())
            }
            if (strokeWidth != null && strokeWidth!! > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    var start = 0
                    val stateList: ArrayList<Int> = ArrayList()
                    val colorList: ArrayList<Int> = ArrayList()
                    if (pressedStrokeColor != null && unPressedStrokeColor != null) {
                        stateList.add(android.R.attr.state_pressed)
                        stateList.add(-android.R.attr.state_pressed)
                        colorList.add(pressedStrokeColor!!)
                        colorList.add(unPressedStrokeColor!!)
                    }
                    if (checkableStrokeColor != null && unCheckableStrokeColor != null) {
                        stateList.add(android.R.attr.state_checkable)
                        stateList.add(-android.R.attr.state_checkable)
                        colorList.add(checkableStrokeColor!!)
                        colorList.add(unCheckableStrokeColor!!)
                    }
                    if (checkedStrokeColor != null && unCheckedStrokeColor != null) {
                        stateList.add(android.R.attr.state_checked)
                        stateList.add(-android.R.attr.state_checked)
                        colorList.add(checkedStrokeColor!!)
                        colorList.add(unCheckedStrokeColor!!)
                    }
                    if (enabledStrokeColor != null && unEnabledStrokeColor != null) {
                        stateList.add(android.R.attr.state_enabled)
                        stateList.add(-android.R.attr.state_enabled)
                        colorList.add(enabledStrokeColor!!)
                        colorList.add(unEnabledStrokeColor!!)
                    }
                    if (selectedStrokeColor != null && unSelectedStrokeColor != null) {
                        stateList.add(android.R.attr.state_selected)
                        stateList.add(-android.R.attr.state_selected)
                        colorList.add(selectedStrokeColor!!)
                        colorList.add(unSelectedStrokeColor!!)
                    }
                    if (focusedStrokeColor != null && unFocusedStrokeColor != null) {
                        stateList.add(android.R.attr.state_focused)
                        stateList.add(-android.R.attr.state_focused)
                        colorList.add(focusedStrokeColor!!)
                        colorList.add(unFocusedStrokeColor!!)
                    }
                    if (stateList.size > 0) {
                        val state = arrayOfNulls<IntArray>(stateList.size)
                        val color = IntArray(stateList.size)
                        for (iState in stateList) {
                            state[start] = intArrayOf(iState)
                            color[start] = colorList[start]
                            start++
                        }
                        val colorStateList =
                            ColorStateList(
                                state,
                                color
                            )
                        drawable.setStroke(
                            strokeWidth!!.toInt(),
                            colorStateList,
                            strokeDashWidth,
                            strokeDashGap
                        )
                    } else if (strokeColor != null) {
                        drawable.setStroke(
                            strokeWidth!!.toInt(),
                            strokeColor!!,
                            strokeDashWidth,
                            strokeDashGap
                        )
                    }
                } else if (strokeColor != null) {
                    drawable.setStroke(
                        strokeWidth!!.toInt(),
                        strokeColor!!,
                        strokeDashWidth,
                        strokeDashGap
                    )
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                var start = 0
                val stateList: ArrayList<Int> = ArrayList()
                val colorList: ArrayList<Int> = ArrayList()
                if (pressedSolidColor != null && unPressedSolidColor != null) {
                    stateList.add(android.R.attr.state_pressed)
                    stateList.add(-android.R.attr.state_pressed)
                    colorList.add(pressedSolidColor!!)
                    colorList.add(unPressedSolidColor!!)
                }
                if (checkableSolidColor != null && unCheckableSolidColor != null) {
                    stateList.add(android.R.attr.state_checkable)
                    stateList.add(-android.R.attr.state_checkable)
                    colorList.add(checkableSolidColor!!)
                    colorList.add(unCheckableSolidColor!!)
                }
                if (checkedSolidColor != null && unCheckedSolidColor != null) {
                    stateList.add(android.R.attr.state_checked)
                    stateList.add(-android.R.attr.state_checked)
                    colorList.add(checkedSolidColor!!)
                    colorList.add(unCheckedSolidColor!!)
                }
                if (enabledSolidColor != null && unEnabledSolidColor != null) {
                    stateList.add(android.R.attr.state_enabled)
                    stateList.add(-android.R.attr.state_enabled)
                    colorList.add(enabledSolidColor!!)
                    colorList.add(unEnabledSolidColor!!)
                }
                if (selectedSolidColor != null && unSelectedSolidColor != null) {
                    stateList.add(android.R.attr.state_selected)
                    stateList.add(-android.R.attr.state_selected)
                    colorList.add(selectedSolidColor!!)
                    colorList.add(unSelectedSolidColor!!)
                }
                if (focusedSolidColor != null && unFocusedSolidColor != null) {
                    stateList.add(android.R.attr.state_focused)
                    stateList.add(-android.R.attr.state_focused)
                    colorList.add(focusedSolidColor!!)
                    colorList.add(unFocusedSolidColor!!)
                }
                if (stateList.size > 0) {
                    val state = arrayOfNulls<IntArray>(stateList.size)
                    val color = IntArray(stateList.size)
                    for (iState in stateList) {
                        state[start] = intArrayOf(iState)
                        color[start] = colorList[start]
                        start++
                    }
                    val colorStateList =
                        ColorStateList(state, color)
                    drawable.color = colorStateList
                } else if (solidColor != null) {
                    drawable.setColor(solidColor!!)
                }
            } else if (solidColor != null) {
                drawable.setColor(solidColor!!)
            }
            return drawable
        }

    private fun getStateListDrawable(stateListDrawable: StateListDrawable?): StateListDrawable {
        return stateListDrawable ?: StateListDrawable()
    }

    fun addColorItem(stateItem: StateItem.ColorItem) {
        csl.add(stateItem)
    }
}
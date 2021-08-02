package osp.leobert.android.davinci

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import osp.leobert.android.davinci.Applier.Companion.applier
import osp.leobert.android.davinci.Applier.Companion.csl
import osp.leobert.android.davinci.Applier.Companion.viewBackground

/**
 * <p><b>Package:</b> osp.leobert.android.davinci </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> DaVinCiExt </p>
 * Created by leobert on 2021/5/31.
 */

//region 利用符合语法的String设置相关内容

fun View.daVinCiSld(str: String) {
    if (DaVinCi.enableDebugLog) Log.d(DaVinCiExpression.sLogTag, "${this.logTag()} daVinCi:$str")

    val daVinCi = DaVinCi.of(str, this.applier())
    val expressions = DaVinCiExpression.StateListDrawable()

    daVinCi.applySld(expressions)
    daVinCi.release()
}

@Deprecated("", ReplaceWith("this.daVinCiShape(str)"))
fun View.daVinCi(str: String) {
    this.daVinCiShape(str)
}

fun View.daVinCiShape(str: String) {
    if (DaVinCi.enableDebugLog) Log.d(DaVinCiExpression.sLogTag, "${this.logTag()} daVinCi:$str")

    val daVinCi = DaVinCi.of(str, this.applier())

    val expressions: DaVinCiExpression = DaVinCiExpression.Shape()

    expressions.injectThenParse(daVinCi)
    expressions.interpret()
    ViewCompat.setBackground(this, daVinCi.core.buildSimpleDrawable())
    daVinCi.release()
}

fun TextView.daVinCiColor(str: String) {
    if (DaVinCi.enableDebugLog) Log.d(DaVinCiExpression.sLogTag, "${this.logTag()} daVinCiColor:$str")
    val daVinCi = DaVinCi.of(str, this.csl())

    val expressions = DaVinCiExpression.stateColor()
    daVinCi.applyCsl(expressions)
    daVinCi.release()

//    expressions.injectThenParse(daVinCi)
//    expressions.interpret()
//
//    daVinCi.core.buildTextColor()?.let {
//        this.setTextColor(it)
//    }
}
//endregion


@BindingAdapter("daVinCiTextColor")
fun TextView.daVinCiColor(expressions: DaVinCiExpression.ColorStateList) {
    if (DaVinCi.enableDebugLog) Log.d(
        DaVinCiExpression.sLogTag,
        "${this.logTag()} daVinCiColor:$expressions"
    )
    val daVinCi = DaVinCi.of(null, this.csl())

    daVinCi.applyCsl(expressions)
    daVinCi.release()

//    expressions.injectThenParse(daVinCi)
//    expressions.interpret()
//
//    daVinCi.core.buildTextColor()?.let {
//        this.setTextColor(it)
//    }
}

@BindingAdapter("daVinCiBgStyle")
fun View.daVinCiBgStyle(styleName: String) {
    with(StyleRegistry.find(styleName)) {

        this?.applyTo(daVinCi = DaVinCi.of(null, this@daVinCiBgStyle.applier()), releaseAfter = true)
            ?: Log.d(DaVinCiExpression.sLogTag, "could not found style with name $styleName")
    }
}

@Deprecated("nobody want to read the log", ReplaceWith(""))
internal fun View.logTag(): String {
    return this.getTag(R.id.log_tag)?.run { "{${toString()}}:" } ?: this.toString()
}

/**
 * 2021/8/2 在原先的设计中，仅打算使用一对属性，但是这并不符合一般性需求，往往会出现组合情况，故而该API的表意是不恰当的，很容易导致误解,
 * 但是拆解开来作为多组API也会对原先的使用者造成影响（BindingAdapter的制约），
 * */
@BindingAdapter(
    "daVinCi_bg", "daVinCi_bg_pressed", "daVinCi_bg_unpressed",
    "daVinCi_bg_checkable", "daVinCi_bg_uncheckable", "daVinCi_bg_checked", "daVinCi_bg_unchecked",
    requireAll = false
)
@Deprecated("在原先的设计中，仅打算使用一对属性，但是这并不符合一般性需求，往往会出现组合情况，故而该API的表意是不恰当的，很容易导致误解")
fun View.daVinCi(
    normal: DaVinCiExpression? = null,
    pressed: DaVinCiExpression? = null, unpressed: DaVinCiExpression? = null,
    checkable: DaVinCiExpression? = null, uncheckable: DaVinCiExpression? = null,
    checked: DaVinCiExpression? = null, unchecked: DaVinCiExpression? = null,
) {
    val daVinCi = DaVinCi.of(null, this.viewBackground())

    normal?.let {
        daVinCi.apply {
            currentToken = normal.startTag()
        }
        if (DaVinCi.enableDebugLog) Log.d(
            DaVinCiExpression.sLogTag,
            "${this.logTag()} daVinCi normal:$normal"
        )

        normal.injectThenParse(daVinCi)
        normal.interpret()
    }

    pressed?.let {
        simplify(daVinCi,it,"pressed")
        daVinCi.core.asOneStateInStateListDrawable().states(State.PRESSED_T)
    }

    unpressed?.let {
        simplify(daVinCi,it,"unpressed")
        daVinCi.core.asOneStateInStateListDrawable().states(State.PRESSED_F)
    }

    checkable?.let {
        simplify(daVinCi,it,"checkable")
        daVinCi.core.asOneStateInStateListDrawable().states(State.CHECKABLE_T)
    }

    uncheckable?.let {
        simplify(daVinCi,it,"uncheckable")
        daVinCi.core.asOneStateInStateListDrawable().states(State.CHECKABLE_F)
    }

    checked?.let {
        simplify(daVinCi,it,"checked")
        daVinCi.core.asOneStateInStateListDrawable().states(State.CHECKED_T)
    }

    unchecked?.let {
        simplify(daVinCi,it,"unchecked")
        daVinCi.core.asOneStateInStateListDrawable().states(State.CHECKABLE_F)
    }


    //    private var enabledDrawable: Drawable? = null
    //    private var unEnabledDrawable: Drawable? = null
    //下面这一堆没啥意义了
    //    private var selectedDrawable: Drawable? = null
    //    private var focusedDrawable: Drawable? = null
    //    private var focusedHovered: Drawable? = null
    //    private var focusedActivated: Drawable? = null
    //    private var unSelectedDrawable: Drawable? = null
    //    private var unFocusedDrawable: Drawable? = null
    //    private var unFocusedHovered: Drawable? = null
    //    private var unFocusedActivated: Drawable? = null

    ViewCompat.setBackground(this, daVinCi.core.buildDrawable())
    daVinCi.release()
}

internal fun simplify(
    daVinCiLoop: DaVinCi,
    exp: DaVinCiExpression?,
    state: String,
) {
    exp?.let {
        daVinCiLoop.apply {
            currentToken = exp.startTag()
        }

        if (DaVinCi.enableDebugLog) Log.d(DaVinCiExpression.sLogTag, "daVinCi $state:$exp")

        exp.injectThenParse(daVinCiLoop)
        exp.interpret()
    }
}

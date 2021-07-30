package osp.leobert.android.davinci

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter

/**
 * <p><b>Package:</b> osp.leobert.android.davinci </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> DaVinCiExt </p>
 * Created by leobert on 2021/5/31.
 */

/*
 *自high 功能，语法解析，目前语法校验还不严格，没有按照严谨的文法约束以及校验
 * */
fun View.daVinCi(str: String) {
    if (DaVinCi.enableDebugLog) Log.d(DaVinCiExpression.sLogTag, "${this.logTag()} daVinCi:$str")
    val daVinCi = DaVinCi(str, this)

    val expressions: DaVinCiExpression = DaVinCiExpression.stateListDrawable()// DaVinCiExpression.Shape()

    expressions.injectThenParse(daVinCi)
    expressions.interpret()
    ViewCompat.setBackground(this, daVinCi.core.build())
}

fun TextView.daVinCiColor(str: String) {
    if (DaVinCi.enableDebugLog) Log.d(
        DaVinCiExpression.sLogTag,
        "${this.logTag()} daVinCiColor:$str"
    )
    val daVinCi = DaVinCi(str, this)

    val expressions: DaVinCiExpression = DaVinCiExpression.stateColor()

    expressions.injectThenParse(daVinCi)
    expressions.interpret()

    daVinCi.core.buildTextColor()?.let {
        this.setTextColor(it)
    }
}

@BindingAdapter("daVinCiTextColor")
fun TextView.daVinCiColor(expressions: DaVinCiExpression.ColorStateList) {
    if (DaVinCi.enableDebugLog) Log.d(
        DaVinCiExpression.sLogTag,
        "${this.logTag()} daVinCiColor:$expressions"
    )
    val daVinCi = DaVinCi(null, this)

    expressions.injectThenParse(daVinCi)
    expressions.interpret()

    daVinCi.core.buildTextColor()?.let {
        this.setTextColor(it)
    }
}

@BindingAdapter("daVinCiBgStyle")
fun View.daVinCiBgStyle(styleName: String) {
    with(StyleRegistry.find(styleName)) {

        this?.applyTo(
            DaVinCi(null, this@daVinCiBgStyle)
        )
            ?: Log.d(DaVinCiExpression.sLogTag, "could not found style with name $styleName")
    }
}

@Deprecated(
    "nobody want to read the log",
    ReplaceWith("")
)
internal fun View.logTag(): String {
    return this.getTag(R.id.log_tag)?.run { "{${toString()}}:" } ?: this.toString()
}

@BindingAdapter(
    "daVinCi_bg", "daVinCi_bg_pressed", "daVinCi_bg_unpressed",
    "daVinCi_bg_checkable", "daVinCi_bg_uncheckable", "daVinCi_bg_checked", "daVinCi_bg_unchecked",
    requireAll = false
)
fun View.daVinCi(
    normal: DaVinCiExpression? = null,
    pressed: DaVinCiExpression? = null, unpressed: DaVinCiExpression? = null,
    checkable: DaVinCiExpression? = null, uncheckable: DaVinCiExpression? = null,
    checked: DaVinCiExpression? = null, unchecked: DaVinCiExpression? = null
) {
    val daVinCi = DaVinCi(null, this)
    //用于多次构建
    val daVinCiLoop = DaVinCi(null, this)

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
        simplify(daVinCiLoop, it, "pressed")
        daVinCi.core.setPressedDrawable(daVinCiLoop.core.build())
        daVinCiLoop.core.clear()
    }

    unpressed?.let {
        simplify(daVinCiLoop, it, "unpressed")
        daVinCi.core.setUnPressedDrawable(daVinCiLoop.core.build())
        daVinCiLoop.core.clear()
    }

    checkable?.let {
        simplify(daVinCiLoop, it, "checkable")
        daVinCi.core.setCheckableDrawable(daVinCiLoop.core.build())
        daVinCiLoop.core.clear()
    }

    uncheckable?.let {
        simplify(daVinCiLoop, it, "uncheckable")
        daVinCi.core.setUnCheckableDrawable(daVinCiLoop.core.build())
        daVinCiLoop.core.clear()
    }

    checked?.let {
        simplify(daVinCiLoop, it, "checked")
        daVinCi.core.setCheckedDrawable(daVinCiLoop.core.build())
        daVinCiLoop.core.clear()
    }

    unchecked?.let {
        simplify(daVinCiLoop, it, "unchecked")
        daVinCi.core.setUnCheckedDrawable(daVinCiLoop.core.build())
        daVinCiLoop.core.clear()
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

    ViewCompat.setBackground(this, daVinCi.core.build())
}

internal fun simplify(
    daVinCiLoop: DaVinCi,
    exp: DaVinCiExpression?,
    state: String
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

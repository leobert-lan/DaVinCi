package osp.leobert.android.davinci

/*
 * Created by leobert on 2021/5/31.
 */
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.CheckResult
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import osp.leobert.android.davinci.Applier.Companion.applier
import osp.leobert.android.davinci.Applier.Companion.csl
import osp.leobert.android.davinci.Applier.Companion.viewBackground
import osp.leobert.android.davinci.DaVinCi.Companion.takeIfInstance


///////////////////////////////////////////////////////////////////////////
// deprecated and removed
///////////////////////////////////////////////////////////////////////////

//region deprecated

//@Deprecated("", ReplaceWith("this.daVinCiShape(str)"))
//fun View.daVinCi(str: String) {
//    this.daVinCiShape(str)
//}

//@BindingAdapter("daVinCiBgStyle")
//@Deprecated("含义不恰当", ReplaceWith("this.daVinCiStyle(styleName)"))
//fun View.daVinCiBgStyle(styleName: String) {
//    this.daVinCiStyle(styleName)
//}

//endregion deprecated

//region 利用符合语法的String设置相关内容

fun View.daVinCiSld(str: String) {
    if (DaVinCi.enableDebugLog) Log.d(DaVinCiExpression.sLogTag, "daVinCi:$str")

    val daVinCi = DaVinCi.of(str, this.applier())
    val expressions = DaVinCiExpression.StateListDrawable()

    daVinCi.applySld(expressions)
    daVinCi.release()
    expressions.release()
}


fun View.daVinCiShape(str: String) {
    if (DaVinCi.enableDebugLog) Log.d(DaVinCiExpression.sLogTag, "daVinCi:$str")

    val daVinCi = DaVinCi.of(str, this.applier())

    val expressions: DaVinCiExpression = DaVinCiExpression.Shape.of()

    expressions.injectThenParse(daVinCi)
    expressions.interpret()
    ViewCompat.setBackground(this, daVinCi.core.buildSimpleDrawable())
    daVinCi.release()
    expressions.release()
}

fun TextView.daVinCiColor(str: String) {
    if (DaVinCi.enableDebugLog) Log.d(DaVinCiExpression.sLogTag, "daVinCiColor:$str")
    val daVinCi = DaVinCi.of(str, this.csl())

    val expressions = DaVinCiExpression.stateColor()
    daVinCi.applyCsl(expressions)
    daVinCi.release()
    expressions.release()
}
//endregion


@BindingAdapter("daVinCiTextColor")
fun TextView.daVinCiColor(expressions: DaVinCiExpression.ColorStateList) {
    if (DaVinCi.enableDebugLog) Log.d(DaVinCiExpression.sLogTag, "daVinCiColor:$expressions")
    val daVinCi = DaVinCi.of(null, this.csl())

    daVinCi.applyCsl(expressions)
    daVinCi.release()
}

fun View.daVinCiShape(expressions: DaVinCiExpression.StateListDrawable) {
    if (DaVinCi.enableDebugLog) Log.d(DaVinCiExpression.sLogTag, "daVinCiShape:$expressions")
    val daVinCi = DaVinCi.of(null, this.applier())

    daVinCi.applySld(expressions)
    daVinCi.release()
}

@BindingAdapter("daVinCiStyle")
fun View.daVinCiStyle(styleName: String) {
    with(StyleRegistry.find(styleName)) {

        this?.applyTo(daVinCi = DaVinCi.of(null, this@daVinCiStyle.applier()), releaseAfter = true)
            ?: Log.d(DaVinCiExpression.sLogTag, "could not found style with name $styleName")
    }
}

@BindingAdapter("daVinCi_bg1", "states1", requireAll = false)
fun View.daVinCiBg1(exp: DaVinCiExpression.Shape, states: Array<State>? = null) {
    this.helpXmlDaVinCiBg(exp, states)
}

@BindingAdapter("daVinCi_bg2", "states2", requireAll = false)
fun View.daVinCiBg2(exp: DaVinCiExpression.Shape, states: Array<State>? = null) {
    this.helpXmlDaVinCiBg(exp, states)

}

@BindingAdapter("daVinCi_bg3", "states3", requireAll = false)
fun View.daVinCiBg3(exp: DaVinCiExpression.Shape, states: Array<State>? = null) {
    this.helpXmlDaVinCiBg(exp, states)
}

@BindingAdapter("daVinCi_bg4", "states4", requireAll = false)
fun View.daVinCiBg4(exp: DaVinCiExpression.Shape, states: Array<State>? = null) {
    this.helpXmlDaVinCiBg(exp, states)
}

@BindingAdapter("daVinCi_bg5", "states5", requireAll = false)
fun View.daVinCiBg5(exp: DaVinCiExpression.Shape, states: Array<State>? = null) {
    this.helpXmlDaVinCiBg(exp, states)
}

@BindingAdapter("daVinCi_bg6", "states6", requireAll = false)
fun View.daVinCiBg6(exp: DaVinCiExpression.Shape, states: Array<State>? = null) {
    this.helpXmlDaVinCiBg(exp, states)
}

@BindingAdapter("daVinCi_bg7", "states7", requireAll = false)
fun View.daVinCiBg7(exp: DaVinCiExpression.Shape, states: Array<State>? = null) {
    this.helpXmlDaVinCiBg(exp, states)
}

@BindingAdapter("daVinCi_bg8", "states8", requireAll = false)
fun View.daVinCiBg8(exp: DaVinCiExpression.Shape, states: Array<State>? = null) {
    this.helpXmlDaVinCiBg(exp, states)
}

private fun View.helpXmlDaVinCiBg(exp: DaVinCiExpression.Shape, states: Array<State>?) {
    val stateListDrawable = this.getTag(R.id.davinci_sld)?.takeIfInstance<DaVinCiExpression.StateListDrawable>()
        ?: DaVinCiExpression.stateListDrawable().apply {
            this@helpXmlDaVinCiBg.setTag(R.id.davinci_sld, this)
        }
    stateListDrawable.shape(exp).states(*requireNotNull(states ?: exp.statesArray())).applyInto(this)
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
@Deprecated(
    "在原先的设计中，仅打算使用一对属性，但是这并不符合一般性需求，往往会出现组合情况，故而该API的表意是不恰当的，很容易导致误解。" +
            "例如：enable_t/enable_f + checked_t/checked_f; 一般可以构建三组背景，对应 'enable_t+checked_t','enable_t+checked_f','enable_f'," +
            "但是原始设计中并不包含这一意图。需要注意，如果直接使用StateListDrawable可以解决问题，但是会导致xml宽度很宽！" +
            "下个版本会考虑添加最小成本的迁移方案"
)
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
            "daVinCi normal:$normal"
        )

        normal.injectThenParse(daVinCi)
        normal.interpret()
    }

    pressed?.let {
        val states = simplify(daVinCi, it, "pressed").apply { add(State.PRESSED_T) }
        daVinCi.core.asOneStateInStateListDrawable().states(states)
    }

    unpressed?.let {
        val states = simplify(daVinCi, it, "unpressed").apply { add(State.PRESSED_F) }
        daVinCi.core.asOneStateInStateListDrawable().states(states)
    }

    checkable?.let {
        val states = simplify(daVinCi, it, "checkable").apply { add(State.CHECKABLE_T) }
        daVinCi.core.asOneStateInStateListDrawable().states(states)
    }

    uncheckable?.let {
        val states = simplify(daVinCi, it, "uncheckable").apply { add(State.CHECKABLE_F) }
        daVinCi.core.asOneStateInStateListDrawable().states(states)
    }

    checked?.let {
        val states = simplify(daVinCi, it, "checked").apply { add(State.CHECKED_T) }
        daVinCi.core.asOneStateInStateListDrawable().states(states)
    }

    unchecked?.let {
        val states = simplify(daVinCi, it, "unchecked").apply { add(State.CHECKABLE_F) }
        daVinCi.core.asOneStateInStateListDrawable().states(states)
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

@CheckResult
@Deprecated("不建议使用")
internal fun simplify(
    daVinCiLoop: DaVinCi,
    exp: DaVinCiExpression?,
    state: String,
): MutableList<State> {
    exp?.let {
        daVinCiLoop.apply {
            currentToken = exp.startTag()
        }

        if (DaVinCi.enableDebugLog) Log.d(DaVinCiExpression.sLogTag, "daVinCi $state:$exp")

        exp.injectThenParse(daVinCiLoop)
        exp.interpret()

        return if (exp is DaVinCiExpression.Shape) {
            exp.listExpression().dState?.collect() ?: arrayListOf()
        } else {
            arrayListOf()
        }
    }
    return arrayListOf()
}

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
import osp.leobert.android.reporter.review.TODO


//region deprecated
//all deleted
//endregion deprecated

//region 利用符合语法的String设置相关内容

fun View.daVinCiSld(str: String) {
    if (DaVinCi.enableDebugLog) Log.d(DaVinCiExpression.sLogTag, "daVinCi:$str")

    val daVinCi = DaVinCi.of(str, this.applier())
    val expressions = DaVinCiExpression.StateListDrawable()

    daVinCi.applySld(expressions) {
        daVinCi.release()
        expressions.release()
    }
}


fun View.daVinCiShape(str: String) {
    if (DaVinCi.enableDebugLog) Log.d(DaVinCiExpression.sLogTag, "daVinCi:$str")

    val daVinCi = DaVinCi.of(str, this.applier())

    val expressions: DaVinCiExpression.Shape = DaVinCiExpression.Shape.of()
    daVinCi.applyShape(exp = expressions) {
        daVinCi.release()
        expressions.release()
    }
}

fun TextView.daVinCiColor(str: String) {
    if (DaVinCi.enableDebugLog) Log.d(DaVinCiExpression.sLogTag, "daVinCiColor:$str")
    val daVinCi = DaVinCi.of(str, this.csl())

    val expressions = DaVinCiExpression.ColorStateList()
    daVinCi.applyCsl(expressions) {
        daVinCi.release()
        expressions.release()
    }
}
//endregion


@BindingAdapter("daVinCiTextColor")
fun TextView.daVinCiColor(expressions: DaVinCiExpression.ColorStateList) {
    expressions.applyInto(this)
}

fun View.daVinCiShape(expressions: DaVinCiExpression.StateListDrawable) {
    expressions.applyInto(this)
}

@BindingAdapter("daVinCiStyle")
fun View.daVinCiStyle(styleName: String) {
    with(StyleRegistry.find(styleName)) {

        this?.applyTo(daVinCi = DaVinCi.of(null, this@daVinCiStyle.applier()), releaseAfter = true)
            ?: Log.d(DaVinCiExpression.sLogTag, "could not found style with name $styleName")
    }
}

//@BindingAdapter("daVinCi_bg1", "states1", requireAll = false)
//@TODO(desc = "这一系列API，依旧存在很大不合理")
//fun View.daVinCiBg1(exp: DaVinCiExpression.Shape, states: Array<State>? = null) {
//    this.helpXmlDaVinCiBg(exp, states)
//}
//
//@TODO(desc="很显然，目前的分布构建法，无法找到合适的切入点释放expression")
//private fun View.helpXmlDaVinCiBg(exp: DaVinCiExpression.Shape, states: Array<State>?) {
//    val stateListDrawable = this.getTag(R.id.davinci_sld)?.takeIfInstance<DaVinCiExpression.StateListDrawable>()
//        ?: DaVinCiExpression.stateListDrawable().apply {
//            this@helpXmlDaVinCiBg.setTag(R.id.davinci_sld, this)
//        }
//    stateListDrawable.shape(exp).states(*requireNotNull(states ?: exp.statesArray())).applyInto(this)
//}

@BindingAdapter("daVinCi_bg1", "states1",
    "daVinCi_bg2", "states2",
    "daVinCi_bg3", "states3",
    "daVinCi_bg4", "states4",
    "daVinCi_bg5", "states5",
    "daVinCi_bg6", "states6",
    "daVinCi_bg7", "states7",
    "daVinCi_bg8", "states8",
    requireAll = false)
@TODO(desc = "以XML资源定义为例，可能出现提示：This item is unreachable because a previous item (item #1) is a more general match than this one，" +
        "同样的，davinci也会面临这个问题，0.0.8版本中未处理，需要使用者注意顺序。应该在Sld内部处理好排序")
fun View.daVinCiBg1(exp1: DaVinCiExpression.Shape? = null, states1: Array<State>? = null,
                    exp2: DaVinCiExpression.Shape? = null, states2: Array<State>? = null,
                    exp3: DaVinCiExpression.Shape? = null, states3: Array<State>? = null,
                    exp4: DaVinCiExpression.Shape? = null, states4: Array<State>? = null,
                    exp5: DaVinCiExpression.Shape? = null, states5: Array<State>? = null,
                    exp6: DaVinCiExpression.Shape? = null, states6: Array<State>? = null,
                    exp7: DaVinCiExpression.Shape? = null, states7: Array<State>? = null,
                    exp8: DaVinCiExpression.Shape? = null, states8: Array<State>? = null) {
    DaVinCiExpression.stateListDrawable().apply {
        setIfNotNull(exp1,states1)
        setIfNotNull(exp2,states2)
        setIfNotNull(exp3,states3)
        setIfNotNull(exp4,states4)
        setIfNotNull(exp5,states5)
        setIfNotNull(exp6,states6)
        setIfNotNull(exp7,states7)
        setIfNotNull(exp8,states8)
        Log.e("lmsg","debug:$this")
    }.applyInto(this)

}

private fun DaVinCiExpression.StateListDrawable.setIfNotNull(
    exp: DaVinCiExpression.Shape? = null, states: Array<State>? = null
):DaVinCiExpression.StateListDrawable
{
    exp?.let {
        this.shape(it).states(*requireNotNull(states ?: it.statesArray()?: arrayOf()))
    }
    return this
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
            exp.statedStub().states() ?: arrayListOf()
        } else {
            arrayListOf()
        }
    }
    return arrayListOf()
}

inline fun <T> timeCost(info: String, job: () -> T): T {
    val nanoStart = System.nanoTime()
    val t: T = job()
    val cost = System.nanoTime() - nanoStart
    Log.d("davinci-timecost", "$info cost: $cost ns, about ${cost / 1000_000} ms")
    return t
}
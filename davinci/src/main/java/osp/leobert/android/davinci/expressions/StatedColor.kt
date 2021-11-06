package osp.leobert.android.davinci.expressions

import androidx.annotation.ColorInt
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.DaVinCiExpression
import osp.leobert.android.davinci.State
import osp.leobert.android.davinci.StateItem

internal class StatedColor internal constructor(daVinCi: DaVinCi? = null, manual: Boolean = false) :
    DaVinCiExpression.CommandExpression(daVinCi, manual) {
    @ColorInt
    var colorInt: Int? = null //这是解析出来的，不要乱赋值

    val states: MutableList<State> by lazy { arrayListOf() }

    companion object {
        const val tag = "sc:["

        const val prop_state = "state:"

        const val prop_color = "color:"

        const val separator = state_separator
    }

    init {
        injectThenParse(daVinCi)
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
        text?.let { content ->
            colorInt = null
            states.clear()

            content.split(";").forEach { e ->
                when {
                    e.startsWith(prop_color) -> {
                        if (daVinCi != null)
                            colorInt = parseColor(e.replace(prop_color, ""))
                    }
                    e.startsWith(prop_state) -> {
                        if (daVinCi != null) {
                            parseStates(e.replace(prop_state, ""))?.let { s ->
                                states.addAll(s)
                            }
                        }
                    }
                    else -> {
                        log("暂未支持解析:$e", null)
                    }
                }
            }

            if (states.isEmpty())
                log<State>("state 不能为空", null)
            colorInt ?: log<Int>("color 不能为空", null)
        }
    }

    override fun interpret() {
        val states: List<State> = (states.takeUnless { it.isEmpty() } ?: log<List<State>>("state 不能为空", null)) ?: return

        val colorInt = colorInt ?: log<Int>("color 不能为空", null) ?: return

        if (tag == tokenName || manual) {
            daVinCi?.core?.addColorItem(StateItem.of(colorInt).applyState(states))
        }
    }

    override fun toString(): String {
        return if (parseFromText)
            "$tag $text $END"
        else ("$tag " +
                states.run { ";$prop_state$this" } +
                colorInt.run { ";$prop_color$this" } +
                " $END").replaceFirst(";", "")
    }
}
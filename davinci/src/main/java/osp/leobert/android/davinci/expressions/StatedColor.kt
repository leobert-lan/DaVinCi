package osp.leobert.android.davinci.expressions

import androidx.annotation.ColorInt
import osp.leobert.android.davinci.DPools
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.State
import osp.leobert.android.davinci.StateItem
import osp.leobert.android.davinci.uml.ExpDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram

@NotTerminal
@ExpDiagram
@GenerateClassDiagram
internal class StatedColor private constructor() : CommandExpression() {
    @ColorInt
    var colorInt: Int? = null //这是解析出来的，不要乱赋值

    val states: MutableList<State> by lazy { arrayListOf() }

    companion object {
        const val tag = "sc:["

        const val prop_state = "state:"

        const val prop_color = "color:"

        private const val separator = state_separator

        val factory: DPools.Factory<StatedColor> = object : DPools.Factory<StatedColor> {
            override fun create(): StatedColor {
                return StatedColor()
            }
        }

        fun of(daVinCi: DaVinCi? = null, manual: Boolean = false): StatedColor {
            return requireNotNull(DPools.statedColorExpPool.acquire()).apply {
                this.manual = manual
                //todo 检查是否此处就应该使用null
                this.daVinCi = daVinCi
                injectThenParse(daVinCi)
            }
        }

        internal fun create(
            manual: Boolean = false, parseFromText: Boolean = true,
            colorInt: Int? = null, colorStr: String?, states: Array<out State>
        ): StatedColor {

            val ret = of(manual = manual)
            ret.colorInt = colorInt
            ret.states.addAll(states)
            ret.parseFromText = parseFromText
            ret.text = "$prop_state${states.joinToString(separator)};$prop_color${colorStr}"

            return ret
        }

        internal fun create(
            manual: Boolean = false, parseFromText: Boolean = true,
            colorInt: Int? = null, colorStr: String?, states: Array<out String>
        ): StatedColor {

            val ret = of(manual = manual)
            ret.colorInt = colorInt
            ret.parseFromText = parseFromText
            ret.text = "$prop_state${states.joinToString(separator)};$prop_color${colorStr}"

            return ret
        }

    }

//    init {
//        injectThenParse(null)
////        injectThenParse(daVinCi)
//    }

    override fun reset() {
        super.reset()
        manual = false
        colorInt = null
        states.clear()
    }

    override fun release() {
        super.release()
        DPools.statedColorExpPool.release(this)
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
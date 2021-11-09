package osp.leobert.android.davinci.expressions

import android.util.Log
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.State

/*
 * region state
 */
internal class DState internal constructor(
    daVinCi: DaVinCi? = null,
    manual: Boolean = false
) : CommandExpression(daVinCi, manual) {

    private val states: MutableSet<State> by lazy { linkedSetOf() }

    //caution: 可能尚未解析出实质的State,务必注意
    var statesHash: Int = 0
        private set


    fun appendStates(states: Array<out State>) {
        //ignore all check！
        this.states.addAll(states)
        text = states.joinToString(CommandExpression.state_separator)
    }

    override fun onTextContentSet(text: String?) {
        super.onTextContentSet(text)
        //state hash
        statesHash = text?.split(CommandExpression.state_separator)?.sorted()?.hashCode() ?: 0
        Log.e("lmsg", "set stateHash:$statesHash")
    }


    companion object {
        const val tag = "state:["
        private const val state_separator = CommandExpression.state_separator

        internal fun create(
            daVinCi: DaVinCi? = null, manual: Boolean = false, parseFromText: Boolean = true,
            states: Array<out State>
        ): DState {
            val ret = DState(daVinCi = daVinCi, manual = manual)
            ret.parseFromText = parseFromText
            ret.appendStates(states)
            return ret
        }

        internal fun create(
            daVinCi: DaVinCi? = null, manual: Boolean = false, parseFromText: Boolean = true,
            states: Array<out String>
        ): DState {
            val ret = DState(daVinCi = daVinCi, manual = manual)
            ret.parseFromText = parseFromText
            ret.text = states.joinToString(CommandExpression.state_separator)
            return ret
        }
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
            states.clear()
            if (daVinCi != null) {
                parseStates(content)?.let { s ->
                    states.addAll(s)
                }
            }

            states.takeUnless { it.isEmpty() } ?: log<State>("state 不能为空", null)
        }
    }

    override fun interpret() {
        states.takeUnless { it.isEmpty() } ?: log<State>("state 不能为空", null)

        if (tag == tokenName || manual) {
            daVinCi?.core?.asOneStateInStateListDrawable()?.states(states = states)
        }
    }

    override fun toString(): String {
        return "$tag ${if (parseFromText) text else states.joinToString(state_separator)} $END"
    }

    fun collect(): MutableList<State> {
        return states.toCollection(arrayListOf())
    }

    fun collectToArray():Array<State> {
        return states.toTypedArray()
    }
}
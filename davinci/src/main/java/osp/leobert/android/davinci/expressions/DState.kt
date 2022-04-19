package osp.leobert.android.davinci.expressions

import osp.leobert.android.davinci.pool.DPools
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.State
import osp.leobert.android.davinci.uml.ExpDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.android.reporter.review.TODO

/**
 * short for DaVinCiState, expression for [State]
 * states can be composed as a group of states, e.g. {CHECKABLE_T,CHECKED_T} ,{CHECKABLE_T,CHECKED_F}
 */
@NotTerminal
@ExpDiagram
@GenerateClassDiagram
internal class DState private constructor() : CommandExpression() {

    private val states: MutableSet<State> by lazy { linkedSetOf() }

    //caution: 可能尚未解析出实质的State,务必注意
    @TODO("设计改进，使用编码方式，注意text设置的需要及时解析")
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
    }

    fun stateChunksEncode():Int {
        return 0.apply {
            states.forEach {
                it.appendEncode(this)
            }
        }
    }

    companion object {
        const val tag = "state:["
        private const val state_separator = CommandExpression.state_separator

        val factory: DPools.Factory<DState> = object : DPools.Factory<DState> {
            override fun create(): DState {
                return DState()
            }
        }

        fun of(daVinCi: DaVinCi? = null, manual: Boolean = false): DState {
            return requireNotNull(DPools.dStateExpPool.acquire()).apply {
                this.manual = manual
                // 原先解析的原因：DState 在一组中需要保持唯一性，所以构建AST的过程中，需要立即判断去重，故建树过程中需要完成解析
                // 情况一共三种：
                // 1. 从序列化的DSL解析，则此时进入自动建树过程（manual为false），信息由DSL解析而出，所以是需要解析的
                // 2. 手工建立AST并赋予信息（manual为true），信息可能是：
                //    2.1: 枚举 -- 可正确计算hash
                //    2.2: 枚举对应的可解析文本  -- 也可以计算hash，但文本值如果错误将导致功能错误
                if (!manual) {
                    injectThenParse(daVinCi)
                }
            }
        }

        internal fun manualCreate(
            daVinCi: DaVinCi? = null, states: Array<out State>
        ): DState {
            val ret = of(daVinCi = daVinCi, manual = true)
            ret.parseFromText = false
            ret.appendStates(states)
            //实际上没有必要进行解析.内部也会抛弃解析
//            ret.injectThenParse(daVinCi)
            return ret
        }

        internal fun manualCreate(
            daVinCi: DaVinCi? = null, states: Array<out String>
        ): DState {
            val ret = of(daVinCi = daVinCi, manual = true)
            ret.parseFromText = true
            ret.text = states.joinToString(CommandExpression.state_separator).uppercase()
            //如果不考虑 text生成的hash纠错，也没有必要先行解析，TODO optimize
            ret.injectThenParse(daVinCi)
            return ret
        }
    }

    override fun reset() {
        super.reset()
        states.clear()
        statesHash = 0
    }

    override fun release() {
        super.release()
        DPools.dStateExpPool.release(this)
    }

    override fun injectThenParse(daVinCi: DaVinCi?) {
        this.daVinCi = daVinCi
        if (manual) {
            if (parseFromText)
                parse(daVinCi)
            //给定 State则无需解析
            return
        }
        //解析序列化的DSL，必然要进行字符串解析
        asPrimitiveParse(tag, daVinCi)
        parse(daVinCi)
    }

    private fun parse(daVinCi: DaVinCi?) {
        text?.let { content ->
            states.clear()
//            if (daVinCi != null) { 纯文本解析，不依赖DaVinCi，且不应当限制，进入条件必然是需要进行解析
            parseStates(content)?.let { s ->
                states.addAll(s)
            }
//            }

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

    internal fun collect(): MutableList<State> {
        return states.toCollection(arrayListOf())
    }

    internal fun collectToArray(): Array<State> {
        return states.toTypedArray()
    }
}
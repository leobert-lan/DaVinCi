package osp.leobert.android.davinci.expressions

import android.annotation.SuppressLint
import android.util.Log
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.DaVinCiExpression
import osp.leobert.android.davinci.State
import java.util.*

//region ListExp 同级别多条目解析
internal class ListExpression constructor(daVinCi: DaVinCi? = null, private val manual: Boolean = false) : DaVinCiExpression() {
    private val list: ArrayList<DaVinCiExpression> = ArrayList()

    /**
     * in rule: only one state expression is permitted
     * */
    var dState: DState? = null

    fun append(exp: DaVinCiExpression) {
        list.add(exp)
    }

    fun appendState(vararg states: State) {
        val dState = dState ?: DState(daVinCi)
        dState.appendStates(states)
        this.dState = dState
    }

    internal fun states():MutableCollection<State>? {
        return dState?.collect()
    }

    internal fun statesArray():Array<State>? {
        return dState?.collectToArray()
    }

    @SuppressLint("all")
    override fun injectThenParse(daVinCi: DaVinCi?) {
        this.daVinCi = daVinCi
        if (manual) {
            list.forEach { it.injectThenParse(daVinCi) }
            dState?.injectThenParse(daVinCi)
            return
        }

        // 在ListExpression解析表达式中,循环解释语句中的每一个单词,直到终结符表达式或者异常情况退出
        daVinCi?.let {
            var i = 0
            while (i < 100) { // true,语法错误时有点可怕，先上限100
                if (it.currentToken == null) { // 获取当前节点如果为 null 则表示缺少]表达式

                    if (DaVinCi.enableDebugLog) Log.e(
                        sLogTag,
                        "Error: The Expression Missing ']'! ")

                    break
                } else if (it.equalsWithCommand(END)) {
                    it.next()
                    // 解析正常结束
                    break
                } else if (it.equalsWithCommand(NEXT)) {
                    //进入同级别下一个解析
                    it.next()
                } else if (it.equalsWithCommand(DState.tag)) {
                    if (dState != null) {
                        if (DaVinCi.enableDebugLog) Log.d(
                            sLogTag,
                            "only one state expression is permitted in one group, will override old one $dState"
                        )
                    }
                    dState = DState(it)

                } else { // 建立Command 表达式
                    try {
                        val expressions: DaVinCiExpression = CommandExpression.of(it)
                        list.add(expressions)
                    } catch (e: Exception) {
                        if (DaVinCi.enableDebugLog) Log.e(sLogTag, "语法解析有误", e)
                        break
                    }
                }
                i++
            }
            if (i == 100) {
                if (DaVinCi.enableDebugLog) Log.e(sLogTag, "语法解析有误，进入死循环，强制跳出")
            }
        }
    }

    @SuppressLint("all")
    override fun interpret() {
        list.forEach { it.interpret() }
        dState?.interpret()
    }

    override fun toString(): String {
        val b = StringBuilder()

        dState?.let { b.append(it.toString()) }

        val iMax: Int = list.size - 1
        if (iMax == -1) return b.toString()
        if (dState != null)
            b.append("; ")
        var i = 0
        while (true) {
            b.append(list[i].toString())
            if (i == iMax) return b.toString()
            b.append("; ")
            i++
        }
    }

    override fun containsState(dState: DState): Boolean {
        return (this.dState?.statesHash ?: 0) == dState.statesHash
    }
}
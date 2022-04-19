package osp.leobert.android.davinci.expressions

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.CallSuper
import osp.leobert.android.davinci.*
import osp.leobert.android.davinci.pool.Poolable
import java.util.*

/**
 * <p><b>Package:</b> osp.leobert.android.davinci.expressions </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> ExpressionStub </p>
 * <p><b>Description:</b> 壳，按照状态 [State] 填充各类 [DaVinCiExpression] </p>
 * Created by leobert on 2022/2/17.
 */
internal abstract class ExpressionStub<T>(private val parsers: Strategy<String, StubItemParser<T>>) : Poolable {

    interface StubItemParser<T> : IHandler {
        fun parse(t: T, daVinCi: DaVinCi): DaVinCiExpression?
    }

    internal var manual: Boolean = false
    internal val list: ArrayList<DaVinCiExpression> = ArrayList()

    open fun append(exp: DaVinCiExpression) {
        var targetIndex: Int? = null
        for (i in list.indices) {
            if (list[i].isStateGeneralThan(exp)) {
                targetIndex = i
                break
            }
        }
        if (targetIndex == null) {
            list.add(exp)
        } else {
            list.add(targetIndex, exp)
        }
    }

    fun setExpression(dState: DState, exp: DaVinCiExpression) {
        list.removeAll {
            it.equalState(dState)
        }
        append(exp)
    }

    protected abstract fun getT(): T

    @CallSuper
    protected open fun onManualInjectThenParse(daVinCi: DaVinCi?) {
        list.forEach { it.injectThenParse(daVinCi) }
    }

    fun injectThenParse(daVinCi: DaVinCi?) {
        if (manual) {
            onManualInjectThenParse(daVinCi)
            return
        }

        // 在ListExpression解析表达式中,循环解释语句中的每一个单词,直到终结符表达式或者异常情况退出
        daVinCi?.let {
            var i = 0
            while (i < 100) {
                // true,语法错误时有点可怕，先上限100

                if (it.currentToken == null) {
                    // 获取当前节点如果为 null 则表示缺少]表达式
                    if (DaVinCi.enableDebugLog) Log.e(
                        DaVinCiExpression.sLogTag,
                        "Error: The Expression Missing ']'! "
                    )
                    break
                } else if (it.equalsWithCommand(DaVinCiExpression.END)) {
                    // 解析正常结束
                    it.next()
                    break
                } else if (it.equalsWithCommand(DaVinCiExpression.NEXT)) {
                    //进入同级别下一个解析
                    it.next()
                } else {
                    val parser = parsers.fetch(it.currentToken ?: "")
                    if (parser == null) {
                        if (DaVinCi.enableDebugLog) Log.e(
                            DaVinCiExpression.sLogTag,
                            "No parser registered to parse item in list for token: {${it.currentToken}}"
                        )
                    } else {
                        //when null returns,it means the chunk expression should not put into the list, e.g. DState
                        parser.parse(t = getT(), daVinCi = it)?.let { exp ->
                            //不需要再调用，parse时已经处理
//                            exp.injectThenParse(it)

                            // 解析延迟到对应的interpret chain
//                            exp.interpret()
                            //todo consider order for stated things
                            onExpParsed(exp)
                        }
                    }
                }


                i++
            }
            if (i == 100) {
                if (DaVinCi.enableDebugLog) Log.e(DaVinCiExpression.sLogTag, "语法解析有误，进入死循环，强制跳出")
            }
        }
    }

    fun onExpParsed(exp: DaVinCiExpression) {
        append(exp)
    }

    @SuppressLint("all")
    open fun interpret() {
        list.forEach { it.interpret() }
    }

    override fun reset() {
        manual = false
        list.clear()
    }

    override fun release() {
        list.forEach { it.release() }
    }

    override fun toString(): String {
        val b = StringBuilder()

        val iMax: Int = list.size - 1
        if (iMax == -1) return ""
        var i = 0
        while (true) {
            b.append(list[i].toString())
            if (i == iMax) return b.toString()
            b.append("; ")
            i++
        }
    }
}
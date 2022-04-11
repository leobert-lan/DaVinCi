package osp.leobert.android.davinci.expressions

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.CallSuper
import osp.leobert.android.davinci.*
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

    fun append(exp: DaVinCiExpression) {
        list.add(exp)
    }

    fun setExpression(dState: DState, exp: DaVinCiExpression) {
        list.removeAll {
            it.containsState(dState)
        }
        list.add(exp)
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
                        parser.parse(t = getT(), daVinCi = it)?.let { exp ->
                            //injectThenParse(it) //should not call this method, in autoParse mode, it will invoke nextToken to fetch the specified tag, but now it has moved to
                            exp.interpret()
                            list.add(exp)
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

    @SuppressLint("all")
    open fun interpret() {
        if (manual) {
            list.forEach { it.interpret() }
        } else {
            if (DaVinCi.enableDebugLog) Log.d(DaVinCiExpression.sLogTag, "已自动解析")
        }
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
package osp.leobert.android.davinci.expressions

import android.annotation.SuppressLint
import android.util.Log
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.DaVinCiExpression
import java.util.ArrayList

//专门用于解析一串shape
internal class ShapeListExpression constructor(daVinCi: DaVinCi? = null, private val manual: Boolean = false) :
    DaVinCiExpression(daVinCi) {
    private val list: ArrayList<DaVinCiExpression> = ArrayList()

    fun append(exp: DaVinCiExpression) {
        list.add(exp)
    }

    fun setExpression(dState: DState, exp: DaVinCiExpression) {
        list.removeAll {
            it.containsState(dState)
        }
        list.add(exp)
    }

    @SuppressLint("all")
    override fun injectThenParse(daVinCi: DaVinCi?) {
        this.daVinCi = daVinCi
        if (manual) {
            list.forEach { it.injectThenParse(daVinCi) }
            return
        }

        // 在ListExpression解析表达式中,循环解释语句中的每一个单词,直到终结符表达式或者异常情况退出
        daVinCi?.let {
            var i = 0
            while (i < 100) {
                // true,语法错误时有点可怕，先上限100

                if (it.currentToken == null) {
                    // 获取当前节点如果为 null 则表示缺少]表达式
                    println("Error: The Expression Missing ']'! ")
                    break
                } else if (it.equalsWithCommand(END)) {
                    // 解析正常结束
                    it.next()
                    break
                } else if (it.equalsWithCommand(NEXT)) {
                    //进入同级别下一个解析
                    it.next()
                } else if (it.equalsWithCommand(Shape.tag)) {
                    list.add(Shape().apply {
                        this.daVinCi = it
                        //injectThenParse(it) //should not call this method, in autoParse mode, it will invoke nextToken to fetch the specified tag, but now it has moved to
                        interpret()
                    })
                } else {
                    if (DaVinCi.enableDebugLog) Log.e(
                        sLogTag,
                        "The {${Shape.tag}} is Excepted to parse item in list! but got {${it.currentToken}}"
                    )
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
        if (manual) {
            list.forEach { it.interpret() }
        } else {
            if (DaVinCi.enableDebugLog) Log.d(sLogTag, "已自动解析")
        }
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
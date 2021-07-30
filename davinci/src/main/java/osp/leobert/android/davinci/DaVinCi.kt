package osp.leobert.android.davinci

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import java.util.*

@Suppress("unused")
class DaVinCi(text: String?, val view: View) {
    companion object {
        var enableDebugLog = true

        private inline fun <reified R> Any?.takeIfInstance(): R? {
            if (this is R) return this
            return null
        }

    }

    val context: Context = view.context

    val core: DaVinCiCore by lazy {
        DaVinCiCore()
    }

    // 待解析的文本内容
    // 使用空格分隔待解析文本内容
    private val stringTokenizer: StringTokenizer = StringTokenizer(text ?: "")

    // 当前命令
    var currentToken: String? = null

    // 用来存储动态变化信息内容
    private val map: MutableMap<String, Any> = HashMap()

    // TODO: 2021/7/9 add api
//    fun applyBg(exp:DaVinCiExpression) {
//
//    }

    fun applyCsl(exp: DaVinCiExpression.ColorStateList) {
        // TODO: 2021/7/9 add applier and replace

        if (enableDebugLog)
            Log.d(DaVinCiExpression.sLogTag, "daVinCi csl:$exp")

        view.takeIfInstance<TextView>()?.setTextColor(
            exp.run {
                core.clear()
                exp.injectThenParse(this@DaVinCi)
                exp.interpret()

                core.buildTextColor()
            }
        )?.also { core.clear() }
    }

    /*
     * 解析文本
     */
    operator fun next(): String? {
        currentToken = if (stringTokenizer.hasMoreTokens()) {
            stringTokenizer.nextToken()
        } else {
            null
        }
        return currentToken
    }

    /*
     * 判断命令是否正确
     */
    fun equalsWithCommand(command: String?): Boolean {
        return command != null && command == currentToken
    }


    /*
     * 获得节点的内容
     */
    fun getTokenContent(text: String?): String? {
        // 替换map中的动态变化内容后返回 Iterator<String>
        // 替换map中的动态变化内容后返回

        return text?.run {
            var a = this
            for (key in map.keys) {
                val obj = map[key]
                a = a.replace(key.toRegex(), obj.toString())
            }
            a
        }
    }

    fun put(key: String, value: Any) {
        map[key] = value
    }

    fun clear(key: String?) {
        map.remove(key)
    }
}
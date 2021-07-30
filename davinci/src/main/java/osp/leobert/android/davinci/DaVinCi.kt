package osp.leobert.android.davinci

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat
import osp.leobert.android.davinci.Applier.Companion.csl
import osp.leobert.android.davinci.Applier.Companion.viewBackground
import java.util.*

// TODO: 2021/7/30 make it poolable
@Suppress("unused")
class DaVinCi/*(text: String?, val view: View)*/ {
    companion object {
        var enableDebugLog = true

        private inline fun <reified R> Any?.takeIfInstance(): R? {
            if (this is R) return this
            return null
        }
    }


    val context: Context
        get() = requireNotNull(applier).context

    val core: DaVinCiCore by lazy {
        DaVinCiCore()
    }

    var applier: Applier? = null

    // 待解析的文本内容
    // 使用空格分隔待解析文本内容
    private val stringTokenizer: StringTokenizer

    constructor(text: String?, view: View) {
        stringTokenizer = StringTokenizer(text ?: "")
        applier = (view.takeIfInstance<TextView>()?.csl()?.viewBackground())?:view.viewBackground()
    }

    constructor(text: String?, applier: Applier?) {
        stringTokenizer = StringTokenizer(text ?: "")
        this.applier = applier
    }

    // 当前命令
    var currentToken: String? = null

    // 用来存储动态变化信息内容
    private val map: MutableMap<String, Any> = HashMap()

    fun applySld(exp: DaVinCiExpression.StateListDrawable) {
        if (enableDebugLog)
            Log.d(DaVinCiExpression.sLogTag, "daVinCi sld:$exp")

        exp.injectThenParse(this)
        exp.interpret()
        val d = core.build()
        applier?.applyDrawable(d)
    }

    fun applyCsl(exp: DaVinCiExpression.ColorStateList) {
        // TODO: 2021/7/9 add applier and replace

        if (enableDebugLog)
            Log.d(DaVinCiExpression.sLogTag, "daVinCi csl:$exp")

        applier?.applyColorStateList(exp.run {
            core.clear()
            exp.injectThenParse(this@DaVinCi)
            exp.interpret()

            core.buildTextColor()
        })
        core.clear()

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
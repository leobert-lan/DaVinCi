package osp.leobert.android.davinci.expressions

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.DaVinCiExpression
import osp.leobert.android.davinci.State
import osp.leobert.android.reporter.review.TODO
import java.util.*

//region CommandExp 用于解析构建实际子属性
//manual = true 认为是手动创建的，不会进入解析逻辑
@Suppress("WeakerAccess", "unused")
internal open class CommandExpression constructor(daVinCi: DaVinCi? = null, val manual: Boolean = false) : DaVinCiExpression() {

    companion object {
        const val state_separator = "|"

        fun of(daVinCi: DaVinCi? = null, manual: Boolean = false): CommandExpression {
            return CommandExpression(daVinCi, manual).apply {
                //取代了init中的逻辑 ：if (this::class == CommandExpression::class) onParse(daVinCi)
                onParse(daVinCi)
            }
        }
    }

    private var expressions: DaVinCiExpression? = null

    override fun injectThenParse(daVinCi: DaVinCi?) {
        onParse(daVinCi)
    }

    @TODO(desc = "迁移到DaVinCi中，修改为策略模式，添加PT等单位处理")
    protected fun toPx(str: String, context: Context): Int? {
        return when {
            str.endsWith("dp") -> {
                val scale: Float = context.resources.displayMetrics.density
                val dipValue = (str.substring(0, str.length - 2).toIntOrNull() ?: 0)
                (dipValue * scale + 0.5f).toInt()
            }
            str == "w" -> {
                ViewGroup.LayoutParams.WRAP_CONTENT
            }
            str == "m" -> {
                ViewGroup.LayoutParams.MATCH_PARENT
            }
            else -> str.toIntOrNull()
        }

    }

    @TODO(desc = "迁移到DaVinCi中，进一步考虑Theme")
    protected fun parseColor(text: String?): Int? {
        if (text.isNullOrEmpty()) return null
        text.let { s ->
            return when {
                s.startsWith("@") -> {
                    getColor(daVinCi?.context, getTag(daVinCi?.context, s.substring(1)))
                }
                s.startsWith(sResourceColor) -> {
                    getColor(daVinCi?.context, s.substring(sResourceColor.length))
                }
                else -> {
                    getColor(daVinCi?.context, s)
                }
            }
        }
    }

    protected fun parseStates(text: String?): List<State>? {
        if (text.isNullOrEmpty()) return null

        return text.toUpperCase(Locale.ENGLISH).split(state_separator).map {
            State.valueOf(it)
        }

    }

    protected fun parseInt(text: String?, default: Int?): Int? {
        if (text.isNullOrEmpty()) return default
        return text.toIntOrNull() ?: default
    }

    protected fun parseFloat(text: String?, default: Float?): Float? {
        if (text.isNullOrEmpty()) return default
        return text.toFloatOrNull() ?: default
    }

    protected fun getTag(context: Context?, resName: String): String? {
        if (context == null) return null
        val resources = context.resources
        val id = resources.getIdentifier(resName, "id", context.packageName)
        return if (id == 0) {
            null
        } else (daVinCi?.applier?.getTag(id) ?: "").toString()
    }

    protected fun getColor(context: Context?, resName: String?): Int? {
        try {
            if (resName.isNullOrEmpty()) return null

            if (resName.startsWith("#")) {
                return Color.parseColor(resName)
            }
            if (context == null) return null
            val resources = context.resources
            val id = resources.getIdentifier(resName, "color", context.packageName)
            return if (id == 0) {
                if (DaVinCi.enableDebugLog) Log.d(sLogTag, "no color resource named $resName")
                null
            } else ContextCompat.getColor(context, id)
        } catch (e: Exception) {
            if (DaVinCi.enableDebugLog) Log.e(sLogTag, "parse color exception", e)
            return null
        }
    }

    @Throws(Exception::class)
    private fun onParse(daVinCi: DaVinCi?) {
        this.daVinCi = daVinCi
        if (manual) return
        daVinCi?.let {
            expressions = when (it.currentToken) {
                Corners.tag -> Corners.of(it)
                Solid.tag -> Solid.of(it)
                ShapeType.tag -> ShapeType.of(it)
                Stroke.tag -> Stroke.of(it)
                Size.tag -> Size.of(it)
                Padding.tag -> Padding.of(it)
                Gradient.tag -> Gradient.of(it)
                StatedColor.tag -> StatedColor()
                else -> throw Exception("cannot parse ${it.currentToken}")
            }
        }
    }

    protected fun asPrimitiveParse(start: String, daVinCi: DaVinCi?) {
        this.daVinCi = daVinCi
        daVinCi?.let {
            tokenName = it.currentToken
            it.next()
            if (start == tokenName) {
                this.text = it.currentToken
                it.next()
            } else {
                it.next()
            }
        }
    }

    override fun interpret() {
        expressions?.interpret()
    }

    override fun toString(): String {
        return "$expressions"
    }
}
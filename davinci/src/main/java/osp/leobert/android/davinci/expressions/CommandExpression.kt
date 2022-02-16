package osp.leobert.android.davinci.expressions

import android.content.Context
import android.util.Log
import androidx.annotation.CallSuper
import osp.leobert.android.davinci.*
import osp.leobert.android.davinci.lookup.IColorLookup
import osp.leobert.android.davinci.uml.ExpDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.android.reporter.review.TODO
import java.util.*

/**
 * CommandExp 用于解析构建实际子属性
 * manual = true 认为是手动创建的，不会进入字符串解析逻辑
 * 在字符串解析中（构建语法树）的过程中，将作为一个"包装"，从代码角度看，将隔离解析过程，但这个做法值得商榷。
 * 在继承体系中，作为 [Solid] 等非终结符的父类，
 *
 * */
@Suppress("WeakerAccess", "unused")
@NotTerminal
@TODO(desc="分析解析过程，是否有必要套层")
@ExpDiagram
@GenerateClassDiagram
internal open class CommandExpression internal constructor() : DaVinCiExpression() {

    companion object {
        const val state_separator = "|"

        val factory: DPools.Factory<CommandExpression> = object : DPools.Factory<CommandExpression> {
            override fun create(): CommandExpression {
                return CommandExpression()
            }
        }


        fun of(daVinCi: DaVinCi? = null, manual: Boolean = false): CommandExpression {
            return requireNotNull(DPools.commandExpPool.acquire()).apply {
                this.manual = manual
                //取代了init中的逻辑 ：if (this::class == CommandExpression::class) onParse(daVinCi)
                onParse(daVinCi)
            }
        }
    }

    var manual: Boolean = false
        internal set

    private var expressions: DaVinCiExpression? = null

    @CallSuper
    override fun reset() {
        super.reset()
//        expressions?.reset()// just set null is enough, it will be reset if it's released
        expressions = null
    }

    @CallSuper
    override fun onRelease() {
        super.onRelease()
        expressions?.release()
    }

    override fun release() {
        onRelease()
        DPools.commandExpPool.release(this)
    }

    override fun injectThenParse(daVinCi: DaVinCi?) {
        onParse(daVinCi)
    }

    protected fun toPx(str: String, context: Context): Int? {
        return requireNotNull(daVinCi).lookupDimension(str, context)
    }

    @TODO(desc = "迁移到DaVinCi中，进一步考虑Theme")
    protected fun parseColor(text: String?): Int? {
        if (text.isNullOrEmpty()) return null
        //todo tag的解析方式耦合较高，目前尚不容易迁移，可以认为这是分两步走的：解析怎么定义的资源+解析（获取）资源对应的ColorInt
        //先迁移第二步 getColor
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

    private fun getColor(context: Context?, resName: String?): Int? {
        val lookup: IColorLookup = daVinCi ?: DaVinCiConfig.apply {
            if (DaVinCi.enableDebugLog) Log.e(sLogTag, "daVinCi is null.use default config to parse $resName")
        }

        return lookup.lookupColor(resName,context)
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
                StatedColor.tag -> StatedColor.of()
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
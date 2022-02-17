package osp.leobert.android.davinci.expressions

import android.content.Context
import android.util.Log
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.DaVinCiConfig
import osp.leobert.android.davinci.DaVinCiExpression
import osp.leobert.android.davinci.State
import osp.leobert.android.davinci.lookup.IColorLookup
import osp.leobert.android.davinci.uml.ExpDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.android.reporter.review.TODO
import java.util.*

/**
 * CommandExp 用于解析构建实际子属性
 * manual = true 认为是手动创建的（指，手动创建了树，而不是基于语法式自动解析构建树），不会进入字符串解析逻辑
 * 在字符串解析中（构建语法树）的过程中，将作为一个"包装"，从代码角度看，将隔离解析过程，但这个做法值得商榷。
 * 在继承体系中，作为 [Solid] 等非终结符的父类.
 *
 * 2022-02-17 目前，应当将其修改为抽象类更加合理，仅保留部分内部简便API
 *
 * */
@Suppress("WeakerAccess", "unused")
@NotTerminal
@ExpDiagram
@GenerateClassDiagram
internal abstract class CommandExpression internal constructor() : DaVinCiExpression() {

    companion object {
        const val state_separator = "|"

        fun of(daVinCi: DaVinCi? = null): CommandExpression {

            requireNotNull(daVinCi)

            return daVinCi.run {
                when (this.currentToken) {
                    Corners.tag -> Corners.of(this)
                    Solid.tag -> Solid.of(this)
                    ShapeType.tag -> ShapeType.of(this)
                    Stroke.tag -> Stroke.of(this)
                    Size.tag -> Size.of(this)
                    Padding.tag -> Padding.of(this)
                    Gradient.tag -> Gradient.of(this)
                    StatedColor.tag -> StatedColor.of()
                    else -> throw Exception("cannot parse ${this.currentToken}")
                }
            }
        }
    }

    /**
     * true if this Expression-Tree is build by manual.
     * false if it's parsed and build from the string-serialized-expression
     * */
    var manual: Boolean = false
        internal set


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

        return lookup.lookupColor(resName, context)
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
}
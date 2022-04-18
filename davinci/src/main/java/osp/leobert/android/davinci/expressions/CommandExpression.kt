package osp.leobert.android.davinci.expressions

import android.content.Context
import android.util.Log
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.DaVinCiConfig
import osp.leobert.android.davinci.DaVinCiExpression
import osp.leobert.android.davinci.State
import osp.leobert.android.davinci.lookup.IResourceLookup
import osp.leobert.android.davinci.res.DaVinCiResource
import osp.leobert.android.davinci.uml.ExpDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.android.reporter.review.TODO

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

//        @Deprecated("理论上不应该被使用，厘清后移除")
//        fun of(daVinCi: DaVinCi? = null): CommandExpression {
//
//            requireNotNull(daVinCi)
//
//            return daVinCi.run {
//                when (this.currentToken) {
//                    Corners.tag -> Corners.of(this)
//                    Solid.tag -> Solid.of(this)
//                    ShapeType.tag -> ShapeType.of(this)
//                    Stroke.tag -> Stroke.of(this)
//                    Size.tag -> Size.of(this)
//                    Padding.tag -> Padding.of(this)
//                    Gradient.tag -> Gradient.of(this)
//                    StatedColor.tag -> StatedColor.of()
//                    else -> throw Exception("cannot parse ${this.currentToken}")
//                }
//            }
//        }
    }

    /**
     * true if this Expression-Tree is build by manual.
     * false if it's parsed and build from the string-serialized-expression
     * */
    var manual: Boolean = false
        internal set


    protected fun toPx(str: String, context: Context): Int? {
        return requireNotNull(daVinCi).lookupDimension(str, context, daVinCi)
    }

    protected fun lookupColorInt(text: String?): Int? {

        if (text.isNullOrEmpty()) return null

        val lookup: IResourceLookup = daVinCi ?: DaVinCiConfig.apply {
            if (DaVinCi.enableDebugLog) Log.e(sLogTag, "daVinCi is null.use default config to lookup colorIntRes")
        }

        return kotlin.runCatching {
            DaVinCiResource.ColorIntRes::class.java.cast(lookup.lookupResource(DaVinCiResource.ColorIntRes::class.java, text, daVinCi?.context, daVinCi))
        }.getOrNull()?.colorInt
    }

    @TODO("valueOf 不能纠错，进一步考虑效率问题")
    protected fun parseStates(text: String?): List<State>? {
        if (text.isNullOrEmpty()) return null

        return text.uppercase().split(state_separator).map {
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

//    protected fun getTag(context: Context?, resName: String): String? {
//        return daVinCi?.lookupTag(resName, context, daVinCi)
//    }

//    private fun getColor(context: Context?, colorResNameOrColorStr: String?): Int? {
//        val lookup: IColorLookup = daVinCi ?: DaVinCiConfig.apply {
//            if (DaVinCi.enableDebugLog) Log.e(sLogTag, "daVinCi is null.use default config to parse $colorResNameOrColorStr")
//        }
//
//        return lookup.lookupColor(colorResNameOrColorStr, context, daVinCi)
//    }

    protected fun asPrimitiveParse(start: String, daVinCi: DaVinCi?) {
        this.daVinCi = daVinCi
        daVinCi?.let {
            tokenName = it.currentToken
            //要求已经对齐到start tag
            if (start == tokenName) {
                //获取内容段
                this.text = it.next()//it.currentToken
                //对齐到下一位 理论上为NEXT or END
                it.next()
            } else {
                //TODO 应该抛出错误
                if (DaVinCi.enableDebugLog) {
                    Log.e(
                        sLogTag,
                        "The {$start} is Excepted For Start When Not Manual! but got {${it.currentToken}}",
                        Throwable()
                    )
                }
                null
            }
        }
    }
}
package osp.leobert.android.davinci

import android.annotation.SuppressLint
import android.util.Log
import androidx.core.view.ViewCompat

/**
 * <p><b>Package:</b> osp.leobert.android.davinci </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> StyleRegistry </p>
 * Created by leobert on 2021/5/31.
 */
object StyleRegistry {

    private val styles: MutableMap<String, Style> = mutableMapOf()

    private val lazyFactoryOfStyles: MutableMap<String, Style.Factory> = mutableMapOf()

    fun allStyleNames(): List<String> {
        return styles.keys.toCollection(linkedSetOf()).apply {
            addAll(lazyFactoryOfStyles.keys)
        }.sorted()
    }

    fun register(style: Style) {
        if (DaVinCi.enableDebugLog)
            Log.d(DaVinCiExpression.sLogTag, "StyleRegistry register style:${style}")
        if (styles.containsKey(style.name)) {
            val equals = style == styles[style.name]

            if (DaVinCi.enableDebugLog) {

                if (equals) {
                    Log.d(
                        DaVinCiExpression.sLogTag,
                        "StyleRegistry already registered style:${style}"
                    )
                } else {
                    Log.e(
                        DaVinCiExpression.sLogTag,
                        "StyleRegistry already registered style name ${style.name} -> ${styles[style.name]},now changed to $style"
                    )
                }
            }
            if (equals) return
        }

        styles[style.name] = style
    }

    fun registerFactory(factory: Style.Factory) {
        if (DaVinCi.enableDebugLog)
            Log.d(DaVinCiExpression.sLogTag, "StyleRegistry register style-factory:${factory}")
        if (styles.containsKey(factory.styleName)) {
            val equals = factory == lazyFactoryOfStyles[factory.styleName]

            if (DaVinCi.enableDebugLog) {

                if (equals) {
                    Log.d(
                        DaVinCiExpression.sLogTag,
                        "StyleRegistry already registered style-factory:${factory}"
                    )
                } else {
                    Log.e(
                        DaVinCiExpression.sLogTag,
                        "StyleRegistry already registered style-factory name ${factory.styleName} -> ${lazyFactoryOfStyles[factory.styleName]},now changed to $factory"
                    )
                }
            }
            if (equals) return
        }

        lazyFactoryOfStyles[factory.styleName] = factory
    }


    fun find(styleName: String): Style? {
        if (styles.containsKey(styleName))
            return styles[styleName]

        //find if in lazy factory
        // TODO: 2021/5/31 consider remove?

        return lazyFactoryOfStyles[styleName]?.register2StyleRegistry(this).apply {
            if (this == null && DaVinCi.enableDebugLog) {
                Log.e(
                    DaVinCiExpression.sLogTag,
                    "StyleRegistry could not find style with name: $styleName"
                )
            }
        }
    }

    open class Style(val name: String):Statable<Style> {
        private val statedExpressions: MutableMap<State, DaVinCiExpression> = mutableMapOf()

        //考虑到两者的特性，将其分开
        private var statedColorExp: DaVinCiExpression.ColorStateList? = null

        @Deprecated("不符合一般认知")
        fun register(state: State, expression: DaVinCiExpression): Style = this.apply {
            statedExpressions[state] = expression
        }

        fun registerCsl(exp: DaVinCiExpression.ColorStateList): Style {
            statedColorExp = exp
            return this
        }

        @SuppressLint("all")
        fun applyTo(daVinCi: DaVinCi) {
            val daVinCiLoop = DaVinCi(null, daVinCi.view)
            statedExpressions.entries.forEach {
                it.key.adapt(daVinCi, daVinCiLoop, it.value)
            }
            ViewCompat.setBackground(daVinCi.view, daVinCi.core.build())
            daVinCi.core.clear()

            statedColorExp?.let {
                daVinCi.applyCsl(it)
            }

        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Style

            if (name != other.name) return false
            if (statedExpressions != other.statedExpressions) return false
            if (statedColorExp != other.statedColorExp) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + statedExpressions.hashCode()
            result = 31 * result + (statedColorExp?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "Style(name='$name', statedExpressions=$statedExpressions, statedColorExp=$statedColorExp)"
        }


        abstract class Factory {

            abstract val styleName: String

            internal fun register2StyleRegistry(register: StyleRegistry): Style {
                val style = Style(styleName)
                apply(style)
                register.register(style)
                return style
            }

            abstract fun apply(style: Style)
        }

        override fun states(vararg states: State): Style {
            TODO("Not yet implemented")
        }

        override fun states(vararg states: String): Style {
            TODO("Not yet implemented")
        }
    }

}
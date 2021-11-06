package osp.leobert.android.davinci

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import osp.leobert.android.davinci.Applier.Companion.viewBackground
import osp.leobert.android.davinci.expressions.*
import osp.leobert.android.davinci.expressions.Corners
import osp.leobert.android.davinci.expressions.DState
import osp.leobert.android.davinci.expressions.Padding
import osp.leobert.android.davinci.expressions.Stroke
import java.util.*

@Suppress("WeakerAccess", "unused")
sealed class DaVinCiExpression(var daVinCi: DaVinCi? = null) {

    protected fun <T> log(str: String, any: T?): T? {
        if (DaVinCi.enableDebugLog) Log.d(sLogTag, "${javaClass.simpleName}:$str")
        return any
    }

    // 节点名称
    protected var tokenName: String? = null

    // 文本内容
    protected var text: String? = null
        set(value) {
            field = value
            onTextContentSet(value)
        }

    protected open fun onTextContentSet(text: String?) {

    }

    //实际属性是否需要从text解析，手动创建并给了专有属性的，设为false，就不会被覆盖了
    protected var parseFromText = true

    //一定会植入，手动创建的不解析
    abstract fun injectThenParse(daVinCi: DaVinCi?)

    /*
     * 执行方法
     */
    abstract fun interpret()

    open fun startTag(): String = ""


    internal open fun containsState(dState: DState): Boolean {
        return false
    }

    companion object {

        @JvmStatic
        fun stateListDrawable(): StateListDrawable = StateListDrawable(manual = true)

        @JvmStatic
        fun shape(): Shape = Shape(manual = true)

        @JvmStatic
        fun stateColor(): ColorStateList = ColorStateList(manual = true)

        @JvmStatic
        fun shapeAndStateColor(
            shape: Shape,
            stateColor: ColorStateList,
        ): DaVinCiExpression = ListExpression(manual = true).apply {
            append(shape)
            append(stateColor)
        }

        @JvmStatic
        fun stateArray(vararg state: State) = state


        const val sLogTag = "DaVinCi"

        const val END = "]"

        const val NEXT = "];"

        const val sResourceColor = "rc/"
    }

    //region CommandExp 用于解析构建实际子属性
    //manual = true 认为是手动创建的，不会进入解析逻辑
    @Suppress("WeakerAccess", "unused")
    open class CommandExpression(daVinCi: DaVinCi? = null, val manual: Boolean = false) : DaVinCiExpression(daVinCi) {

        companion object {
            const val state_separator = "|"
        }

        private var expressions: DaVinCiExpression? = null

        init {
            //因为是嵌套层，且作为父类了，避免递归
            if (this::class == CommandExpression::class)
                onParse(daVinCi)
        }

        override fun injectThenParse(daVinCi: DaVinCi?) {
            onParse(daVinCi)
        }

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
            text.let {
                return text.toIntOrNull() ?: default
            }
        }

        protected fun parseFloat(text: String?, default: Float?): Float? {
            if (text.isNullOrEmpty()) return default
            text.let {
                return text.toFloatOrNull() ?: default
            }
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
                    Corners.tag -> Corners(it)
                    Solid.tag -> Solid(it)
                    ShapeType.tag -> ShapeType(it)
                    Stroke.tag -> Stroke(it)
                    Size.tag -> Size(it)
                    Padding.tag -> Padding(it)
                    Gradient.tag -> Gradient(it)
                    StatedColor.tag -> StatedColor(it)
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
    //endregion

    //region ListExp 同级别多条目解析
    internal class ListExpression(daVinCi: DaVinCi? = null, private val manual: Boolean = false) : DaVinCiExpression(daVinCi) {
        private val list: ArrayList<DaVinCiExpression> = ArrayList()

        /**
         * in rule: only one state expression is permitted
         * */
        var dState: DState? = null

        fun append(exp: DaVinCiExpression) {
            list.add(exp)
        }

        fun appendState(vararg states: State) {
            val dState = dState ?: DState(daVinCi)
            dState.appendStates(states)
            this.dState = dState
        }

        fun appendState1(states: Array<out State>) {
            val dState = dState ?: DState(daVinCi)
            dState.appendStates(states)
            this.dState = dState
        }

        @SuppressLint("all")
        override fun injectThenParse(daVinCi: DaVinCi?) {
            this.daVinCi = daVinCi
            if (manual) {
                list.forEach { it.injectThenParse(daVinCi) }
                dState?.injectThenParse(daVinCi)
                return
            }

            // 在ListExpression解析表达式中,循环解释语句中的每一个单词,直到终结符表达式或者异常情况退出
            daVinCi?.let {
                var i = 0
                while (i < 100) { // true,语法错误时有点可怕，先上限100
                    if (it.currentToken == null) { // 获取当前节点如果为 null 则表示缺少]表达式
                        println("Error: The Expression Missing ']'! ")
                        break
                    } else if (it.equalsWithCommand(END)) {
                        it.next()
                        // 解析正常结束
                        break
                    } else if (it.equalsWithCommand(NEXT)) {
                        //进入同级别下一个解析
                        it.next()
                    } else if (it.equalsWithCommand(DState.tag)) {
                        if (dState != null) {
                            if (DaVinCi.enableDebugLog) Log.d(
                                sLogTag,
                                "only one state expression is permitted in one group, will override old one $dState"
                            )
                        }
                        dState = DState(it)

                    } else { // 建立Command 表达式
                        try {
                            val expressions: DaVinCiExpression = CommandExpression(it)
                            list.add(expressions)
                        } catch (e: Exception) {
                            if (DaVinCi.enableDebugLog) Log.e(sLogTag, "语法解析有误", e)
                            break
                        }
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
            list.forEach { it.interpret() }
            dState?.interpret()
        }

        override fun toString(): String {
            val b = StringBuilder()

            dState?.let { b.append(it.toString()) }

            val iMax: Int = list.size - 1
            if (iMax == -1) return b.toString()
            if (dState != null)
                b.append("; ")
            var i = 0
            while (true) {
                b.append(list[i].toString())
                if (i == iMax) return b.toString()
                b.append("; ")
                i++
            }
        }

        override fun containsState(dState: DState): Boolean {
            return (this.dState?.statesHash ?: 0) == dState.statesHash
        }
    }
    //endregion

    //region StateListDrawable
    class StateListDrawable internal constructor(val manual: Boolean = false) : DaVinCiExpression(null) {

        private var expressions: ShapeListExpression? = null
        override fun startTag(): String = tag

        internal fun shapeListExpression(): ShapeListExpression = expressions ?: ShapeListExpression(daVinCi, manual).apply {
            expressions = this
        }

        fun shape(exp: Shape): SldSyntactic {
            return SldSyntactic.of(this, exp)
        }

        companion object {
            const val tag = "sld:["
        }

        override fun injectThenParse(daVinCi: DaVinCi?) {
            this.daVinCi = daVinCi
            if (manual) return
            daVinCi?.next()
        }

        override fun interpret() {
            daVinCi?.let {
                if (manual) {
                    this.expressions = shapeListExpression().apply {
                        this.injectThenParse(it)
                        this.interpret()
                    }
                } else if (!it.equalsWithCommand(tag)) {
                    if (DaVinCi.enableDebugLog) Log.e(
                        sLogTag,
                        "The {$tag} is Excepted For Start When Not Manual! but got {${it.currentToken}}"
                    )
                } else {
                    //解析型
                    it.next()
                    this.expressions = shapeListExpression().apply {
                        this.injectThenParse(it)
                        this.interpret()
                    }
                }
            }
        }

        override fun toString(): String {
            return "$tag $expressions $END"
        }

        fun applyInto(view: View) {
            val daVinCi = DaVinCi.of(null, view.viewBackground())
            daVinCi.applySld(this)
            daVinCi.release()
        }
    }
    //endregion

    //专门用于解析一串shape
    internal class ShapeListExpression(daVinCi: DaVinCi? = null, private val manual: Boolean = false) :
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


    //region Shape
    class Shape internal constructor(private val manual: Boolean = false) : DaVinCiExpression(null) {

        private var expressions: ListExpression? = null
        override fun startTag(): String = tag

        internal fun listExpression(): ListExpression {
            return expressions ?: ListExpression(daVinCi, manual).apply {
                expressions = this
            }
        }

        //region apis
        @Deprecated("为了迁移旧版本正确性的保留API，不建议常规使用", replaceWith = ReplaceWith("ShapeListExpression"))
        fun appendState(vararg states: State): Shape {
            val exp: ListExpression = listExpression()
            exp.appendState1(states)
            return this
        }

        fun type(str: String): Shape {
            ShapeType(manual = true).apply {
                this.text = str
                listExpression().append(this)
            }
            return this
        }

        fun rectAngle(): Shape {
            ShapeType(manual = true).apply {
                this.text = ShapeType.Rectangle
                parseFromText = false
                listExpression().append(this)
            }
            return this
        }

        fun oval(): Shape {
            ShapeType(manual = true).apply {
                this.text = ShapeType.Oval
                parseFromText = false
                listExpression().append(this)
            }
            return this
        }

        fun ring(): Shape {
            ShapeType(manual = true).apply {
                this.text = ShapeType.Ring
                parseFromText = false
                listExpression().append(this)
            }
            return this
        }

        fun line(): Shape {
            ShapeType(manual = true).apply {
                this.text = ShapeType.Line
                parseFromText = false
                listExpression().append(this)
            }
            return this
        }


        fun corner(@Px r: Int): Shape {
            Corners(manual = true).apply {
                this.conners = arrayListOf(r, r, r, r)
                parseFromText = false
                listExpression().append(this)
            }
            return this
        }

        fun corner(str: String): Shape {
            Corners(manual = true).apply {
                this.text = str
                parseFromText = true
                listExpression().append(this)
            }
            return this
        }

        fun corners(@Px lt: Int, @Px rt: Int, @Px rb: Int, @Px lb: Int): Shape {
            Corners(manual = true).apply {
                this.conners = arrayListOf(lt, rt, rb, lb)
                parseFromText = false
                listExpression().append(this)
            }
            return this
        }

        //e.g. "@tagid","#e5332c"
        fun solid(str: String): Shape {
            Solid(manual = true).apply {
                text = str
                listExpression().append(this)
            }
            return this
        }

        fun solid(@ColorInt color: Int): Shape {
            Solid(manual = true).apply {
                text = "#" + String.format("%8x", color)
                this.colorInt = color
                parseFromText = false
                listExpression().append(this)
            }
            return this
        }

        //e.g. color "@tagid","#e5332c"
        //width "4"->4px "3dp"->3dp
        fun stroke(width: String, color: String): Shape {
            Stroke(manual = true).apply {
                text = Stroke.prop_width + width + ";" + Stroke.prop_color + color
                listExpression().append(this)
            }
            return this
        }

        fun stroke(width: String, color: String, dashGap: String, dashWidth: String): Shape {
            Stroke(manual = true).apply {
                text =
                    "${Stroke.prop_width}$width;${Stroke.prop_color}$color;${Stroke.prop_dash_gap}$dashGap;${Stroke.prop_dash_width}$dashWidth"
                listExpression().append(this)
            }
            return this
        }

        fun stroke(@Px width: Int, @ColorInt colorInt: Int): Shape {
            Stroke(manual = true).apply {
                color = colorInt
                this.width = width
                parseFromText = false
                text = Stroke.prop_width + width + ";" + Stroke.prop_color + "#" + String.format(
                    "%8x",
                    colorInt
                )
                listExpression().append(this)
            }
            return this
        }

        fun gradient(
            type: String = Gradient.TYPE_LINEAR, @ColorInt startColor: Int, @ColorInt endColor: Int,
            angle: Int = 0,
        ): Shape {
            return gradient(type, startColor, null, endColor, 0f, 0f, angle)
        }

        //        @JvmOverloads 不适合使用kotlin的重载
        fun gradient(
            type: String = Gradient.TYPE_LINEAR, @ColorInt startColor: Int,
            @ColorInt centerColor: Int?, @ColorInt endColor: Int,
            centerX: Float,
            centerY: Float,
            angle: Int = 0,
        ): Shape {
            Gradient(manual = true).apply {
                this.type = type
                this.startColor = startColor
                this.centerColor = centerColor
                this.endColor = endColor

                this.centerX = centerX
                this.centerY = centerY
                this.angle = angle

                parseFromText = false
                //犯懒了，不想手拼了
                listExpression().append(this)
            }
            return this
        }

        fun gradient(startColor: String, endColor: String, angle: Int): Shape {
            return gradient(Gradient.TYPE_LINEAR, startColor, null, endColor, 0f, 0f, angle)
        }

        fun gradient(
            type: String = Gradient.TYPE_LINEAR,
            startColor: String,
            endColor: String,
            angle: Int = 0,
        ): Shape {
            return gradient(type, startColor, null, endColor, 0f, 0f, angle)
        }

        fun gradient(
            type: String = Gradient.TYPE_LINEAR, startColor: String,
            centerColor: String?, endColor: String, centerX: Float, centerY: Float, angle: Int,
        ): Shape {
            Gradient(manual = true).apply {
                text = type.run { "${Gradient.prop_type}$this" } +
                        startColor.run { ";${Gradient.prop_start_color}$this" } +
                        centerColor.run { ";${Gradient.prop_center_color}$this" } +
                        endColor.run { ";${Gradient.prop_end_color}$this" } +
                        centerX.run { ";${Gradient.prop_center_x}$this" } +
                        centerY.run { ";${Gradient.prop_center_y}$this" } +
                        angle.run { ";${Gradient.prop_angle}$this" }
                listExpression().append(this)
            }
            return this
        }
        //endregion


        companion object {
            const val tag = "shape:["
        }

        override fun injectThenParse(daVinCi: DaVinCi?) {
            this.daVinCi = daVinCi
            if (manual) return
            daVinCi?.next()
        }

        override fun interpret() {
            daVinCi?.let {
                if (manual) {
                    this.expressions = listExpression().apply {
                        this.injectThenParse(it)
                        this.interpret()
                    }
                } else if (!it.equalsWithCommand(tag)) {
                    if (DaVinCi.enableDebugLog) Log.e(
                        sLogTag,
                        "The {$tag} is Excepted For Start When Not Manual! but got {${it.currentToken}}"
                    )
                } else {
                    //解析型
                    it.next()
                    this.expressions = listExpression().apply {
                        this.injectThenParse(it)
                        this.interpret()
                    }
                }
            }
        }

        override fun toString(): String {
            return "$tag $expressions $END"
        }


    }
    //endregion


    //region Color State List
    class ColorStateList internal constructor(private val manual: Boolean = false) : DaVinCiExpression(null) {

        private var expressions: ListExpression? = null
        override fun startTag(): String = tag


        internal fun exps(): ListExpression = expressions ?: ListExpression(daVinCi, manual).apply {
            expressions = this
        }


        companion object {
            const val tag = "csl:["
        }

        fun color(color: String): Statable<ColorStateList> {
            return CslSyntactic.of(this, color)
        }


        fun color(@ColorInt color: Int): Statable<ColorStateList> {
            return CslSyntactic.of(this, color)
        }


        @Deprecated("表意不准确", ReplaceWith("color(color).states(state...)"))
        fun apply(state: State, color: String): ColorStateList {
            return color(color).states(state)
        }

        @Deprecated("表意不准确", ReplaceWith("color(color).states(state...)"))
        fun apply(state: String, color: String): ColorStateList {
            return color(color).states(state)
        }

        @Deprecated("表意不准确", ReplaceWith("color(color).states(state...)"))
        fun apply(state: State, @ColorInt color: Int): ColorStateList {
            return color(color).states(state)
        }


        override fun injectThenParse(daVinCi: DaVinCi?) {
            this.daVinCi = daVinCi
            if (manual) return
            daVinCi?.next()
        }

        override fun interpret() {
            daVinCi?.let {
                if (manual) {
                    this.expressions = exps().apply {
                        this.injectThenParse(it)
                        this.interpret()
                    }
                } else if (!it.equalsWithCommand(tag)) {
                    if (DaVinCi.enableDebugLog) Log.e(
                        sLogTag,
                        "The {$tag} is Excepted For Start When Not Manual! but got {${it.currentToken}}"
                    )
                } else {
                    //解析型
                    it.next()
                    this.expressions = exps().apply {
                        this.injectThenParse(it)
                        this.interpret()
                    }
                }
            }
        }

        override fun toString(): String {
            return "$tag $expressions $END"
        }
    }
    //endregion


    ///////////////////////////////////////////////////////////////////////////
    // make syntactic more easy and smooth
    ///////////////////////////////////////////////////////////////////////////

    //region make syntactic more easy and smooth

    class SldSyntactic : Statable<StateListDrawable> {
        private var exp: Shape? = null
        private var host: StateListDrawable? = null

        companion object {
            val factory: DPools.Factory<SldSyntactic> = object : DPools.Factory<SldSyntactic> {
                override fun create(): SldSyntactic {
                    return SldSyntactic()
                }
            }

            val resetter: DPools.Resetter<SldSyntactic> = object : DPools.Resetter<SldSyntactic> {
                override fun reset(target: SldSyntactic) {
                    target.exp = null
                    target.host = null
                }
            }

            fun of(host: StateListDrawable, exp: Shape): SldSyntactic {
                return requireNotNull(DPools.sldSyntacticPool.acquire()).apply {
                    this.exp = exp
                    this.host = host
                }
            }
        }

        override fun states(vararg states: State): StateListDrawable {

            val host = requireNotNull(host)
            val exp = requireNotNull(exp)
            val dState = DState(manual = true).apply {
                parseFromText = false
                this.appendStates(states)
            }

            exp.listExpression().dState = dState

            host.shapeListExpression().setExpression(dState, exp)
//            host.shapeListExpression().append(exp)

            DPools.sldSyntacticPool.release(this)
            return host
        }

        override fun states(vararg states: String): StateListDrawable {
            val host = requireNotNull(host)
            val exp = requireNotNull(exp)
            val dState = DState(manual = true).apply {
                parseFromText = true
                this.text = states.joinToString(CommandExpression.state_separator)
            }
            exp.listExpression().dState = dState

            host.shapeListExpression().setExpression(dState, exp)
//            host.shapeListExpression().append(exp)

            DPools.sldSyntacticPool.release(this)
            return host
        }
    }

    class CslSyntactic : Statable<ColorStateList> {

        companion object {
            val factory: DPools.Factory<CslSyntactic> = object : DPools.Factory<CslSyntactic> {
                override fun create(): CslSyntactic {
                    return CslSyntactic()
                }
            }

            val resetter: DPools.Resetter<CslSyntactic> = object : DPools.Resetter<CslSyntactic> {
                override fun reset(target: CslSyntactic) {
                    target.host = null
                    target.color = null
                    target.colorInt = null
                    target.colorTag = 0
                }
            }

            fun of(host: ColorStateList, color: String): CslSyntactic {
                return requireNotNull(DPools.cslSyntacticPool.acquire()).apply {
                    this.color = color
                    this.host = host
                }
            }

            fun of(host: ColorStateList, @ColorInt color: Int): CslSyntactic {
                return requireNotNull(DPools.cslSyntacticPool.acquire()).apply {
                    this.colorInt = color
                    this.host = host
                }
            }
        }

        var host: ColorStateList? = null

        var color: String? = null
            set(value) {
                field = value
                if (value != null)
                    colorTag = 1
            }

        var colorInt: Int? = null
            set(value) {
                field = value
                if (value != null)
                    colorTag = 2
            }

        private var colorTag = 0

        private fun strColor(): String? {
            return when (colorTag) {
                1 -> color
                2 -> "#" + String.format("%8x", colorInt)
                else -> null
            }
        }

        override fun states(vararg states: State): ColorStateList {
            val host = requireNotNull(host)
            StatedColor(
                manual = true
            ).apply {
                if (this@CslSyntactic.colorTag == 2)
                    this.colorInt = this@CslSyntactic.colorInt
                this.states.addAll(states)
                parseFromText = (this@CslSyntactic.colorTag == 1)
                text =
                    "${StatedColor.prop_state}${states.joinToString(StatedColor.separator)};${StatedColor.prop_color}${this@CslSyntactic.strColor()}"

                host.exps().append(this)
            }
            DPools.cslSyntacticPool.release(this)
            return host

        }

        override fun states(vararg states: String): ColorStateList {

            val host = requireNotNull(host)
            StatedColor(
                manual = true
            ).apply {
                if (this@CslSyntactic.colorTag == 2)
                    this.colorInt = this@CslSyntactic.colorInt

                text =
                    "${StatedColor.prop_state}${states.joinToString(StatedColor.separator)};${StatedColor.prop_color}${this@CslSyntactic.strColor()}"

                host.exps().append(this)
            }
            DPools.cslSyntacticPool.release(this)
            return host
        }
    }
    //endregion

}
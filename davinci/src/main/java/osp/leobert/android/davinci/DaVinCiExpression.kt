package osp.leobert.android.davinci

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import osp.leobert.android.davinci.Applier.Companion.viewBackground
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


    protected open fun containsState(dState: DState): Boolean {
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
            injectThenParse(daVinCi)
            interpret()
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

    //region Corners
    //        corners-->
    //        <!--android:radius="integer"-->
    //        <!--android:topLeftRadius="integer"-->
    //        <!--android:topRightRadius="integer"-->
    //        <!--android:bottomLeftRadius="integer"-->
    //        <!--android:bottomRightRadius="integer" />-->
    // shape:[ corners:[ 4 ] solid:[ #353538 ] ]
    class Corners(daVinCi: DaVinCi? = null, manual: Boolean = false) : CommandExpression(daVinCi, manual) {

        var conners: List<Int>? = null

        companion object {
            const val tag = "corners:["
        }

        init {
            injectThenParse(daVinCi)
        }

        override fun injectThenParse(daVinCi: DaVinCi?) {
            this.daVinCi = daVinCi
            if (manual) {
                if (parseFromText) {
                    conners = null
                    if (daVinCi != null)
                        parseRadius(daVinCi)
                    else
                        if (DaVinCi.enableDebugLog) Log.v(
                            sLogTag,
                            "daVinCi is null cannot parse corner,in manual,parse from text,maybe on init"
                        )
                }
                return
            }
            asPrimitiveParse(tag, daVinCi)
            conners = null
            if (daVinCi != null)
                parseRadius(daVinCi)
            else
                if (DaVinCi.enableDebugLog) Log.e(
                    sLogTag,
                    "daVinCi is null cannot parse corner,from text:$parseFromText"
                )
        }

        override fun interpret() {
            if (tag == tokenName || manual) {
                daVinCi?.let {
                    parseRadius(it)?.let { r ->
                        it.core.setCornersRadius(
                            r[3].toFloat(), //左下
                            r[2].toFloat(), //右下
                            r[0].toFloat(),//左上
                            r[1].toFloat() // 右上
                        )
                    }
                }
            }
        }

        private fun parseRadius(daVinCi: DaVinCi): List<Int>? {
            if (conners != null) return conners

            text?.let {
                val tmp = it.split(",").toList()
                when (tmp.size) {
                    1 -> {
                        val r = toPx(tmp[0], daVinCi.context) ?: 0
                        conners = arrayListOf(r, r, r, r)
                    }
                    4 -> {
                        conners = tmp.map { e -> toPx(e, daVinCi.context) ?: 0 }
                    }
                    else -> {
                        if (DaVinCi.enableDebugLog) Log.e(
                            sLogTag,
                            "error $text for corners, only support single or four element separated by ${","},e.g.: ${"1"},${"2dp"},${"1,2dp,3,4"}"
                        )
                    }
                }
            }

            return conners
        }


        override fun toString(): String {
            return "$tag $conners $END"
        }
    }
    //endregion

    //region Corners
    //        corners-->
    //        <!--android:radius="integer"-->
    //        <!--android:topLeftRadius="integer"-->
    //        <!--android:topRightRadius="integer"-->
    //        <!--android:bottomLeftRadius="integer"-->
    //        <!--android:bottomRightRadius="integer" />-->
    // shape:[ st:[reactangle] ]

    // Rectangle(0), Oval(1), Line(2), Ring(3);

    class ShapeType(daVinCi: DaVinCi? = null, manual: Boolean = false) : CommandExpression(daVinCi, manual) {

        companion object {
            const val tag = "st:["

            const val Rectangle = "Rectangle"
            const val Oval = "Oval"
            const val Line = "Line"
            const val Ring = "Ring"
        }

        init {
            injectThenParse(daVinCi)
        }

        override fun injectThenParse(daVinCi: DaVinCi?) {
            this.daVinCi = daVinCi
            if (manual) return
            asPrimitiveParse(tag, daVinCi)
        }

        override fun interpret() {
            if (tag == tokenName || manual) {
                daVinCi?.let {
                    when {
                        Oval.equals(text, true) -> {
                            it.core.setShape(DaVinCiCore.Shape.Oval)
                        }
                        Line.equals(text, true) -> {
                            it.core.setShape(DaVinCiCore.Shape.Line)
                        }
                        Ring.equals(text, true) -> {
                            it.core.setShape(DaVinCiCore.Shape.Ring)
                        }
                        else -> {
                            it.core.setShape(DaVinCiCore.Shape.Rectangle)
                        }
                    }
                }
            }
        }

        override fun toString(): String {
            return "$tag $text $END"
        }
    }
    //endregion

    //region Solid
    class Solid(daVinCi: DaVinCi? = null, manual: Boolean = false) : CommandExpression(daVinCi, manual) {
        @ColorInt
        internal var colorInt: Int? = null //这是解析出来的，不要乱赋值

        companion object {
            const val tag = "solid:["
        }

        init {
            injectThenParse(daVinCi)
        }

        override fun injectThenParse(daVinCi: DaVinCi?) {
            this.daVinCi = daVinCi

            if (manual) {
                if (parseFromText)
                    colorInt = parseColor(text)
                return
            }
            colorInt = null
            asPrimitiveParse(tag, daVinCi)
            colorInt = parseColor(text)

        }

        override fun interpret() {
            if (tag == tokenName || manual) {
                daVinCi?.let {
                    colorInt?.let { color ->
                        it.core.setSolidColor(color)
                    }
                }
            }
        }

        override fun toString(): String {
            return "$tag ${if (parseFromText) text else colorInt?.run { text }} $END"
        }
    }
    //endregion

    //region Gradient
    //    <gradient
    //    android:type=["linear" | "radial" | "sweep"]    //共有3中渐变类型，线性渐变（默认）/放射渐变/扫描式渐变
    //    android:angle="integer"     //渐变角度，必须为45的倍数，0为从左到右，90为从上到下
    //    android:centerX="float"     //渐变中心X的相当位置，范围为0～1
    //    android:centerY="float"     //渐变中心Y的相当位置，范围为0～1
    //    android:startColor="color"   //渐变开始点的颜色
    //    android:centerColor="color"  //渐变中间点的颜色，在开始与结束点之间
    //    android:endColor="color"    //渐变结束点的颜色
    //    android:gradientRadius="float"  //渐变的半径，只有当渐变类型为radial时才能使用
    //    android:useLevel=["true" | "false"] />  //使用LevelListDrawable时就要设置为true。设为false时才有渐变效果

    class Gradient constructor(daVinCi: DaVinCi? = null, manual: Boolean = false) : CommandExpression(daVinCi, manual) {

        override fun startTag(): String = tag

        companion object {
            const val tag = "gradient:["

            const val prop_start_color = "startColor:"
            const val prop_center_color = "centerColor:"
            const val prop_end_color = "endColor:"

            const val prop_type = "type:"

            const val prop_angle = "angle:"

            const val prop_center_x = "centerY:"
            const val prop_center_y = "centerX:"
            const val prop_use_level = "useLevel:"
            const val prop_gradient_radius = "gradientRadius:"

            const val TYPE_LINEAR = "linear"
            const val TYPE_RADIAL = "radial"
            const val TYPE_SWEEP = "sweep"
        }

        init {
            injectThenParse(daVinCi)
        }

        @ColorInt
        var startColor: Int? = null

        @ColorInt
        var centerColor: Int? = null

        @ColorInt
        var endColor: Int? = null

        var type: String? = TYPE_LINEAR //默认线性

        var angle: Int = 0

        var centerX: Float? = 0f
        var centerY: Float? = 0f
        var gradientRadius: Int? = null
        var useLevel: Boolean = false

        override fun injectThenParse(daVinCi: DaVinCi?) {
            this.daVinCi = daVinCi
            if (manual) {
                if (parseFromText)
                    parse(daVinCi)
                return
            }
            asPrimitiveParse(tag, daVinCi)
            parse(daVinCi)
        }

        private fun parse(daVinCi: DaVinCi?) {
            text?.let { it ->
                startColor = null
                centerColor = null
                endColor = null
                type = TYPE_LINEAR
                angle = 0
                centerX = 0f
                centerY = 0f
                gradientRadius = null
                useLevel = false

                it.split(";").forEach { e ->
                    when {
                        e.startsWith(prop_start_color) -> {
                            if (daVinCi != null)
                                startColor = parseColor(e.replace(prop_start_color, ""))
                        }

                        e.startsWith(prop_center_color) -> {
                            if (daVinCi != null)
                                centerColor = parseColor(e.replace(prop_center_color, ""))
                        }

                        e.startsWith(prop_end_color) -> {
                            if (daVinCi != null)
                                endColor = parseColor(e.replace(prop_end_color, ""))
                        }

                        e.startsWith(prop_type) -> {
                            type = e.replace(prop_type, "")
                        }

                        e.startsWith(prop_angle) -> {
                            angle = parseInt(e.replace(prop_angle, ""), 0) ?: 0
                        }

                        e.startsWith(prop_center_x) -> {
                            centerX = parseFloat(e.replace(prop_center_x, ""), 0f) ?: 0f
                        }
                        e.startsWith(prop_center_y) -> {
                            centerY = parseFloat(e.replace(prop_center_y, ""), 0f) ?: 0f
                        }

                        e.startsWith(prop_use_level) -> {
//                            useLevel = parseFloat(e.replace(prop_center_x, ""), 0f) ?: 0f
                        }
                        e.startsWith(prop_gradient_radius) -> {
                            if (daVinCi != null)
                                gradientRadius =
                                    toPx(e.replace(prop_gradient_radius, ""), daVinCi.context)
                        }

                        else -> {
                            if (DaVinCi.enableDebugLog) Log.d(sLogTag, "Gradient暂未支持解析:$e")
                        }
                    }
                }
            }
        }

        override fun interpret() {
            if (tag == tokenName || manual) {
                daVinCi?.let { d ->
                    d.core.setGradient(
                        when (type) {
                            TYPE_SWEEP -> DaVinCiCore.Gradient.Sweep
                            TYPE_RADIAL -> DaVinCiCore.Gradient.Radial
                            else -> DaVinCiCore.Gradient.Linear
                        }
                    )
                    d.core.setGradientCenterXY(centerX, centerY)
                    d.core.setGradientColor(startColor, centerColor, endColor)
                    d.core.setGradientAngle(angle)
                    d.core.setGradientRadius(gradientRadius?.toFloat() ?: 0f)
                }
            }
        }


        override fun toString(): String {
            return if (parseFromText)
                "$tag $text $END"
            else ("$tag " +
                    type.run { ";$prop_type$this" } +
                    startColor.run { ";$prop_start_color$this" } +
                    centerColor.run { ";$prop_center_color$this" } +
                    endColor.run { ";$prop_end_color$this" } +
                    centerX.run { ";$prop_center_x$this" } +
                    centerY.run { ";$prop_center_y$this" } +
                    angle.run { ";$prop_angle$this" } +
                    gradientRadius.run { ";$prop_gradient_radius$this" }
                    + " $END").replaceFirst(";", "")
        }
    }
    //endregion

    //region Padding 一般不用
    //        <!--<padding-->
    //        <!--android:left="integer"-->
    //        <!--android:top="integer"-->
    //        <!--android:right="integer"-->
    //        <!--android:bottom="integer" />-->
    class Padding(daVinCi: DaVinCi? = null, manual: Boolean = false) : CommandExpression(daVinCi, manual) {

        override fun startTag(): String = tag

        companion object {
            const val tag = "padding:["

            const val prop_left = "left:"
            const val prop_top = "top:"
            const val prop_right = "right:"
            const val prop_bottom = "bottom:"
        }

        init {
            injectThenParse(daVinCi)
        }

        @Px
        var left: Int? = null

        @Px
        var top: Int? = null

        @Px
        var right: Int? = null

        @Px
        var bottom: Int? = null

        override fun injectThenParse(daVinCi: DaVinCi?) {
            this.daVinCi = daVinCi
            if (manual) {
                if (parseFromText)
                    parse(daVinCi)
                return
            }
            asPrimitiveParse(tag, daVinCi)
            parse(daVinCi)
        }

        private fun parse(daVinCi: DaVinCi?) {
            text?.let { it ->
                left = null
                top = null
                it.split(";").forEach { e ->
                    when {
                        e.startsWith(prop_left) -> {
                            if (daVinCi != null)
                                left = toPx(e.replace(prop_left, ""), daVinCi.context)
                        }

                        e.startsWith(prop_top) -> {
                            if (daVinCi != null)
                                top = toPx(e.replace(prop_top, ""), daVinCi.context)
                        }

                        e.startsWith(prop_right) -> {
                            if (daVinCi != null)
                                right = toPx(e.replace(prop_right, ""), daVinCi.context)
                        }

                        e.startsWith(prop_bottom) -> {
                            if (daVinCi != null)
                                bottom = toPx(e.replace(prop_bottom, ""), daVinCi.context)
                        }
                    }
                }
            }
        }

        override fun interpret() {
            if (tag == tokenName || manual) {
                daVinCi?.core?.setPadding(
                    left?.toFloat() ?: 0f, top?.toFloat() ?: 0f,
                    right?.toFloat() ?: 0f, bottom?.toFloat() ?: 0f
                )
            }
        }


        override fun toString(): String {
            return if (parseFromText)
                "$tag $text $END"
            else ("$tag ${left?.run { ";$prop_left$this" }}" +
                    "${top?.run { ";$prop_top$this" }}" +
                    "${right?.run { ";$prop_right$this" }}" +
                    "${bottom?.run { ";$prop_bottom$this" }} $END").replaceFirst(";", "")
        }
    }
    //endregion

    //region Size 一般不用
    //        <!--<size-->
    //        <!--android:width="integer"-->
    //        <!--android:height="integer" />-->
    class Size(daVinCi: DaVinCi? = null, manual: Boolean = false) : CommandExpression(daVinCi, manual) {

        override fun startTag(): String = tag

        companion object {
            const val tag = "size:["

            const val prop_width = "width:"
            const val prop_height = "height:"
        }

        init {
            injectThenParse(daVinCi)
        }

        @Px
        var width: Int? = null

        @Px
        var height: Int? = null

        override fun injectThenParse(daVinCi: DaVinCi?) {
            this.daVinCi = daVinCi
            if (manual) {
                if (parseFromText)
                    parse(daVinCi)
                return
            }
            asPrimitiveParse(tag, daVinCi)
            parse(daVinCi)

        }

        private fun parse(daVinCi: DaVinCi?) {
            text?.let { it ->
                width = null
                height = null
                it.split(";").forEach { e ->
                    when {
                        e.startsWith(prop_width) -> {
                            if (daVinCi != null)
                                width = toPx(e.replace(prop_width, ""), daVinCi.context)
                        }

                        e.startsWith(prop_height) -> {
                            if (daVinCi != null)
                                height = toPx(e.replace(prop_height, ""), daVinCi.context)
                        }
                    }
                }
            }
        }

        override fun interpret() {
            if (tag == tokenName || manual) {
                daVinCi?.let { d ->
                    width?.let {
                        d.core.setSizeWidth(it.toFloat())
                    }
                    height?.let {
                        d.core.setSizeHeight(it.toFloat())
                    }
                }
            }
        }


        override fun toString(): String {
            return if (parseFromText)
                "$tag $text $END"
            else ("$tag ${width?.run { ";$prop_width$this" }}" +
                    "${height?.run { ";$prop_height$this" }} $END").replaceFirst(";", "")
        }
    }
    //endregion

    //region Stroke
    //        <!--<stroke-->
    //        <!--android:width="integer"-->
    //        <!--android:color="color"-->
    //        <!--android:dashWidth="integer"-->
    //        <!--android:dashGap="integer" />-->
    //shape:[ stroke:[ width:1dp;color:#aaaaaa;dashWidth:4;dashGap:6dp ] ]
    class Stroke(daVinCi: DaVinCi? = null, manual: Boolean = false) : CommandExpression(daVinCi, manual) {

        override fun startTag(): String = tag

        companion object {
            const val tag = "stroke:["

            const val prop_width = "width:"
            const val prop_color = "color:"
            const val prop_dash_width = "dashWidth:"
            const val prop_dash_gap = "dashGap:"
        }

        init {
            injectThenParse(daVinCi)
        }

        @Px
        var width: Int? = null

        @ColorInt
        var color: Int? = null

        @Px
        var dash_width: Int? = null

        @Px
        var dash_gap: Int? = null

        override fun injectThenParse(daVinCi: DaVinCi?) {
            this.daVinCi = daVinCi
            if (manual) {
                if (parseFromText)
                    parse(daVinCi)
                return
            }
            asPrimitiveParse(tag, daVinCi)
            parse(daVinCi)

        }

        private fun parse(daVinCi: DaVinCi?) {
            text?.let { it ->
                width = null
                color = null
                dash_gap = null
                dash_width = null
                it.split(";").forEach { e ->
                    when {
                        e.startsWith(prop_width) -> {
                            if (daVinCi != null)
                                width = toPx(e.replace(prop_width, ""), daVinCi.context)
                        }
                        e.startsWith(prop_color) -> {
                            color = parseColor(e.replace(prop_color, ""))
                        }
                        e.startsWith(prop_dash_gap) -> {
                            if (daVinCi != null)
                                dash_gap = toPx(e.replace(prop_dash_gap, ""), daVinCi.context)
                        }
                        e.startsWith(prop_dash_width) -> {
                            if (daVinCi != null)
                                dash_width = toPx(e.replace(prop_dash_width, ""), daVinCi.context)
                        }
                    }
                }
            }
        }

        override fun interpret() {
            if (tag == tokenName || manual) {
                daVinCi?.let { d ->
                    color?.let {
                        d.core.setStrokeColor(it)
                    }
                    width?.let {
                        d.core.setStrokeWidth(it.toFloat())
                    }
                    dash_width?.let {
                        d.core.setStrokeDashWidth(it.toFloat())
                    }
                    dash_gap?.let {
                        d.core.setStrokeDashGap(it.toFloat())
                    }
                }
            }
        }


        override fun toString(): String {
            return if (parseFromText)
                "$tag $text $END"
            else ("$tag ${width?.run { ";$prop_width$this" }}" +
                    "${color?.run { ";$prop_color$this" }}" +
                    "${dash_width?.run { ";$prop_dash_width$this" }}" +
                    "${dash_gap?.run { ";$prop_dash_gap$this" }} $END").replaceFirst(";", "")
        }
    }
    //endregion

    //region state
    class DState(daVinCi: DaVinCi? = null, manual: Boolean = false) : CommandExpression(daVinCi, manual) {

        private val states: MutableSet<State> by lazy { linkedSetOf() }

        //caution: 可能尚未解析出实质的State,务必注意
        var statesHash: Int = states.hashCode()
            private set


        fun appendStates(states: Array<out State>) {
            //ignore all check！
            this.states.addAll(states)
            text = states.joinToString(CommandExpression.state_separator)
        }

        override fun onTextContentSet(text: String?) {
            super.onTextContentSet(text)
            //state hash
            statesHash = text?.split(CommandExpression.state_separator)?.sorted()?.hashCode() ?: 0
        }


        companion object {
            const val tag = "state:["
            private const val state_separator = CommandExpression.state_separator
        }

        init {
            injectThenParse(daVinCi)
        }

        override fun injectThenParse(daVinCi: DaVinCi?) {
            this.daVinCi = daVinCi
            if (manual) {
                if (parseFromText)
                    parse(daVinCi)
                return
            }
            asPrimitiveParse(tag, daVinCi)
            parse(daVinCi)
        }

        private fun parse(daVinCi: DaVinCi?) {
            text?.let { content ->
                states.clear()
                if (daVinCi != null) {
                    parseStates(content)?.let { s ->
                        states.addAll(s)
                    }
                }

                states.takeUnless { it.isEmpty() } ?: log<State>("state 不能为空", null)
            }
        }

        override fun interpret() {
            states.takeUnless { it.isEmpty() } ?: log<State>("state 不能为空", null)

            if (tag == tokenName || manual) {
                daVinCi?.core?.asOneStateInStateListDrawable()?.states(states = states)
            }
        }

        override fun toString(): String {
            return "$tag ${if (parseFromText) text else states.joinToString(state_separator)} $END"
        }

        fun collect(): MutableList<State> {
            return states.toCollection(arrayListOf())
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

    //region StatedColor
    class StatedColor internal constructor(daVinCi: DaVinCi? = null, manual: Boolean = false) :
        CommandExpression(daVinCi, manual) {
        @ColorInt
        var colorInt: Int? = null //这是解析出来的，不要乱赋值

        val states: MutableList<State> by lazy { arrayListOf() }

        companion object {
            const val tag = "sc:["

            const val prop_state = "state:"

            const val prop_color = "color:"

            const val separator = state_separator
        }

        init {
            injectThenParse(daVinCi)
        }

        override fun injectThenParse(daVinCi: DaVinCi?) {
            this.daVinCi = daVinCi
            if (manual) {
                if (parseFromText)
                    parse(daVinCi)
                return
            }
            asPrimitiveParse(tag, daVinCi)
            parse(daVinCi)
        }

        private fun parse(daVinCi: DaVinCi?) {
            text?.let { content ->
                colorInt = null
                states.clear()

                content.split(";").forEach { e ->
                    when {
                        e.startsWith(prop_color) -> {
                            if (daVinCi != null)
                                colorInt = parseColor(e.replace(prop_color, ""))
                        }
                        e.startsWith(prop_state) -> {
                            if (daVinCi != null) {
                                parseStates(e.replace(prop_state, ""))?.let { s ->
                                    states.addAll(s)
                                }
                            }
                        }
                        else -> {
                            log("暂未支持解析:$e", null)
                        }
                    }
                }

                if (states.isEmpty())
                    log<State>("state 不能为空", null)
                colorInt ?: log<Int>("color 不能为空", null)
            }
        }

        override fun interpret() {
            val states: List<State> = (states.takeUnless { it.isEmpty() } ?: log<List<State>>("state 不能为空", null)) ?: return

            val colorInt = colorInt ?: log<Int>("color 不能为空", null) ?: return

            if (tag == tokenName || manual) {
                daVinCi?.core?.addColorItem(StateItem.of(colorInt).applyState(states))
            }
        }

        override fun toString(): String {
            return if (parseFromText)
                "$tag $text $END"
            else ("$tag " +
                    states.run { ";$prop_state$this" } +
                    colorInt.run { ";$prop_color$this" } +
                    " $END").replaceFirst(";", "")
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
            exp.listExpression().dState = DState(manual = true).apply {
                parseFromText = true
                this.text = states.joinToString(CommandExpression.state_separator)
            }

            host.shapeListExpression().append(exp)

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
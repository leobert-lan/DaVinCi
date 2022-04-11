package osp.leobert.android.davinci

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import osp.leobert.android.davinci.lookup.IApplierTagLookup
import osp.leobert.android.davinci.lookup.IColorLookup
import osp.leobert.android.davinci.lookup.IDimensionLookup
import osp.leobert.android.reporter.review.TODO
import java.util.*
import java.util.concurrent.Executors

@Suppress("unused")
class DaVinCi private constructor(
    private val config: DaVinCiConfig = DaVinCiConfig
) : IDimensionLookup by config, IColorLookup by config, IApplierTagLookup by config {
    companion object {
        var enableDebugLog = true

        private val sDaVinCiExecutor = Executors.newSingleThreadExecutor { r ->
            Thread(r, "DaVinCi Execute Thread").apply {
                // This thread is never stopped
                isDaemon = true
            }
        }

        private val dispatcher = sDaVinCiExecutor.asCoroutineDispatcher()

        internal val scopeMain = CoroutineScope(Dispatchers.Main)
        private val scopeExecutor = CoroutineScope(dispatcher)


        internal fun <T> T.daVinCiExecute(runnable: T.() -> Unit): T {
            scopeExecutor.launch {
                runnable(this@daVinCiExecute)
            }
            return this
        }

        internal suspend fun <T, R> T.daVinCiExecute(runnable: T.() -> R, listen: suspend R.() -> Unit) {
            flow {
                this.emit(runnable(this@daVinCiExecute))
            }.flowOn(dispatcher)
                .onEach(listen)
                .collect()
        }

        internal fun <T, R> T.daVinCiExecute(scope: CoroutineScope, runnable: T.() -> R, listen: suspend R.() -> Unit) {
            flow {
                this.emit(runnable(this@daVinCiExecute))
            }.flowOn(dispatcher)
                .onEach(listen)
                .launchIn(scope)
        }

        /*
        * 在Application静态块中调用一次，基本可以免去Application onCreate时加载样式，或者第一次使用DaVinCi的耗时
        * */
        fun fastLoad() {
            daVinCiExecute {
                val start = System.nanoTime()
                DaVinCiExpression.stateListDrawable()
                    .shape(DaVinCiExpression.shape().rectAngle().solid("#ffffffff").stroke("1", "#ffffffff").corner("0"))
                    .states(State.ENABLE_F)

                val cost = System.nanoTime() - start
                if (enableDebugLog)
                    Log.d(
                        DaVinCiExpression.sLogTag,
                        "davinci-timecost first load cost:$cost ns, about ${cost / 1000_000} ms, it will be faster next time"
                    )

            }

        }

        internal inline fun <reified R> Any?.takeIfInstance(): R? {
            if (this is R) return this
            return null
        }

        val factory: DPools.Factory<DaVinCi> = object : DPools.Factory<DaVinCi> {
            override fun create(): DaVinCi {
                return DaVinCi()
            }
        }

        val resetter: DPools.Resetter<DaVinCi> = object : DPools.Resetter<DaVinCi> {
            override fun reset(target: DaVinCi) {
                target.core.clear()
                target.applier = null
                target.stringTokenizer = null
                target.currentToken = null
                target.clearAllVariable()
            }
        }

        fun of(text: String? = null, applier: Applier? = null): DaVinCi {
            return requireNotNull(DPools.daVinCiPool.acquire()).apply {
                this.stringTokenizer = text?.run { StringTokenizer(this) }
                this.applier = applier
            }
        }
    }

    fun release() {
        daVinCiExecute {
            Log.e("lmsg", "release davinci, ${hashCode()}")
            DPools.daVinCiPool.release(this)
        }
    }

    val context: Context
        get() = requireNotNull(applier).context

    val core: DaVinCiCore by lazy {
        DaVinCiCore()
    }

    var applier: Applier? = null
        internal set

    // 待解析的文本内容
    // 使用空格分隔待解析文本内容
    private var stringTokenizer: StringTokenizer? = null

    // 当前命令
    var currentToken: String? = null

    // 用来存储动态变化信息内容
    private val map: MutableMap<String, Any> = HashMap()

    fun applySld(exp: DaVinCiExpression.StateListDrawable) {
        applySld(exp, null)
    }

    fun applySld(exp: DaVinCiExpression.StateListDrawable, onComplete: (() -> Unit)? = null) {
        daVinCiExecute(scope = scopeMain, runnable = {
            if (enableDebugLog && exp.manual) //手动创建的结构
                Log.d(DaVinCiExpression.sLogTag, "manual created,daVinCi sld:$exp")

            exp.injectThenParse(this@DaVinCi)
            exp.interpret()

            if (enableDebugLog && !exp.manual) //利用DaVinCi内容解析的结构
                Log.d(DaVinCiExpression.sLogTag, "parsed,daVinCi sld:$exp")
            core.buildDrawable()
        }) {
            applier?.applyDrawable(this)
            onComplete?.invoke()
        }
    }

    fun applyShape(exp: DaVinCiExpression.Shape) {
        applyShape(exp, null)
    }

    fun applyShape(exp: DaVinCiExpression.Shape, onComplete: (() -> Unit)? = null) {
        daVinCiExecute(scope = scopeMain, runnable = {
            if (enableDebugLog && exp.manual) //手动创建的结构
                Log.d(DaVinCiExpression.sLogTag, "manual created,${hashCode()},daVinCi sld:$exp")

            exp.injectThenParse(this)
            exp.interpret()

            if (enableDebugLog && !exp.manual) //利用DaVinCi内容解析的结构
                Log.d(DaVinCiExpression.sLogTag, "parsed,daVinCi sld:$exp")

            core.buildSimpleDrawable()
        }) {
            applier?.applyDrawable(this)
            onComplete?.invoke()
        }
    }

    fun applyCsl(exp: DaVinCiExpression.ColorStateList) {
        applyCsl(exp, null)
    }

    @TODO("`core.clear()` -- figure out why clear it, if exp is cycle-using?")
    fun applyCsl(exp: DaVinCiExpression.ColorStateList, onComplete: (() -> Unit)? = null) {
        daVinCiExecute(scope = scopeMain, runnable = {
            if (enableDebugLog)
                Log.d(DaVinCiExpression.sLogTag, "daVinCi csl:$exp")

            Log.e("lmsg", "applyCsl,${hashCode()} ${this.applier == null}")
            core.clear()
            exp.injectThenParse(this@DaVinCi)
            exp.interpret()
            val ret = core.buildTextColor()
            core.clear() //todo figure out why clear it, if exp is cycle-using?
            ret

        }) {
            applier?.applyColorStateList(this)
            onComplete?.invoke()
        }

    }

    /*
     * 解析文本
     */
    internal operator fun next(): String? {
        currentToken = if (stringTokenizer?.hasMoreTokens() == true) {
            stringTokenizer?.nextToken()
        } else {
            null
        }
        return currentToken
    }

    /*
     * 判断命令是否正确
     */
    internal fun equalsWithCommand(command: String?): Boolean {
        return command != null && command == currentToken
    }

    /*
     * 获得节点的内容
     */
    internal fun getTokenContent(text: String?): String? {
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

    fun clearAllVariable() {
        map.clear()
    }
}
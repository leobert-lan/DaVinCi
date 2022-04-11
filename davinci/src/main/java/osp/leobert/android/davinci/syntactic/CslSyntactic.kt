package osp.leobert.android.davinci.syntactic

import androidx.annotation.ColorInt
import osp.leobert.android.davinci.DPools
import osp.leobert.android.davinci.DaVinCiExpression
import osp.leobert.android.davinci.Statable
import osp.leobert.android.davinci.State
import osp.leobert.android.davinci.expressions.StatedColor

class CslSyntactic private constructor() : Statable<DaVinCiExpression.ColorStateList> {

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

        fun of(host: DaVinCiExpression.ColorStateList, color: String): CslSyntactic {
            return requireNotNull(DPools.cslSyntacticPool.acquire()).apply {
                this.color = color
                this.host = host
            }
        }

        fun of(host: DaVinCiExpression.ColorStateList, @ColorInt color: Int): CslSyntactic {
            return requireNotNull(DPools.cslSyntacticPool.acquire()).apply {
                this.colorInt = color
                this.host = host
            }
        }

        private const val TAG_COLOR_NOT_INIT = 0
        private const val TAG_COLOR_STR = 1
        private const val TAG_COLOR_INT = 2
    }

    var host: DaVinCiExpression.ColorStateList? = null

    var color: String? = null
        set(value) {
            field = value
            if (value != null)
                colorTag = TAG_COLOR_STR
        }

    var colorInt: Int? = null
        set(value) {
            field = value
            if (value != null)
                colorTag = TAG_COLOR_INT
        }

    private var colorTag = TAG_COLOR_NOT_INIT

    private fun strColor(): String? {
        return when (colorTag) {
            TAG_COLOR_STR -> color
            TAG_COLOR_INT -> "#" + String.format("%8x", colorInt)
            else -> null
        }
    }

    override fun states(vararg states: State): DaVinCiExpression.ColorStateList {
        val host = requireNotNull(host)

        val statedColor = StatedColor.create(
            manual = true,
            //如果颜色也不需要解析，则无需从文本解析
            parseFromText = this@CslSyntactic.colorTag == 1,
            colorInt = this@CslSyntactic.colorInt.takeIf { this@CslSyntactic.colorTag == TAG_COLOR_INT },
            colorStr = this@CslSyntactic.strColor(),
            states = states
        )
        host.statedStub().append(statedColor)
        DPools.cslSyntacticPool.release(this)
        return host

    }

    override fun states(vararg states: String): DaVinCiExpression.ColorStateList {

        val host = requireNotNull(host)
        val statedColor = StatedColor.create(
            manual = true,
            parseFromText = true,
            colorInt = this@CslSyntactic.colorInt.takeIf { this@CslSyntactic.colorTag == TAG_COLOR_INT },
            colorStr = this@CslSyntactic.strColor(),
            states = states
        )
        host.statedStub().append(statedColor)
        DPools.cslSyntacticPool.release(this)
        return host
    }
}
package osp.leobert.android.davinci.syntactic

import osp.leobert.android.davinci.DPools
import osp.leobert.android.davinci.DaVinCiExpression
import osp.leobert.android.davinci.Statable
import osp.leobert.android.davinci.State
import osp.leobert.android.davinci.expressions.DState

class SldSyntactic private constructor() : Statable<DaVinCiExpression.StateListDrawable> {
    private var exp: DaVinCiExpression.Shape? = null
    private var host: DaVinCiExpression.StateListDrawable? = null

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

        fun of(host: DaVinCiExpression.StateListDrawable, exp: DaVinCiExpression.Shape): SldSyntactic {
            return requireNotNull(DPools.sldSyntacticPool.acquire()).apply {
                this.exp = exp
                this.host = host
            }
        }
    }

    override fun states(vararg states: State): DaVinCiExpression.StateListDrawable {

        val host = requireNotNull(host)
        val exp = requireNotNull(exp)
        val dState = DState.manualCreate(
            states = states
        )

        exp.statedStub().setDState(dState)
        host.stub().setExpression(dState, exp)

        DPools.sldSyntacticPool.release(this)
        return host
    }

    override fun states(vararg states: String): DaVinCiExpression.StateListDrawable {
        val host = requireNotNull(host)
        val exp = requireNotNull(exp)
        val dState = DState.manualCreate(
            states = states
        )
        exp.statedStub().setDState(dState)
        host.stub().setExpression(dState, exp)

        DPools.sldSyntacticPool.release(this)
        return host
    }
}
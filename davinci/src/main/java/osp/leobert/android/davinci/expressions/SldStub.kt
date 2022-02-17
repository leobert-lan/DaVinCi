package osp.leobert.android.davinci.expressions

import android.util.Log
import osp.leobert.android.davinci.DPools
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.DaVinCiExpression
import osp.leobert.android.davinci.strategyOf
import osp.leobert.android.davinci.uml.ExpDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram

/**
 * 让StateListDrawable的构建和解析过程更加明确添加的Stub
 * */
@NotTerminal
@ExpDiagram
@GenerateClassDiagram
internal class SldStub private constructor() : ExpressionStub<SldStub>(parser) {

    companion object {

        val parser = strategyOf<String, StubItemParser<SldStub>>()
            .register(DaVinCiExpression.Shape.tag, object : StubItemParser<SldStub> {
                override fun parse(t: SldStub, daVinCi: DaVinCi): DaVinCiExpression {
                    return DaVinCiExpression.Shape.of(daVinCi = daVinCi)
                }
            })

        val factory: DPools.Factory<SldStub> = object : DPools.Factory<SldStub> {
            override fun create(): SldStub {
                return SldStub()
            }
        }

        fun of(manual: Boolean = false): SldStub {
            return requireNotNull(DPools.sldStubPool.acquire()).apply {
                this.manual = manual
            }
        }
    }

    override fun interpret() {
        if (manual) {
            list.forEach { it.interpret() }
        } else {
            if (DaVinCi.enableDebugLog) Log.d(DaVinCiExpression.sLogTag, "已自动解析")
        }
    }

    override fun release() {
        super.release()
        DPools.sldStubPool.release(this)
    }

    override fun getT(): SldStub {
        return this
    }


}
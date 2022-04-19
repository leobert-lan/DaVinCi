package osp.leobert.android.davinci.expressions

import osp.leobert.android.davinci.pool.DPools
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.DaVinCiExpression
import osp.leobert.android.davinci.strategyOf
import osp.leobert.android.davinci.uml.ExpDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.android.reporter.review.TODO

/**
 * 让StateListDrawable的构建和解析过程更加明确添加的Stub
 *
 * 本质上代表了一个 [DaVinCiExpression.StateListDrawable] 的实质内容，
 * 创建AST的逻辑复用 [ExpressionStub]，基于组合模式保障了 [DaVinCiExpression.StateListDrawable] 的继承结构不受干扰，剥离了AST建立细节
 * */
@NotTerminal
@ExpDiagram
@GenerateClassDiagram
internal class SldStub private constructor() : ExpressionStub<SldStub>(parser) {

    companion object {

        //按照规则，Sld中可以放置的根元素需要在此进行注册
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
        list.forEach { it.interpret() }
    }

    @TODO(desc = "考虑排序,但是需要注意，它是否已经完成解析")
    override fun append(exp: DaVinCiExpression) {
        super.append(exp)
    }

    override fun release() {
        super.release()
        DPools.sldStubPool.release(this)
    }

    override fun getT(): SldStub {
        return this
    }


}
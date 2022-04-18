package osp.leobert.android.davinci.expressions

import android.annotation.SuppressLint
import android.util.Log
import osp.leobert.android.davinci.*
import osp.leobert.android.davinci.uml.ExpDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.android.reporter.review.TODO

/**
 * 状态化的Stub，可包含一个 [DState] 标识状态，以及一个list 包含 [DaVinCiExpression],
 * 复用了 [ExpressionStub] 构建AST的逻辑
 * 目前可用于表示 ：
 * [DaVinCiExpression.ColorStateList] 的内容，则list内容为 [StatedColor]
 * [DaVinCiExpression.Shape] 的内容
 *
 * 很显然，这两者设计格格不入，因为 ColorStateList 实现进行了简化，如果设计一致，则 [DaVinCiExpression.ColorStateList] 需要组合一个 CslStub 类似 [SldStub]
 * CslStub 中 包含类似 [DaVinCiExpression.Shape] 的 Color ,Color 组合一个 [StatedStub], 以其 [StatedStub.dState] 作为状态，加一个色值表达Expression 于list中。
 *
 * 显而易见，这复杂了，所以问题在于 [DaVinCiExpression.Shape] 相比之下更加复杂！原因在于：设计[DaVinCiExpression.StateListDrawable] 的DSL语法时，
 * 为了减少一层嵌套而将状态表达加入了Shape之内, 所以他的本质是StatedShape , 即本身应该 DState + Shape => StatedShape 而实际为 {list(exp).remove(dState) + dState} => Shape
 * 而 [DaVinCiExpression.ColorStateList] 太简单了，以至于不想再用 : DState + Color => StatedColor 而是直接定义了 {list(state)+color} => StatedColor
 *
 * 从文法和OO基本原则上看，这两个行为都挖了坑
 * */
@NotTerminal
@ExpDiagram
@GenerateClassDiagram
internal class StatedStub private constructor() : ExpressionStub<StatedStub>(parser) {

    companion object {

        val parser = strategyOf<String, StubItemParser<StatedStub>>()
            .register(DState.tag, object : StubItemParser<StatedStub> {
                override fun parse(t: StatedStub, daVinCi: DaVinCi): DaVinCiExpression? {

                    if (t.dState != null) {
                        if (DaVinCi.enableDebugLog) Log.d(
                            DaVinCiExpression.sLogTag,
                            "only one state expression is permitted in one group, will override old one ${t.dState}"
                        )
                    }
                    t.dState = DState.of(daVinCi)

                    return null
                }
            })
            .register(Corners.tag, object : StubItemParser<StatedStub> {
                override fun parse(t: StatedStub, daVinCi: DaVinCi): DaVinCiExpression {
                    return Corners.of(daVinCi)
                }
            })
            .register(Solid.tag, object : StubItemParser<StatedStub> {
                override fun parse(t: StatedStub, daVinCi: DaVinCi): DaVinCiExpression {
                    return Solid.of(daVinCi = daVinCi)
                }
            })
            .register(ShapeType.tag, object : StubItemParser<StatedStub> {
                override fun parse(t: StatedStub, daVinCi: DaVinCi): DaVinCiExpression {
                    return ShapeType.of(daVinCi = daVinCi)
                }
            })
            .register(Stroke.tag, object : StubItemParser<StatedStub> {
                override fun parse(t: StatedStub, daVinCi: DaVinCi): DaVinCiExpression {
                    return Stroke.of(daVinCi = daVinCi)
                }
            })
            .register(Size.tag, object : StubItemParser<StatedStub> {
                override fun parse(t: StatedStub, daVinCi: DaVinCi): DaVinCiExpression {
                    return Size.of(daVinCi = daVinCi)
                }
            })
            .register(Padding.tag, object : StubItemParser<StatedStub> {
                override fun parse(t: StatedStub, daVinCi: DaVinCi): DaVinCiExpression {
                    return Padding.of(daVinCi = daVinCi)
                }
            })
            .register(Gradient.tag, object : StubItemParser<StatedStub> {
                override fun parse(t: StatedStub, daVinCi: DaVinCi): DaVinCiExpression {
                    return Gradient.of(daVinCi = daVinCi)
                }
            })
            .register(StatedColor.tag, object : StubItemParser<StatedStub> {
                override fun parse(t: StatedStub, daVinCi: DaVinCi): DaVinCiExpression {
                    return StatedColor.of(daVinCi = daVinCi)
                }
            })

        val factory: DPools.Factory<StatedStub> = object : DPools.Factory<StatedStub> {
            override fun create(): StatedStub {
                return StatedStub()
            }
        }

        fun of(manual: Boolean = false): StatedStub {
            return requireNotNull(DPools.statedStubPool.acquire()).apply {
                this.manual = manual
            }
        }
    }

    /**
     * in rule: only one state expression is permitted
     * only used for Shape in StatedListDrawable
     * */
    private var dState: DState? = null

    override fun reset() {
        super.reset()
        manual = false
        dState = null
        list.clear()
    }


    override fun release() {
        super.release()
        dState?.release()
        DPools.statedStubPool.release(this)
    }

    fun setDState(dState: DState) {
        this.dState = dState
    }

    @TODO(desc = "当其为ColorStateList情况时,考虑排序,但是需要注意，它是否已经完成解析")
    override fun append(exp: DaVinCiExpression) {
        super.append(exp)
    }

    /**
     * different with [append], it's only used for DState to serve [DaVinCiExpression.Shape]
     * */
    fun appendState(daVinCi: DaVinCi?, vararg states: State) {
        val dState = dState ?: DState.of(daVinCi)
        dState.appendStates(states)
        this.dState = dState
    }

    internal fun states(): MutableList<State>? {
        return dState?.collect()
    }

    internal fun statesArray(): Array<State>? {
        return dState?.collectToArray()
    }

    override fun onManualInjectThenParse(daVinCi: DaVinCi?) {
        super.onManualInjectThenParse(daVinCi)
        dState?.injectThenParse(daVinCi)
    }

    @SuppressLint("all")
    override fun interpret() {
        list.forEach { it.interpret() }
        dState?.interpret()
    }

    override fun toString(): String {
        val b = StringBuilder()

        dState?.let { b.append(it.toString()) }
        if (dState != null)
            b.append("; ")
        return b.append(super.toString()).toString()

    }

    fun equalState(dState: DState): Boolean {
        return (this.dState?.statesHash ?: 0) == dState.statesHash
    }

    override fun getT(): StatedStub {
        return this
    }


}
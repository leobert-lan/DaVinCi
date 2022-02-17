package osp.leobert.android.davinci.expressions

import android.annotation.SuppressLint
import android.util.Log
import osp.leobert.android.davinci.*
import osp.leobert.android.davinci.uml.ExpDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram

//专门用于解析一串shape
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
     * */
    var dState: DState? = null

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


    fun appendState(daVinCi: DaVinCi?, vararg states: State) {
        val dState = dState ?: DState.of(daVinCi)
        dState.appendStates(states)
        this.dState = dState
    }

    internal fun states(): MutableCollection<State>? {
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

    fun containsState(dState: DState): Boolean {
        return (this.dState?.statesHash ?: 0) == dState.statesHash
    }

    override fun getT(): StatedStub {
        return this
    }


}
package osp.leobert.android.davinci.expressions

import android.util.Log
import osp.leobert.android.davinci.DPools
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.uml.ExpDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram

/*corners-->
<!--android:radius="integer"-->
<!--android:topLeftRadius="integer"-->
<!--android:topRightRadius="integer"-->
<!--android:bottomLeftRadius="integer"-->
<!--android:bottomRightRadius="integer" />-->
shape:[ corners:[ 4 ] solid:[ #353538 ] ]*/
@NotTerminal
@ExpDiagram
@GenerateClassDiagram
internal class Corners private constructor() : ShapeSpecExpression() {

    var conners: List<Int>? = null

    companion object {
        const val tag = "corners:["

        val factory: DPools.Factory<Corners> = object : DPools.Factory<Corners> {
            override fun create(): Corners {
                return Corners()
            }
        }

        fun of(daVinCi: DaVinCi? = null, manual: Boolean = false): Corners {
            return requireNotNull(DPools.cornersExpPool.acquire()).apply {
                this.manual = manual
                injectThenParse(daVinCi)
            }
        }
    }

    override fun reset() {
        super.reset()
        conners = null
    }

    override fun release() {
        super.release()
        DPools.cornersExpPool.release(this)
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
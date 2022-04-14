package com.example.simpletest.factories

import android.graphics.Color
import android.view.ViewGroup
import com.example.simpletest.Utils
import osp.leobert.android.davinci.DaVinCi
import osp.leobert.android.davinci.DaVinCiExpression
import osp.leobert.android.davinci.State
import osp.leobert.android.davinci.StyleRegistry
import osp.leobert.android.davinci.annotation.DaVinCiStyle
import osp.leobert.android.davinci.annotation.StyleViewer

/**
 * <p><b>Package:</b> com.example.simpletest.factories </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> DemoStyle </p>
 * Created by leobert on 2021/6/17.
 */
@DaVinCiStyle(styleName = "btn_style.main")
@StyleViewer(
    height = 40, width = ViewGroup.LayoutParams.MATCH_PARENT,
    type = StyleViewer.FLAG_CSL or StyleViewer.FLAG_BG, background = "#ffffff"
)
class DemoStyle : StyleRegistry.Style("btn_style.main") {
    init {
        Utils.timeCost("create DemoStyle 'btn_style.main'") {
            this.registerSld(
                exp = DaVinCiExpression.stateListDrawable()
                    .shape(DaVinCiExpression.shape().stroke("1", "#ff653c").corner("2dp"))
                    .states(State.CHECKED_F, State.ENABLE_T)

                    .shape(DaVinCiExpression.shape().solid("#ff653c").corner("2dp,2dp,0,0"))
                    .states(State.CHECKED_T, State.ENABLE_T)

                    .shape(DaVinCiExpression.shape().rectAngle().solid("#80ff3c08").corner("10dp")).states(State.ENABLE_F)

            ).registerCsl(
                exp = DaVinCiExpression.stateColor()
                    .color("#000000").states(State.ENABLE_T, State.CHECKED_T)
                    .color("#666666").states(State.ENABLE_T, State.CHECKED_F)
                    .color("#ffffff").states(State.ENABLE_F)
            )
        }
    }
}

@DaVinCiStyle(styleName = "btn_style.test")
@StyleViewer(
    height = 48, width = 150,
    type = StyleViewer.FLAG_CSL or StyleViewer.FLAG_BG, background = "#ffffff"
)
class DemoStyle2 : StyleRegistry.Style("btn_style.test") {
    init {

        val shape = Utils.timeCost("if first create shape") {
            DaVinCiExpression.shape().rectAngle().solid("#80ff3c08").corner("10dp")
        }

        Utils.timeCost("if first init") {
            DaVinCiExpression.stateListDrawable()
                .shape(shape)
                .states(State.ENABLE_F)
        }

        Utils.timeCost("create DemoStyle2 'btn_style.test'") {
            this.registerSld(
                exp = DaVinCiExpression.stateListDrawable()
                    .shape(DaVinCiExpression.shape().rectAngle().solid("#80ff3c08").corner("10dp"))
                    .states(State.ENABLE_F)
                    .shape(
                        DaVinCiExpression.shape().rectAngle().corner("10dp")
//                        .solid("#ff653c") 用 solid对比一下耗时 并未有明显变化
                            .gradient("#ff3c08", "#ff653c", 0)
                    )
                    .states(State.ENABLE_T)
            )
        }
    }
}

//以下均为测试耗时而增加的马甲

@DaVinCiStyle(styleName = "btn_style.test2")
@StyleViewer(
    height = 48, width = 150,
    type = StyleViewer.FLAG_CSL or StyleViewer.FLAG_BG, background = "#ffffff"
)
class DemoStyle3 : StyleRegistry.Style("btn_style.test2") {
    init {
        val shape = DaVinCiExpression.shape().rectAngle().solid("#80ff3c08").corner("10dp")

        DaVinCiExpression.stateListDrawable()
            .shape(shape)
            .states(State.ENABLE_F)

        this.registerSld(
            exp = DaVinCiExpression.stateListDrawable()
                .shape(DaVinCiExpression.shape().rectAngle().solid("#80ff3c08").corner("10dp"))
                .states(State.ENABLE_F)
                .shape(
                    DaVinCiExpression.shape().rectAngle().corner("10dp")
//                        .solid("#ff653c") 用 solid对比一下耗时 并未有明显变化
                        .gradient("#ff3c08", "#ff653c", 0)
                )
                .states(State.ENABLE_T)
        )
    }
}

@DaVinCiStyle(styleName = "btn_style.test3")
@StyleViewer(
    height = 48, width = 150,
    type = StyleViewer.FLAG_CSL or StyleViewer.FLAG_BG, background = "#ffffff"
)
class DemoStyle4 : StyleRegistry.Style("btn_style.test3") {
    init {
        val shape = DaVinCiExpression.shape().rectAngle().solid("#80ff3c08").corner("10dp")

        DaVinCiExpression.stateListDrawable()
            .shape(shape)
            .states(State.ENABLE_F)

        this.registerSld(
            exp = DaVinCiExpression.stateListDrawable()
                .shape(DaVinCiExpression.shape().rectAngle().solid("#80ff3c08").corner("10dp"))
                .states(State.ENABLE_F)
                .shape(
                    DaVinCiExpression.shape().rectAngle().corner("10dp")
//                        .solid("#ff653c") 用 solid对比一下耗时 并未有明显变化
                        .gradient("#ff3c08", "#ff653c", 0)
                )
                .states(State.ENABLE_T)
        )
    }
}
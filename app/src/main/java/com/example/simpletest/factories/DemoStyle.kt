package com.example.simpletest.factories

import android.view.ViewGroup
import osp.leobert.android.davinci.DaVinCiExpression
import osp.leobert.android.davinci.State
import osp.leobert.android.davinci.StyleRegistry
import osp.leobert.android.davinci.annotation.DaVinCiStyle
import osp.leobert.android.davinci.annotation.StyleViewer

/**
 * <p><b>Package:</b> com.example.simpletest.factories </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> DemoStyle </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2021/6/17.
 */
@DaVinCiStyle(styleName = "btn_style.main")
@StyleViewer(height = 40, width = ViewGroup.LayoutParams.MATCH_PARENT,
    type = StyleViewer.FLAG_CSL or StyleViewer.FLAG_BG, background = "#ffffff")
class DemoStyle : StyleRegistry.Style("btn_style.main") {
    init {
        this.register(
            state = State.STATE_CHECKED_FALSE,
            expression = DaVinCiExpression.shape().stroke("1", "#ff653c").corner("2dp")
        ).register(State.STATE_CHECKED_TRUE,
            DaVinCiExpression.shape().solid("#ff653c").corner("2dp,2dp,0,0")
        ).register(
            state = State.STATE_ENABLE_FALSE,
            expression = DaVinCiExpression.shape().rectAngle().solid("#80ff3c08").corner("10dp")
        ).register(
            state = State.STATE_ENABLE_TRUE,
            expression = DaVinCiExpression.shape().rectAngle().corner("10dp")
                .gradient("#ff3c08", "#ff653c", 0)
        ).registerCsl(
            exp = DaVinCiExpression.stateColor()
                .color("#000000").states(State.STATE_ENABLE_TRUE, State.STATE_CHECKED_TRUE)
                .color("#666666").states(State.STATE_ENABLE_TRUE, State.STATE_CHECKED_FALSE)
                .color("#ffffff").states(State.STATE_ENABLE_FALSE)

//                .apply(
//                    state = State.STATE_ENABLE_FALSE,
//                    color = "#ffffff"
//                ).apply(
//                    state = State.STATE_ENABLE_TRUE,
//                    color = "#333333"
//                )
        )
    }
}

@DaVinCiStyle(styleName = "btn_style.test")
@StyleViewer(height = 48, width = 150,
    type = StyleViewer.FLAG_CSL or StyleViewer.FLAG_BG, background = "#ffffff")
class DemoStyle2 : StyleRegistry.Style("btn_style.test") {
    init {
        this.register(
            state = State.STATE_ENABLE_FALSE,
            expression = DaVinCiExpression.shape().rectAngle().solid("#80ff3c08").corner("10dp")
        ).register(
            state = State.STATE_ENABLE_TRUE,
            expression = DaVinCiExpression.shape().rectAngle().corner("10dp")
                .gradient("#ff3c08", "#ff653c", 0)
        )
    }
}
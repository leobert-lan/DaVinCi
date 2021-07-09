package com.example.simpletest.factories

import osp.leobert.android.davinci.DaVinCiExpression
import osp.leobert.android.davinci.State
import osp.leobert.android.davinci.StyleRegistry
import osp.leobert.android.davinci.annotation.DaVinCiStyle

/**
 * <p><b>Package:</b> com.example.simpletest.factories </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> DemoStyle </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2021/6/17.
 */
@DaVinCiStyle(styleName = "btn_style.main")
class DemoStyle : StyleRegistry.Style("btn_style.main") {
    init {
        this.register(
            state = State.STATE_ENABLE_FALSE,
            expression = DaVinCiExpression.shape().rectAngle().solid("#80ff3c08").corner("10dp")
        ).register(
            state = State.STATE_ENABLE_TRUE,
            expression = DaVinCiExpression.shape().rectAngle().corner("10dp")
                .gradient("#ff3c08", "#ff653c", 0)
        ).registerCsl(
            exp = DaVinCiExpression.stateColor().apply(
                state = State.STATE_ENABLE_FALSE,
                color = "#ffffff"
            ).apply(
                state = State.STATE_ENABLE_TRUE,
                color = "#333333"
            )
        )
    }
}

@DaVinCiStyle(styleName = "btn_style.test")
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
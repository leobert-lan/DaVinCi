package com.example.simpletest.factories

import osp.leobert.android.davinci.DaVinCiExpression
import osp.leobert.android.davinci.State
import osp.leobert.android.davinci.StyleRegistry

/**
 * <p><b>Package:</b> com.example.simpletest.factories </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> DemoStyleFactory </p>
 * Created by leobert on 2021/6/2.
 */
class DemoStyleFactory : StyleRegistry.Style.Factory() {
    override val styleName: String = "btn_style.main"

    override fun apply(style: StyleRegistry.Style) {
        style.register(
            state = State.STATE_ENABLE_FALSE,
            expression = DaVinCiExpression.shape().rectAngle().solid("#80ff3c08").corner("10dp")
        ).register(
            state = State.STATE_ENABLE_TRUE,
            expression = DaVinCiExpression.shape().rectAngle().corner("10dp")
                .gradient("#ff3c08", "#ff653c", 0)
        )
    }
}
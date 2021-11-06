package com.example.simpletest.factories

import osp.leobert.android.davinci.DaVinCiExpression
import osp.leobert.android.davinci.State
import osp.leobert.android.davinci.StyleRegistry
import osp.leobert.android.davinci.annotation.DaVinCiStyleFactory

/**
 * <p><b>Package:</b> com.example.simpletest.factories </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> DemoStyleFactory </p>
 * Created by leobert on 2021/6/2.
 */
@DaVinCiStyleFactory(styleName = "btn_style.sub")
class DemoStyleFactory : StyleRegistry.Style.Factory() {
    override val styleName: String = "btn_style.sub"

    override fun apply(style: StyleRegistry.Style) {
        style.register(
            state = State.ENABLE_F,
            expression = DaVinCiExpression.shape().rectAngle().solid("#80ff3c08").corner("10dp")
        ).register(
            state = State.ENABLE_T,
            expression = DaVinCiExpression.shape().rectAngle().corner("10dp")
                .gradient("#ff3c08", "#ff653c", 0)
        )
    }
}
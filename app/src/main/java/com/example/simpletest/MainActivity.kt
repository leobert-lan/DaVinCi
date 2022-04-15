package com.example.simpletest

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.simpletest.databinding.ActivityMainBinding
import osp.leobert.android.davinci.DaVinCiExpression
import osp.leobert.android.davinci.State
import osp.leobert.android.davinci.daVinCiColor
import osp.leobert.android.davinci.daVinCiSld

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Utils.timeCost("单纯加载布局 compare1") {
//            setContentView( R.layout.activity_main_none)
//        }
//
//        Utils.timeCost("单纯加载布局 compare2") {
//            setContentView( R.layout.activity_main_none)
//        }
//
//        Utils.timeCost("加载databinding compare") {
//            DataBindingUtil.setContentView<ActivityMainNoneBinding>(this, R.layout.activity_main_none)
//        }
//
//        Utils.timeCost("加载databinding compare2") {
//            DataBindingUtil.setContentView<ActivityMainNoneBinding>(this, R.layout.activity_main_none)
//        }

        val binding = Utils.timeCost("加载databinding exist davinci") {
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        }
        // 通过简单的对比可以发现，排除掉必要的LayoutInflater初始化等，时间上并无明显差异，DaVinCi解析（包括构建）过程分离到子线程后，已经对性能无影响


        DaVinCiExpression.stateColor()
            .color("#e5332c").states(State.PRESSED_T)
            .color("#667700").states(State.PRESSED_F)
            .let {
                binding.test.daVinCiColor(it)
            }

        DaVinCiExpression.stateColor()
            .color(Color.parseColor("#ff0000")).states(State.PRESSED_F, State.CHECKED_T)
            .color(Color.parseColor("#0000dd")).states(State.PRESSED_T, State.CHECKED_T)
            .color(Color.parseColor("#ff0000")).states(State.CHECKED_T.name)
            .color(Color.parseColor("#00aa00")).states(State.CHECKED_F)
            .let {
                binding.cb1.daVinCiColor(it)
            }


        binding.test.setOnClickListener {
            val exp = Utils.timeCost("普通代码创建") {
                DaVinCiExpression.shape().oval()
                    .corner("40dp") //这个就没啥用了
                    .solid(resources.getColor(R.color.colorPrimaryDark))
                    .stroke(12, Color.parseColor("#26262a"))
            }

            Utils.timeCost("exp 到设置到view") {
                exp.applyInto(it)
            }

//            val test = DaVinCiExpression.stateListDrawable()
//                .shape(DaVinCiExpression.shape().stroke("1", "#ff653c").corner("2dp"))
//                .states(State.CHECKED_F, State.ENABLE_T)
//
//                .shape(DaVinCiExpression.shape().solid("#ff653c").corner("2dp,2dp,0,0"))
//                .states(State.CHECKED_T, State.ENABLE_T)
//                .shape(DaVinCiExpression.shape().solid("#ff653c").corner("2dp,2dp,2dp,2dp"))
//                .states(State.CHECKED_T, State.ENABLE_T)
//
//                .shape(DaVinCiExpression.shape().rectAngle().solid("#80ff3c08").corner("10dp")).states(State.ENABLE_F).toString()
//
//            Log.e("lmsg", test)

        }

        //这种方式不推荐使用，手写语法式很容易出错，但这是一个保留功能，日志将以此语法形式输出，
        binding.test2.setOnClickListener {
            Utils.timeCost("文本dsl创建") {
                it.daVinCiSld(
                    """
                    sld:[ 
                        shape:[ 
                            gradient:[ type:linear;startColor:#ff3c08;endColor:#353538 ];
                            st:[ Oval ];
                            state:[ ${State.ENABLE_T.name}|${State.PRESSED_T.name} ];
                            corners:[ 40dp ];
                            stroke:[ width:4dp;color:#000000 ]
                        ];
                        shape:[ 
                            gradient:[ type:linear;startColor:#ff3c08;endColor:#353538 ];
                            st:[ Oval ];
                            corners:[ 40dp ];
                            state:[ ${State.ENABLE_T.name} ];
                            stroke:[ width:4dp;color:rc/colorAccent ]
                        ];
                        shape:[ 
                            gradient:[ type:linear;startColor:#ff3c08;endColor:#353538 ];
                            st:[ Oval ];
                            state:[ ${State.ENABLE_F.name} ];
                            corners:[ 40dp ];
                            stroke:[ width:4dp;color:#000000 ]
                        ]
                    ]
                    """.trimIndent()
                )
            }
        }

        binding.tvTestFactory.setOnClickListener {
            binding.tvTestFactory2.isEnabled = !binding.tvTestFactory2.isEnabled
        }
    }
}

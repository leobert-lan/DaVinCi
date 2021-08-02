package com.example.simpletest

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.simpletest.databinding.ActivityMainBinding
import osp.leobert.android.davinci.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

//        //单纯测试下：binding.executePendingBindings() 是否会引起重复调用背景设置。如果构建参数和binding无关则不会
//        Log.e("lmsg", "1")
//
//        binding.a = "aaa"
//        binding.executePendingBindings()
//        Log.e("lmsg", "2")
//
//        binding.a = "bbb"
//        binding.executePendingBindings()
//        Log.e("lmsg", "3")

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
            it.daVinCi(
                normal = DaVinCiExpression.shape().oval()
                    .corner("40dp") //这个就没啥用了
                    .solid(resources.getColor(R.color.colorPrimaryDark))
                    .stroke(12, Color.parseColor("#26262a"))
            )

        }

        //这种方式不推荐使用，手写语法式很容易出错，但这是一个保留功能，日志将以此语法形式输出，
        binding.test2.setOnClickListener {
            it.daVinCiSld("""
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
            """.trimIndent())
        }

        binding.tvTestFactory.setOnClickListener {
            binding.tvTestFactory2.isEnabled = !binding.tvTestFactory2.isEnabled
        }
    }
}

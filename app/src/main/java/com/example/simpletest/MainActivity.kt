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
//        setContentView(R.layout.activity_main)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        Log.e("lmsg", "1")

        //单纯测试下是否会引起binding重复调用背景设置

        binding.a = "aaa"
        binding.executePendingBindings()
        Log.e("lmsg", "2")

        binding.a = "bbb"
        binding.executePendingBindings()
        Log.e("lmsg", "3")

        DaVinCiExpression.stateColor()
            .color("#e5332c").states(State.STATE_PRESSED_TRUE)
            .color("#667700").states(State.STATE_PRESSED_FALSE)
            .let {
                binding.test.daVinCiColor(it)
            }

        DaVinCiExpression.stateColor().apply(
            state = State.STATE_PRESSED_TRUE, color = Color.parseColor("#00aa00")
        ).apply(
            state = State.STATE_PRESSED_FALSE, color = Color.parseColor("#667700")
        ).apply(
            state = State.STATE_CHECKED_TRUE.name, color = "#ff0000"
        ).apply(
            state = State.STATE_CHECKED_FALSE, color = "#000000"
        )
            .let {
                binding.cb1.daVinCiColor(it)
            }


        binding.test.getTag(R.id.i1).let {
            Log.e("lmsg", (it ?: "null").toString())
        }


        binding.test.setOnClickListener(View.OnClickListener {
            it.daVinCi(
                normal = DaVinCiExpression.shape().oval()
                    .corner("40dp") //这个就没啥用了
                    .solid(resources.getColor(R.color.colorPrimaryDark))
                    .stroke(12, Color.parseColor("#26262a"))
            )

        })

        //这种不推荐使用啊，考虑语法解析一方面是考虑方便打印，另一方面是考虑到以后替换xml内的方案时，可以有后手
        binding.test2.setOnClickListener {
            it.daVinCiSld("""
                sld:[ 
                    shape:[ 
                        gradient:[ type:linear;startColor:#ff3c08;endColor:#353538 ];
                        st:[ Oval ];
                        corners:[ 40dp ];
                        state:[ ${State.STATE_ENABLE_TRUE.name} ];
                        stroke:[ width:4dp;color:rc/colorAccent ]
                    ];
                    shape:[ 
                        gradient:[ type:linear;startColor:#ff3c08;endColor:#353538 ];
                        st:[ Oval ];
                        state:[ ${State.STATE_ENABLE_FALSE.name} ];
                        corners:[ 40dp ];
                        stroke:[ width:4dp;color:#000000 ]
                    ]
                ]
            """.trimIndent())
        }

        binding.tvTestFactory.setOnTouchListener { v, event ->
            v.isEnabled = !v.isEnabled
            true
        }
    }
}

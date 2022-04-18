package com.example.simpletest

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.simpletest.databinding.ActivityMainBinding
import osp.leobert.android.davinci.*
import osp.leobert.android.davinci.Applier.Companion.applier
import osp.leobert.android.davinci.Applier.Companion.csl
import osp.leobert.android.davinci.Applier.Companion.drawable
import osp.leobert.android.davinci.Applier.Companion.viewBackground

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
            .color(Color.parseColor("#ff00ff")).states(State.PRESSED_F, State.CHECKED_T)
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
        binding.tvDemo3.setOnClickListener {
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

            val csl = "csl:[ sc:[ state:PRESSED_T;color:#e5332c ]; sc:[ state:PRESSED_F;color:#667700 ] ]"
            Utils.timeCost("文本DSL创建 csl") {
                binding.tvDemo3.daVinCiColor(csl)
            }
        }

        binding.tvTestFactory.setOnClickListener {
            binding.tvTestFactory2.isEnabled = !binding.tvTestFactory2.isEnabled
        }
    }

    fun demoOfApplier() {
        val view = findViewById<TextView>(R.id.test2)

        val dsl = """ sld:[ 
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

        //内置策略
        DaVinCi.of(dsl, view.viewBackground())
            .applySld(DaVinCiExpression.StateListDrawable.of(false))

        DaVinCi.of(null, view.csl())
            .applyCsl(
                DaVinCiExpression.stateColor()
                    .color("#e5332c").states(State.PRESSED_T)
                    .color("#667700").states(State.PRESSED_F)
            )

        DaVinCi.of(null, view.applier())
            .applyCsl(
                DaVinCiExpression.stateColor()
                    .color("#e5332c").states(State.PRESSED_T)
                    .color("#667700").states(State.PRESSED_F)
            )

        //自由使用csl
        val daVinCi = DaVinCi.of(null, Applier.csl(this) {
            //自由使用
        })
        val cslExp = DaVinCiExpression.stateColor()
            .color("#e5332c").states(State.PRESSED_T)
            .color("#667700").states(State.PRESSED_F)

        daVinCi.applyCsl(cslExp)
        daVinCi.applyCsl(cslExp, onComplete = {
            //onComplete
        })


        //自由使用shape、sld
        val daVinCi2 = DaVinCi.of(null, Applier.drawable(this) {
            //自由使用
        })
        //shape 或者 stateListDrawable
        val drawableExp = DaVinCiExpression.shape().oval()
            .corner("40dp") //这个就没啥用了
            .solid(resources.getColor(R.color.colorPrimaryDark))
            .stroke(12, Color.parseColor("#26262a"))

        //如果为 stateListDrawable 则使用 applySld
        daVinCi2.applyShape(drawableExp)
        daVinCi.applyShape(drawableExp, onComplete = {
            //onComplete
        })

        //以上两者虽然相对简单，但存在一处缺陷：无法使用tag语法，没有实现从View中查询tag，如果需要使用View中的tag，则可以如下：

        val daVinCi3 = DaVinCi.of(null, Applier.ViewComposer(view)
            .drawable {
                //自由使用drawable
            }.csl {
                //自由使用ColorStateList
            }
        )

    }
}

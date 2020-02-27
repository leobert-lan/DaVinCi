package com.example.simpletest

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import osp.leobert.android.davinci.daVinCi
import com.example.simpletest.databinding.ActivityMainBinding
import osp.leobert.android.davinci.DaVinCiExpression

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


        binding.test.getTag(R.id.i1).let {
            Log.e("lmsg", (it ?: "null").toString())
        }

        binding.test.setOnClickListener {
            it.daVinCi(
                normal = DaVinCiExpression.shape().oval()
                    .corner("40dp") //这个就没啥用了
                    .solid(resources.getColor(R.color.colorPrimaryDark))
                    .stroke(12,Color.parseColor("#26262a"))
            )
        }

        //这种不推荐使用啊，考虑语法解析一方面是考虑方便打印，另一方面是考虑到以后替换xml内的方案时，可以有后手
        binding.test2.setOnClickListener {
            it.daVinCi("shape:[ gradient:[ type:linear;startColor:#ff3c08;endColor:#353538 ]; st:[ Oval ]; corners:[ 40dp ]; stroke:[ width:4dp;color:rc/colorAccent ] ]")
        }
    }
}

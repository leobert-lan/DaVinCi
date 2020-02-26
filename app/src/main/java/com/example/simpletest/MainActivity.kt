package com.example.simpletest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import osp.leobert.android.davinci.daVinCi
import com.example.simpletest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        Log.e("lmsg", "1")

        binding.a = "aaa"
        binding.executePendingBindings()
        Log.e("lmsg", "2")

        binding.a = "bbb"
        binding.executePendingBindings()
        Log.e("lmsg", "3")


        binding.test.getTag(R.id.i1).let {
            Log.e("lmsg", (it ?: "null").toString())
        }

//        binding.test.setOnClickListener {
//            it.daVinCi("shape:[ st:[ Oval ]; solid:[ #e5332c ]; corners:[ 40dp ]; stroke:[ width:4dp;color:#26262a; ] ]")
//        }

        binding.test2.setOnClickListener {
            it.daVinCi("shape:[ gradient:[ type:linear;startColor:#ff3c08;endColor:#353538 ]; st:[ Oval ]; corners:[ 40dp ]; stroke:[ width:4dp;color:rc/colorAccent ] ]")
        }
    }
}

package com.example.simpletest

import android.util.Log

/**
 * <p><b>Package:</b> com.example.simpletest </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> Utils </p>
 * <p><b>Description:</b> Utils </p>
 * Created by leobert on 2021/11/8.
 */
object Utils {
    inline fun <T> timeCost(info: String, job: () -> T): T {
        val nanoStart = System.nanoTime()
        val t: T = job()
        val cost = System.nanoTime() - nanoStart
        Log.d("davinci-timecost", "$info cost: $cost ns, about ${cost / 1000_000} ms")
        return t
    }
}
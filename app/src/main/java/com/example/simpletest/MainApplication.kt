package com.example.simpletest

import android.app.Application
import android.util.Log
import com.example.simpletest.AppDaVinCiStylePreviewInjector
import com.example.simpletest.AppDaVinCiStyles

/**
 * <p><b>Package:</b> com.example.simpletest </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> MainApplication </p>
 * Created by leobert on 2021/6/2.
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val nano1 = System.nanoTime()
        AppDaVinCiStyles.register()
        val cost = System.nanoTime() - nano1
        AppDaVinCiStylePreviewInjector.register()

        Log.d("davinci-app", "register styles cost: $cost ns, about ${cost / 1000_000} ms")


//        StyleRegistry.registerFactory(DemoStyleFactory())
    }
}
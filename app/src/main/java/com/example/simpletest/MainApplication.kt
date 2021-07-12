package com.example.simpletest

import android.app.Application
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
        AppDaVinCiStyles.register()
        AppDaVinCiStylePreviewInjector.register()


//        StyleRegistry.registerFactory(DemoStyleFactory())
    }
}
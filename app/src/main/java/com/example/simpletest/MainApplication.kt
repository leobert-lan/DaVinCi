package com.example.simpletest

import android.app.Application
import osp.leobert.android.davinci.DaVinCi

/**
 * <p><b>Package:</b> com.example.simpletest </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> MainApplication </p>
 * Created by leobert on 2021/6/2.
 */
class MainApplication : Application() {

    companion object {
        init {
            DaVinCi.fastLoad()
        }
    }

    override fun onCreate() {
        super.onCreate()

        Utils.timeCost("AppDaVinCiStyles.register()") {
            AppDaVinCiStyles.register()
        }

        AppDaVinCiStylePreviewInjector.register()

//        StyleRegistry.registerFactory(DemoStyleFactory())
    }
}
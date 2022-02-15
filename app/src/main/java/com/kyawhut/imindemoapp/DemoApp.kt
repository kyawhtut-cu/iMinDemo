package com.kyawhut.imindemoapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * @author kyawhtut
 * @date 2/14/22
 */
@HiltAndroidApp
class DemoApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}

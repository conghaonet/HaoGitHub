package com.app2m.github

import android.content.res.Configuration
import android.support.multidex.MultiDexApplication
import android.os.StrictMode
import com.app2m.github.network.GithubInit


class MyApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        GithubInit.init(this.applicationContext)
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }
}
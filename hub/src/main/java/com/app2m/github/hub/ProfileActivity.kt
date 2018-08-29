package com.app2m.github.hub

import android.os.Bundle
import com.app2m.github.hub.base.BaseActivity
import org.jetbrains.anko.AnkoLogger

class ProfileActivity: BaseActivity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)
    }
}
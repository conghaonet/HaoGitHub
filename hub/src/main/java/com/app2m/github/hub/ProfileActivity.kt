package com.app2m.github.hub

import android.os.Bundle
import com.app2m.github.hub.base.BaseActivity
import com.app2m.github.hub.ui.ProfileUI
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.setContentView

class ProfileActivity: BaseActivity(), AnkoLogger {
    private val profileUI = ProfileUI()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.profile)
        profileUI.setContentView(this)
        profileUI.refresh()
    }
}
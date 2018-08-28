package com.app2m.github.hub.ui

import android.view.Gravity
import android.view.View
import com.app2m.github.hub.HomeActivity
import com.app2m.github.hub.R
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class HomeHeaderUI : AnkoComponent<HomeActivity>, AnkoLogger {
    override fun createView(ui: AnkoContext<HomeActivity>): View {
        return ui.ctx.verticalLayout {
            lparams(matchParent, wrapContent)
            backgroundColor = 0x3300FF00.toInt()
            gravity = Gravity.CENTER_HORIZONTAL

            button(R.string.hub_menu_login_button) {
                onClick {
                    ui.owner.openLoginActivity()
                    //手动控制drawerLayout的显示/隐藏
                    ui.owner.homeActivityUI.drawerLayout.closeDrawer(Gravity.START)
                }
            }.lparams(wrapContent, wrapContent)
            textView {
                text = "Header View"
            }.lparams(wrapContent, wrapContent)
        }
    }
}
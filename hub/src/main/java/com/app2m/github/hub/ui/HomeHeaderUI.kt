package com.app2m.github.hub.ui

import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.app2m.github.hub.GlideApp
import com.app2m.github.hub.HomeActivity
import com.app2m.github.hub.R
import com.app2m.github.network.PrefProperty
import com.app2m.github.network.Preference
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class HomeHeaderUI: AnkoComponent<HomeActivity>, AnkoLogger {
    private lateinit var loginButton: Button
    private lateinit var avatarImg: ImageView
    override fun createView(ui: AnkoContext<HomeActivity>): View {
        return ui.ctx.verticalLayout {
            lparams(matchParent, wrapContent)
            backgroundColor = 0x3300FF00.toInt()
            gravity = Gravity.CENTER_HORIZONTAL
            loginButton = button(R.string.hub_menu_login_button) {
                onClick {
                    ui.owner.openLoginActivity()
                    //手动控制drawerLayout的显示/隐藏
                    ui.owner.homeActivityUI.drawerLayout.closeDrawer(Gravity.START)
                }
            }.lparams(wrapContent, wrapContent)
            avatarImg = imageView {
                visibility = View.GONE
            }.lparams(wrapContent, wrapContent)
            textView {
                text = "Header View"
            }.lparams(wrapContent, wrapContent)
        }
    }
    fun refresh() {
        var prefTokenAuth : String by Preference(avatarImg.context, PrefProperty.AUTH_TOKEN, "")
        if (prefTokenAuth.isEmpty()) {
            avatarImg.visibility = View.GONE
        } else {
            avatarImg.visibility = View.VISIBLE
            avatarImg.imageResource = R.drawable.abc_ic_ab_back_material
            var prefAvatar : String by Preference(avatarImg.context, PrefProperty.USER_AVATAR,  "")
            GlideApp.with(avatarImg).load(prefAvatar).optionalCircleCrop().circleCrop().placeholder(R.drawable.abc_btn_radio_material).into(avatarImg)
        }
    }
}
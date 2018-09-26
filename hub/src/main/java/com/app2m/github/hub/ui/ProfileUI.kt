package com.app2m.github.hub.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.design.widget.AppBarLayout
import android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
import android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
import android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.view.GravityCompat
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.app2m.github.hub.GlideApp
import com.app2m.github.hub.ProfileActivity
import com.app2m.github.hub.R
import com.app2m.github.hub.ext.themeSupportToolbar
import com.app2m.github.network.PrefProperty
import com.app2m.github.network.Preference
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import org.jetbrains.anko.*
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.collapsingToolbarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.support.v4.nestedScrollView
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import android.os.Build
import android.graphics.drawable.BitmapDrawable
import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import com.app2m.github.hub.glide.BackgroundViewTarget
import org.jetbrains.anko.design.themedCoordinatorLayout


class ProfileUI: AnkoComponent<ProfileActivity>, AnkoLogger {
    lateinit var headerLayout : ViewGroup
    lateinit var avatarView : ImageView
    lateinit var tvUsername : TextView
    lateinit var toolbar : Toolbar
    lateinit var context : Context
    private val headerStyle = { view : Any ->
        when(view) {
            is TextView -> with(view) {
                textColorResource = R.color.colorTitle
            }
        }
    }
    override fun createView(ui: AnkoContext<ProfileActivity>) = ui.apply {
        context = ctx
        coordinatorLayout {
            lparams(matchParent, matchParent)
            fitsSystemWindows = true
            appBarLayout {
                fitsSystemWindows = true
                collapsingToolbarLayout {
                    fitsSystemWindows = true
                    title = ""
                    setCollapsedTitleTextAppearance(R.style.CollapsingLayoutCollapsedTitle)
                    setExpandedTitleTextAppearance(R.style.CollapsingLayoutExpandedTitle)
                    setContentScrimResource(R.color.colorPrimary)
                    collapsedTitleGravity = GravityCompat.START
                    expandedTitleGravity = GravityCompat.END or Gravity.BOTTOM
                    headerLayout = frameLayout {
                        view {
                            backgroundColor = 0x99000000.toInt()
                        }.lparams(matchParent, matchParent)
                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            avatarView = imageView {
                                scaleType = ImageView.ScaleType.CENTER_CROP
                            }.lparams(dip(64), dip(64))
                            tvUsername = textView {
                            }.lparams(wrapContent, wrapContent)
                        }.lparams(matchParent, matchParent)
                    }.lparams(matchParent, dip(150)) {
                        collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
                        parallaxMultiplier = 0.5f
                    }.applyRecursively(headerStyle)
                    toolbar = themeSupportToolbar(R.style.MyToolbarStyle) {
                        fitsSystemWindows = true
                    }.lparams(matchParent, dimenAttr(R.attr.actionBarSize)) {
                        collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN
                    }
                }.lparams(matchParent, matchParent) {
                    scrollFlags = SCROLL_FLAG_SCROLL or SCROLL_FLAG_ENTER_ALWAYS or SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED
                }
            }.lparams(matchParent, wrapContent)
            nestedScrollView {
                fitsSystemWindows = true
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    button("button 1") {

                    }.lparams(matchParent,dip(150))
                    button("button 2") {

                    }.lparams(matchParent, dip(150))
                    button("button 3") {

                    }.lparams(matchParent, dip(150))
                    button("button 4") {

                    }.lparams(matchParent, dip(150))
                    button("button 5") {

                    }.lparams(matchParent, dip(150))
                    button("button 6") {

                    }.lparams(matchParent, dip(150))
                }.lparams(matchParent, matchParent)
            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }
    }.view
    fun refresh() {
        val prefTokenAuth : String by Preference(context, PrefProperty.AUTH_TOKEN, "")
        val prefUsername : String by Preference(context, PrefProperty.USERNAME, "")
        val prefAvatar by Preference(context, PrefProperty.USER_AVATAR, "")
        if (prefTokenAuth.isNotEmpty()) {
            toolbar.title = prefUsername
            tvUsername.text = prefUsername
            GlideApp.with(context).load(prefAvatar).centerCrop().into(BackgroundViewTarget(headerLayout))


        }


    }

}
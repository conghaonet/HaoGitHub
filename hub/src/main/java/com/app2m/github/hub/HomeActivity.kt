package com.app2m.github.hub

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Gravity
import com.app2m.github.hub.adapter.MenuItemAdapter
import com.app2m.github.hub.ext.supportToolbar
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.drawerLayout

class HomeActivity: AppCompatActivity() {
    val menuItems = listOf("A","B","C")
    private lateinit var homeActivityUI: HomeActivityUI
    lateinit var menuAdapter: MenuItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.hub_activity_main)
        menuAdapter = MenuItemAdapter(this, menuItems)
        homeActivityUI = HomeActivityUI()
        homeActivityUI.setContentView(this)
        setSupportActionBar(homeActivityUI.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}

class HomeActivityUI : AnkoComponent<HomeActivity>, AnkoLogger {
    lateinit var toolbar : Toolbar
    override fun createView(ui: AnkoContext<HomeActivity>) = with(ui) {
        drawerLayout {
            lparams(width = matchParent, height = matchParent)
            verticalLayout {
                toolbar = supportToolbar {
                    //设置左间距为0，否则左边始终会有间距
                    contentInsetStartWithNavigation = dip(0)
                    title = ""
                    backgroundColorResource = R.color.colorPrimary
                    textView {
                        var myLayoutParams = Toolbar.LayoutParams(wrapContent, wrapContent)
                        myLayoutParams.gravity = Gravity.CENTER
                        layoutParams = myLayoutParams
                        text = "自定义标题"
                        textSize = 18f
                        textColor = 0xFFFFFFFF.toInt()
                    }
                }.lparams(width = matchParent, height = dip(50))
                textView {
                    text = "content layout"
                }.lparams(width = wrapContent, height = wrapContent)
            }.lparams(width = matchParent, height = matchParent)
            listView {
                backgroundColor = Color.WHITE
                adapter = owner.menuAdapter
            }.lparams(width = dip(240), height = matchParent) {
                gravity = Gravity.START
            }
        }
    }

}
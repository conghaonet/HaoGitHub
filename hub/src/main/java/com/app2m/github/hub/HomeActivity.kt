package com.app2m.github.hub

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.app2m.github.hub.adapter.MenuItemAdapter
import com.app2m.github.hub.ext.supportToolbar
import kotlinx.android.synthetic.main.design_navigation_item_header.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design._BottomNavigationView
import org.jetbrains.anko.design.navigationView
import org.jetbrains.anko.support.v4.drawerLayout

class HomeActivity: AppCompatActivity() {
    val menuItems = listOf("A","B","C")
    private lateinit var homeActivityUI: HomeActivityUI
    lateinit var mDrawerToggle : ActionBarDrawerToggle
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

        /*设置Drawerlayout的开关,并且和Home图标联动*/
        mDrawerToggle = ActionBarDrawerToggle(this, homeActivityUI.drawerLayout, homeActivityUI.toolbar, 0, 0)
        homeActivityUI.drawerLayout.addDrawerListener(mDrawerToggle)
        /*同步drawerlayout的状态*/
        mDrawerToggle.syncState()

        //手动控制drawerLayout的显示/隐藏
//        homeActivityUI.drawerLayout.closeDrawer(Gravity.START)
//        homeActivityUI.drawerLayout.openDrawer(Gravity.START)

        menuAdapter.setOnItemClickListener(object : MenuItemAdapter.ItemClickListener {
            override fun onClickItem(position: Int) {
                toast("position = $position")
                homeActivityUI.drawerLayout.closeDrawer(Gravity.START)
            }
        })

        homeActivityUI.navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_camera,R.id.nav_gallery  -> {
                    this@HomeActivity.toast("${it.title}")
                }
                else -> this@HomeActivity.toast("else")
            }
            homeActivityUI.drawerLayout.closeDrawer(Gravity.START)
            true
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toast("onPostCreate")
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toast("onConfigurationChanged")
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        toast("onCreateOptionsMenu")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        toast("onOptionsItemSelected")
        if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        toast("onPrepareOptionsMenu")
        return super.onPrepareOptionsMenu(menu)
    }

}

class HomeActivityUI : AnkoComponent<HomeActivity>, AnkoLogger {
    lateinit var toolbar : Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    private fun getHeaderView(view : View) : View {
        return view.context.verticalLayout {
            view.lparams(width = matchParent, height = wrapContent)
            backgroundColor = 0x3300FF00.toInt()
            gravity = Gravity.CENTER_HORIZONTAL
            button("hello") {

            }.lparams(width = wrapContent, height = wrapContent)
            textView {
                text = "Header View"
            }.lparams(width = wrapContent, height = wrapContent)
        }
    }
    override fun createView(ui: AnkoContext<HomeActivity>) = ui.apply {
        drawerLayout = drawerLayout {
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
                    textSize = 24f
                }.lparams(width = wrapContent, height = wrapContent)
            }.lparams(width = matchParent, height = matchParent)
            navigationView = navigationView {
                fitsSystemWindows = true
//                inflateHeaderView(R.layout.hub_home_header)
                addHeaderView(getHeaderView(this@navigationView))
                inflateMenu(R.menu.hub_home_menu)


            }.lparams(width = dip(240), height = matchParent) {
                gravity = Gravity.START
            }
/*
            //已改为用navigationView实现侧边栏
            listView {
                backgroundColor = Color.WHITE
                adapter = owner.menuAdapter
            }.lparams(width = dip(240), height = matchParent) {
                gravity = Gravity.START
            }
*/
        }
    }.view

}
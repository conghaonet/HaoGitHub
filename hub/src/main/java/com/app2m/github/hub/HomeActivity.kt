package com.app2m.github.hub

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.*
import com.app2m.github.hub.base.BaseActivity
import com.app2m.github.hub.ext.themeSupportToolbar
import com.app2m.github.hub.ui.HomeHeaderUI
import com.app2m.github.network.GitHubService
import com.app2m.github.network.RequestClient
import com.app2m.github.network.schedule
import org.jetbrains.anko.*
import org.jetbrains.anko.design.navigationView
import org.jetbrains.anko.support.v4.drawerLayout
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import org.jetbrains.anko.sdk25.coroutines.onClick

class HomeActivity: BaseActivity() {
    val homeActivityUI = HomeActivityUI()
    private lateinit var mDrawerToggle : ActionBarDrawerToggle
    private val mCompositeDisposable = CompositeDisposable()
    private var disposable: Disposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        homeActivityUI.navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_camera,R.id.nav_gallery  -> {
                    homeActivityUI.headerUI.refresh()
                    this@HomeActivity.toast("${it.title}")
                }
                else -> this@HomeActivity.toast("else")
            }
            homeActivityUI.drawerLayout.closeDrawer(Gravity.START)
            true
        }

    }
    fun requestGitHubApi() {
        val apiService = RequestClient.buildService(GitHubService::class.java)
        disposable = apiService.getGitHubApi().onBackpressureDrop().schedule().subscribeBy(
                onNext = {
                    toast(it.toString())
                },
                onError =  {
                    it.printStackTrace()
                }
        )
        mCompositeDisposable.add(disposable!!)
    }

    fun openLoginActivity() {
        startActivity<LoginActivity>()
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

    override fun onDestroy() {
        mCompositeDisposable.dispose()
        super.onDestroy()
    }
}

class HomeActivityUI : AnkoComponent<HomeActivity>, AnkoLogger {
    lateinit var toolbar : Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    var headerUI = HomeHeaderUI()
    override fun createView(ui: AnkoContext<HomeActivity>) = ui.apply {
        drawerLayout = drawerLayout {
            lparams(width = matchParent, height = matchParent)
            verticalLayout {
                toolbar = themeSupportToolbar(R.style.MyToolbarStyle) {
                    // 设置toolbar的title与左边导航图标之间的间距为0，否则左边始终会有间距，
                    // 或者在dimens.xml中定义：<dimen name="abc_action_bar_content_inset_with_nav" tools:override="true">0dp</dimen>
                    // contentInsetStartWithNavigation = dip(0)
                    backgroundColorResource = R.color.colorPrimary
                    title = ""
                    textView {
                        var myLayoutParams = Toolbar.LayoutParams(wrapContent, wrapContent)
                        myLayoutParams.gravity = Gravity.CENTER
                        layoutParams = myLayoutParams
                        textResource = R.string.app_name
                        textSize = 18f
                        textColor = 0xFFFFFFFF.toInt()
                    }
                }.lparams(matchParent, dimenAttr(R.attr.actionBarSize))
                textView {
                    text = "content layout"
                    textSize = 24f
                }
                button {
                    text = "test github api"
                    onClick {
                        owner.requestGitHubApi()
                    }
                }
                button {
                    text = "goto profile activity"
                    onClick {
                        startActivity<ProfileActivity>()
                    }
                }
            }.lparams(width = matchParent, height = matchParent)
            navigationView = navigationView {
                fitsSystemWindows = true
                addHeaderView(headerUI.createView(this@apply))
                inflateMenu(R.menu.hub_home_menu)
            }.lparams(width = dip(240), height = matchParent) {
                gravity = Gravity.START
            }
        }
    }.view

}
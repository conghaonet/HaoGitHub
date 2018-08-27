package com.app2m.github

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.Menu
import android.widget.Button
import com.app2m.github.hub.HomeActivity
import com.app2m.github.hub.ext.themeSupportToolbar
import com.app2m.github.network.schedule
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.nestedScrollView

class MainActivity : AppCompatActivity(), AnkoLogger {
    private val mUI : MainActivityUI<MainActivity> by lazy {
        MainActivityUI<MainActivity>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUI.setContentView(this)
        setSupportActionBar(mUI.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        startActivity<HomeActivity>()
    }

    fun tryObservable() {
        Observable.create(ObservableOnSubscribe<Int> {
            var i = 0
            while (true) {
                i++
                it.onNext(i)
                info("发送---->$i")
            }
//            it.onComplete()
        }).schedule().subscribeBy(
                onError = {
                    info (it.getStackTraceString())
                },
                onNext = {
                    Thread.sleep(50)
                    info("接收====>$it")
                },
                onComplete = {
                    info("onComplete")
                }
        )
    }
    fun tryFlowable() {
        Flowable.create(FlowableOnSubscribe<Int> {
            var i = 0
            while (i<500) {
                i++
                it.onNext(i)
            }
            it.onComplete()
        }, BackpressureStrategy.DROP).schedule().subscribeBy(
                onError = {
                    info (it.getStackTraceString())
                },
                onNext = {
                    info("接收====>$it")
                },
                onComplete = {
                    info("onComplete")
                }
        )
    }

    //设置menu（右边图标）
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //解析menu布局文件到menu
        menuInflater.inflate(R.menu.hub_home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        toast("onPrepareOptionsMenu")
        return super.onPrepareOptionsMenu(menu)
    }
}

class MainActivityUI<T> : AnkoComponent<T>, AnkoLogger {
    lateinit var toolbar: Toolbar
    private val customStyle = { view : Any ->
        when(view) {
            is Button -> {
                view.allCaps = false
                view.width = matchParent
                view.height = wrapContent
            }
        }
    }
    override fun createView(ui: AnkoContext<T>) = with(ui){
        verticalLayout {
            lparams(matchParent, matchParent)
            toolbar = themeSupportToolbar(R.style.MyToolbarStyle) {
                title = "Toolbar"
                subtitle = "Subtitle"
//                backgroundColor = 0xff3F51B5.toInt()
                backgroundColorResource = R.color.colorPrimary
                textView {
                    var myLayoutParams = Toolbar.LayoutParams(wrapContent, wrapContent)
                    myLayoutParams.gravity = Gravity.CENTER
                    layoutParams = myLayoutParams
                    text = "自定义标题居中"
                    textSize = 18f
                    textColor = 0xFFFFFFFF.toInt()
                }
            }.lparams(matchParent)
            nestedScrollView {
                lparams(width = matchParent, height = matchParent)
                verticalLayout {
                    button("GitHub Home") {
                        onClick {
                            startActivity<HomeActivity>()
                        }
                    }
                    button("Try Observable") {
                        onClick {
                            (owner as MainActivity).tryObservable()
                        }
                    }
                    button("Try Flowable") {
                        onClick {
                            (owner as MainActivity).tryFlowable()
                        }
                    }
                }
            }
        }.applyRecursively(customStyle)
    }

}
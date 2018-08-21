package com.app2m.github

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.app2m.github.hub.HomeActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.nestedScrollView

class MainActivity : AppCompatActivity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityUI<MainActivity>().setContentView(this)
    }
}

class MainActivityUI<T> : AnkoComponent<T>, AnkoLogger {
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
        nestedScrollView {
            lparams(width = matchParent, height = matchParent)
            verticalLayout {
                button("GitHub Home") {
                    onClick {
                        startActivity<HomeActivity>()
                    }
                }
            }
        }.applyRecursively(customStyle)
    }

}
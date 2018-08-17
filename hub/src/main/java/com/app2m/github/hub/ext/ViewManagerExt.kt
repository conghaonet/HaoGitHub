package com.app2m.github.hub.ext

import android.support.v7.widget.Toolbar
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

inline fun ViewManager.supportToolbar() = supportToolbar {}
/**
 * SwipyRefreshLayout.()：T.()->Unit 和 ()->Unit 的区别
 * https://www.jianshu.com/p/88a656e59c61
 */
inline fun ViewManager.supportToolbar(init: Toolbar.() -> Unit): Toolbar {
    return ankoView({ Toolbar(it) }, theme = 0, init = init)
}

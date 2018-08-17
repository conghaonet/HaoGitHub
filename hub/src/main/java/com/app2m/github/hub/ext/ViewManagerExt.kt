package com.app2m.github.hub.ext

import android.support.v7.widget.Toolbar
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

/**
 * https://stackoverflow.com/questions/40604446/what-are-the-possible-values-that-can-be-given-to-suppress-in-kotlin
 */
@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.supportToolbar(): Toolbar = supportToolbar() {}
/**
 * SwipyRefreshLayout.()：T.()->Unit 和 ()->Unit 的区别
 * https://www.jianshu.com/p/88a656e59c61
 */
inline fun ViewManager.supportToolbar(init: Toolbar.() -> Unit): Toolbar {
    return ankoView({ Toolbar(it) }, theme = 0, init = init)
}

package com.app2m.github.hub.ext

import android.support.v7.widget.Toolbar
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

/**
 * https://stackoverflow.com/questions/40604446/what-are-the-possible-values-that-can-be-given-to-suppress-in-kotlin
 */
@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.supportToolbar(): Toolbar = supportToolbar() {}
inline fun ViewManager.supportToolbar(init: Toolbar.() -> Unit): Toolbar {
    return ankoView({ Toolbar(it) }, theme = 0, init = init)
}

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.themeSupportToolbar(theme: Int = 0): Toolbar = themeSupportToolbar(theme) {}
inline fun ViewManager.themeSupportToolbar(theme: Int = 0, init: Toolbar.() -> Unit): Toolbar {
    return ankoView({ Toolbar(it) }, theme, init = init)
}

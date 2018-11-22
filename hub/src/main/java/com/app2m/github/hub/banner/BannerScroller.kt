package com.app2m.github.hub.banner

import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller

class BannerScroller : Scroller {
    var myDuration = 1000
    constructor(context: Context) : super(context)
    constructor(context: Context, interpolator: Interpolator): super(context, interpolator)

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, myDuration)
    }
    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy, myDuration)
    }

}
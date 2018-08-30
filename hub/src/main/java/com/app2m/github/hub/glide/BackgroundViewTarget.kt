package com.app2m.github.hub.glide

import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition

class BackgroundViewTarget(view: View) : CustomViewTarget<View, Drawable>(view) {
    override fun onResourceCleared(placeholder: Drawable?) {
        view.background = placeholder
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        view.background = resource
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        view.background = errorDrawable
    }

}
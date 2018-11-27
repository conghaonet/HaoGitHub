package com.app2m.github.hub.banner

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.app2m.github.hub.R
import org.jetbrains.anko.imageResource
import java.lang.ref.WeakReference

class BannerIndicator @JvmOverloads constructor(val container: RelativeLayout, var size: Int=0) {
    private val weakContext: WeakReference<Context> = WeakReference(container.context)
    private val indicatorLayout = FrameLayout(container.context)
    private var selectedIndicator: ImageView? = null
    init {
        val indicatorLayoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        indicatorLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
        indicatorLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        indicatorLayoutParams.bottomMargin = dp2px(container.context, 8f)

        val unselectedLayout = LinearLayout(container.context)
        unselectedLayout.orientation = LinearLayout.HORIZONTAL
        val unselectedLayoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        unselectedLayoutParams.gravity = Gravity.START or Gravity.CENTER_HORIZONTAL

        createUnselectedIndicator(unselectedLayout)
        indicatorLayout.addView(unselectedLayout, unselectedLayoutParams)
        if (size > 0) {
            createSelectedIndicator()
        }
        container.addView(indicatorLayout, indicatorLayoutParams)
    }

    private fun createUnselectedIndicator(unselectedContainer: LinearLayout) {
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        for (i in 0 until size) {
            val image = ImageView(unselectedContainer.context)
            image.imageResource = R.drawable.banner_indicator_unselected
            unselectedContainer.addView(image, layoutParams)
        }
    }
    private fun createSelectedIndicator() {
        val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.START or Gravity.CENTER_HORIZONTAL
        selectedIndicator = ImageView(indicatorLayout.context)
        selectedIndicator!!.imageResource = R.drawable.banner_indicator_selected
        indicatorLayout.addView(selectedIndicator, layoutParams)
    }

    fun moveSelectedIndicator(distanceRatio: Float) {
        if (weakContext.get() != null) {
            selectedIndicator?.let {
                val distance = distanceRatio * it.width
                if(distance <=  (size - 1) * it.width && distance >= 0) {
                    val layoutParams = it.layoutParams as FrameLayout.LayoutParams
                    layoutParams.leftMargin = distance.toInt()
                    indicatorLayout.updateViewLayout(it, layoutParams)
                }
            }
        }
    }

    internal fun clear() {
        indicatorLayout.removeAllViews()
    }

    private fun dp2px(context: Context, dpValue: Float): Int{
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}
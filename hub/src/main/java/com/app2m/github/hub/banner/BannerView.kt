package com.app2m.github.hub.banner

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.RelativeLayout
import com.app2m.github.network.schedule
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.parcel.Parcelize
import java.lang.ref.WeakReference
import java.lang.reflect.Field
import java.util.concurrent.TimeUnit

private const val TAG ="BannerView"
private const val SCROLLER_DURATION = 500
class BannerView: RelativeLayout {
    private val weakContext: WeakReference<Context> = WeakReference(context)
    private var disposable : Disposable? = null
    var isLoop = true
    private val bannerScroller: BannerScroller
    private val scrollerField: Field by lazy {
        val field = ViewPager::class.java.getDeclaredField("mScroller")
        field.isAccessible = true
        field
    }

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr, defStyleRes)
    init {
        bannerScroller = BannerScroller(context, AccelerateInterpolator())
        bannerScroller.myDuration = SCROLLER_DURATION
    }

    fun setItems(items: List<String>) : BannerItemAdapter {
        disposable?.dispose()
        removeAllViews()
        val viewPager = ViewPager(context)
        val viewPagerLayoutParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        scrollerField.set(viewPager, bannerScroller)
        this.addView(viewPager, viewPagerLayoutParams)

        val adapter = BannerItemAdapter(items, isLoop)
        viewPager.adapter = adapter

        if(isLoop && items.size > 1) {
            viewPager.currentItem = 1
        }
        val indicator = BannerIndicator(this, items.size)
        adapter.notifyDataSetChanged()
        viewPager.addOnPageChangeListener(MyOnPageChangeListener(viewPager, adapter, indicator))
        return adapter
    }

    private inner class MyOnPageChangeListener(val viewPager: ViewPager, val adapter: BannerItemAdapter, val indicator: BannerIndicator): ViewPager.OnPageChangeListener {
        init {
            disposable = null
        }
        override fun onPageSelected(position: Int) {
            Log.d(TAG, "onPageSelected    position=$position")
        }
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            Log.d(TAG, "onPageScrolled    position=$position  positionOffsetPixels=$positionOffsetPixels")
            if(isLoop && adapter.items.size > 1) {
                if(disposable != null) {
                    //手动滑动时，停止自动滚动banner
                    disposable!!.dispose()
                } else {
                    //bannerView初始加载时，开始自动滚动
                    disposable = startAutoPlay()
                }
                if(position > 0 && position < adapter.items.size-1) {
                    val indicatorPosition = position - 1
                    val distanceRatio = (positionOffsetPixels + (indicatorPosition * viewPager.width)).toFloat() / viewPager.width.toFloat()
                    indicator.moveSelectedIndicator(distanceRatio)
                }
            } else {
                val distanceRatio = (positionOffsetPixels + (position * viewPager.width)).toFloat() / viewPager.width.toFloat()
                indicator.moveSelectedIndicator(distanceRatio)
            }
        }

        /**
         * @param state 0:什么都没做; 1:开始滑动; 2:结束滑动;
         */
        override fun onPageScrollStateChanged(state: Int) {
            Log.d(TAG, "onPageScrollStateChanged    state=$state")
            if (isLoop && adapter.items.size > 1) {
                if (state == 0) {
                    if (viewPager.currentItem==0) {
                        viewPager.setCurrentItem(adapter.items.size - 2, false)
                    } else if(viewPager.currentItem == adapter.items.size - 1) {
                        viewPager.setCurrentItem(1, false)
                    }
                    disposable = startAutoPlay()
                }
            }
        }
        private fun startAutoPlay() : Disposable {
            return Observable.interval(3,3, TimeUnit.SECONDS).take(1).schedule().subscribeBy(
                    onNext = {
                        weakContext.get()?.let {
                            var currentIndex = viewPager.currentItem
                            if(++currentIndex < adapter.count) {
                                viewPager.setCurrentItem(currentIndex, true)
                            }
                        }
                    })
        }
    }

}
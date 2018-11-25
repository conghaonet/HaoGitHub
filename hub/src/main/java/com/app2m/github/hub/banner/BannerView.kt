package com.app2m.github.hub.banner

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.RelativeLayout
import android.widget.Scroller
import android.widget.Toast
import com.app2m.github.hub.R
import com.app2m.github.network.schedule
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.banner_item.view.*
import java.lang.ref.WeakReference
import java.lang.reflect.Field
import java.util.concurrent.TimeUnit

private const val TAG ="BannerView"
private const val SCROLLER_DURATION = 500
class BannerView: RelativeLayout {
    private var weakReference: WeakReference<Context> = WeakReference(context)
    private var viewPager: ViewPager = ViewPager(context)
    private var adapter = BannerItemAdapter()
    private var disposable : Disposable? = null
    var isLoop = true
    private val bannerScroller: BannerScroller by lazy {
        BannerScroller(context, AccelerateInterpolator())
    }
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
        val viewPagerLayoutParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        viewPager.addOnPageChangeListener(MyOnPageChangeListener())
        this.viewPager.adapter = adapter
        bannerScroller.myDuration = SCROLLER_DURATION
        scrollerField.set(viewPager, bannerScroller)
        this.addView(viewPager, viewPagerLayoutParams)
    }

    fun setItems(items: List<BannerItem>) {
        adapter.items.clear()
        setLogicPosition(items)
        adapter.items.addAll(items)

        if(isLoop && items.size > 1) {
            adapter.items.add(0, items[items.size -1])
            adapter.items.add(items[0])
            viewPager.currentItem = 1
        }
        adapter.notifyDataSetChanged()

    }

    private fun setLogicPosition(items: List<BannerItem>) {
        for (i in items.indices) {
            items[i].logicPosition = i
        }
    }

    private fun startAutoPlay() : Disposable {
        return Observable.interval(3,3, TimeUnit.SECONDS).take(1).schedule().subscribeBy(
                onNext = {
                    var currentIndex = viewPager.currentItem
                    if(++currentIndex < adapter.count) {
                        viewPager.setCurrentItem(currentIndex, true)
                    }
                }, onError = {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        })
    }

    private inner class MyOnPageChangeListener: ViewPager.OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            Log.d(TAG, "onPageSelected    position=$position")
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            Log.d(TAG, "onPageScrolled    position=$position  positionOffsetPixels=$positionOffsetPixels")
            if(disposable != null) {
                disposable!!.dispose()

            } else {
                disposable = startAutoPlay()
            }
/*
            if (position==0 && positionOffsetPixels==0) {
                viewPager.setCurrentItem(adapter.items.size - 2, false)
            } else if(position == adapter.items.size - 1 && positionOffsetPixels == 0) {
                viewPager.setCurrentItem(1, false)
            }
*/
        }

        override fun onPageScrollStateChanged(state: Int) {
            Log.d(TAG, "onPageScrollStateChanged    state=$state")
            if (state == 0) {
                if (viewPager.currentItem==0) {
                    viewPager.setCurrentItem(adapter.items.size - 2, false)
                } else if(viewPager.currentItem == adapter.items.size - 1) {
                    viewPager.setCurrentItem(1, false)
                }
                this@BannerView.disposable = startAutoPlay()
            }
        }
    }

    private inner class BannerItemAdapter: PagerAdapter {
        val items: MutableList<BannerItem>

        constructor(): this(listOf())
        constructor(items: List<String>) {
            if (items.isNullOrEmpty()) {
                this.items = mutableListOf()
            } else {
                this.items = items as MutableList<BannerItem>
            }
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            var view = View.inflate(context, R.layout.banner_item, null)
            view.banner_text.text = items[position].bannerUrl
            view.setOnClickListener{
                Toast.makeText(context, "${it.banner_text.text} position=$position", Toast.LENGTH_SHORT).show()
            }
            container.addView(view)
            return view
        }

        override fun isViewFromObject(view: View, any: Any): Boolean {
            return view == any
        }

        override fun getCount(): Int {
            return this.items.size
        }

        override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
            // super.destroyItem(container,position,object); 这一句要删除，否则报错
            container.removeView(any as View)
        }
    }

    class BannerItem {
        var bannerUrl: String
        var actionUrl: String
        internal var logicPosition: Int
        @JvmOverloads
        constructor(bannerUrl: String = "", actionUrl: String = "", logicPosition: Int = -1) {
            this.bannerUrl = bannerUrl
            this.actionUrl = actionUrl
            this.logicPosition = logicPosition
        }
    }
}
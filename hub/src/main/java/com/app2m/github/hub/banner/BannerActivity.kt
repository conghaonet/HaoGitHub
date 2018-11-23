package com.app2m.github.hub.banner

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import com.app2m.github.hub.R
import com.app2m.github.hub.databinding.ActivityBannerBinding
import kotlinx.android.synthetic.main.banner_item.view.*
import java.lang.reflect.Field

class BannerActivity : AppCompatActivity() {
    private val mBinding: ActivityBannerBinding by lazy {
        DataBindingUtil.setContentView<ActivityBannerBinding>(this, R.layout.activity_banner)
    }
    private val bannerScroller: BannerScroller by lazy {
        BannerScroller(this, AccelerateInterpolator())
    }
    private val scrollerField: Field by lazy {
        val field = ViewPager::class.java.getDeclaredField("mScroller")
        field.isAccessible = true
        field
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_banner)
        val items = listOf(
                "A    http://betacs.101.com/v0.1/download?dentryId=eb1c5ad9-a42e-41a1-865f-0cd2e5240dd2",
                "B    http://betacs.101.com/v0.1/download?dentryId=f21c1f21-31e9-4ae6-a465-9dfd11c4a0b4",
                "C    http://betacs.101.com/v0.1/download?dentryId=7c82155a-d046-43f0-a8a6-e7325b3a171f",
                "D    http://betacs.101.com/v0.1/download?dentryId=539e7151-9caf-486b-8990-21ee82ff5d8c",
                "E    http://betacs.101.com/v0.1/download?dentryId=173d0877-f334-416e-94fd-6d19c5e90674",
                "F    http://betacs.101.com/v0.1/download?dentryId=df7b9c15-89a3-4b26-be80-053cc04b0366")

        var adapter = MyPagerAdapter(this, items as MutableList<String>)
        mBinding.vp.adapter = adapter
//        scrollerField.set(mBinding.vp, bannerScroller)

        var bannerItems = mutableListOf<BannerView.BannerItem>()
        for (item in items) {
            var bannerItem = BannerView.BannerItem(item)
            bannerItems.add(bannerItem)
        }
        mBinding.bannerView.setItems(bannerItems)

    }

    inner class MyPagerAdapter(val context: Context, var items: MutableList<String>): PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
//            return super.instantiateItem(container, position)
            var view = View.inflate(context, R.layout.banner_item, null)
            view.banner_text.text = "index = $position"
            view.setOnClickListener{
                Toast.makeText(context, "index=$position", Toast.LENGTH_SHORT).show()
                if ((position + 1) < items.size) {
                    bannerScroller.myDuration = 500
                    (container as ViewPager).setCurrentItem(position + 1, true)
                } else {
                    bannerScroller.myDuration = 0
                    (container as ViewPager).setCurrentItem(0, false)
//                    this.notifyDataSetChanged()
                }
            }
            container.addView(view)
            return view
        }
        override fun getCount(): Int {
            return items.size
        }

        override fun isViewFromObject(view: View, any: Any): Boolean {
            return view == any
        }

        override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
            // super.destroyItem(container,position,object); 这一句要删除，否则报错
            container.removeView(any as View)
        }

    }
}

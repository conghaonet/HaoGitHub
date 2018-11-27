package com.app2m.github.hub.banner

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.app2m.github.hub.R
import kotlinx.android.synthetic.main.banner_item.view.*

class BannerItemAdapter: PagerAdapter {
    val items: MutableList<BannerView.BannerItem>

    constructor(): this(listOf())
    constructor(items: List<BannerView.BannerItem>) {
        if (items.isNullOrEmpty()) {
            this.items = mutableListOf()
        } else {
            this.items = items as MutableList<BannerView.BannerItem>
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = View.inflate(container.context, R.layout.banner_item, null)
        view?.let {
            it.banner_text?.text = items[position].bannerUrl
            itemClickListener?.let { listener ->
                it.setOnClickListener{
                    listener.onItemClick(position)
                }
            }
        }
        container.addView(view)
        return view!!
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

    var itemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

}

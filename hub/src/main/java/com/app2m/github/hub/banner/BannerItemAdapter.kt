package com.app2m.github.hub.banner

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.app2m.github.hub.R
import kotlinx.android.synthetic.main.banner_item.view.*

class BannerItemAdapter(items: List<String>, private val isLoop: Boolean): PagerAdapter() {
    val items = mutableListOf<String>()
    init {
        if (items.isNotEmpty()) {
            this.items.addAll(items)
            if(isLoop && this.items.size > 1) {
                this.items.add(0, items[items.size -1])
                this.items.add(items[0])
            }
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = View.inflate(container.context, R.layout.banner_item, null)
        view?.let {
            it.banner_text?.text = items[position]
            itemClickListener?.let { listener ->
                it.setOnClickListener{
                    if(isLoop && items.size > 1) {
                        listener.onItemClick(position - 1)
                    } else {
                        listener.onItemClick(position)
                    }
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

    private var itemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

}

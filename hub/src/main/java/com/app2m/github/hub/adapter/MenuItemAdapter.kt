package com.app2m.github.hub.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.jetbrains.anko.*

class MenuItemAdapter(private val ctx: Context, private val items: List<String>) : BaseAdapter() {
    private val ankoContext = AnkoContext.createReusable(ctx, this)

    override fun getCount() : Int = items.size

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getItem(position: Int): String {
        return items[position]
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        lateinit var viewHolder : ViewHolder
        var view : View? = convertView
        if(view == null) {
            val itemUI = MenuItemUI()
            view = itemUI.createView(ankoContext)
            viewHolder = ViewHolder()
            viewHolder.itemLayout = itemUI.itemLayout
            viewHolder.menuName = itemUI.name
            view.tag = viewHolder
        }
        viewHolder = view.tag as ViewHolder
        viewHolder.menuName?.text = getItem(position)
        viewHolder.itemLayout?.setOnClickListener {
            itemClickListener?.onClickItem(position)
        }
        return view
    }

    inner class ViewHolder {
        var itemLayout : ViewGroup? = null
        var menuName : TextView? = null
    }

    fun setOnItemClickListener(listener: ItemClickListener) {
        itemClickListener = listener
    }
    private var itemClickListener: ItemClickListener? = null
    interface ItemClickListener {
        fun onClickItem(position: Int)
    }
}

class MenuItemUI : AnkoComponent<MenuItemAdapter>, AnkoLogger {
    lateinit var itemLayout : ViewGroup
    lateinit var name: TextView
    override fun createView(ui: AnkoContext<MenuItemAdapter>) = ui.apply {
        itemLayout = linearLayout {
            orientation = LinearLayout.HORIZONTAL
            var myLayoutParams : AbsListView.LayoutParams = AbsListView.LayoutParams(matchParent, wrapContent)
            layoutParams = myLayoutParams
            backgroundColor = 0xFFFFFFFF.toInt()
            isClickable = true
            name = textView {
                text ="menu name"
                textSize = 18f
                textColor = Color.BLUE
            }
        }
    }.view
}
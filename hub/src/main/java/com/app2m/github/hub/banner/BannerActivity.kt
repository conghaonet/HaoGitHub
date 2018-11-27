package com.app2m.github.hub.banner

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.app2m.github.hub.R
import com.app2m.github.hub.databinding.ActivityBannerBinding

class BannerActivity : AppCompatActivity() {
    private val mBinding: ActivityBannerBinding by lazy {
        DataBindingUtil.setContentView<ActivityBannerBinding>(this, R.layout.activity_banner)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
/*
        val items = listOf(
                "A    http://betacs.101.com/v0.1/download?dentryId=eb1c5ad9-a42e-41a1-865f-0cd2e5240dd2",
                "B    http://betacs.101.com/v0.1/download?dentryId=f21c1f21-31e9-4ae6-a465-9dfd11c4a0b4",
                "C    http://betacs.101.com/v0.1/download?dentryId=7c82155a-d046-43f0-a8a6-e7325b3a171f",
                "D    http://betacs.101.com/v0.1/download?dentryId=539e7151-9caf-486b-8990-21ee82ff5d8c",
                "E    http://betacs.101.com/v0.1/download?dentryId=173d0877-f334-416e-94fd-6d19c5e90674",
                "F    http://betacs.101.com/v0.1/download?dentryId=df7b9c15-89a3-4b26-be80-053cc04b0366")
*/

        mBinding.bannerView.isLoop = true
        setBannerData(mutableListOf("A","B","C","D"))
        mBinding.changeItems.setOnClickListener {
            if(it.tag == null) {
                it.tag = "abc"
                setBannerData(mutableListOf("a","b","c"))
            } else {
                it.tag = null
                setBannerData(mutableListOf("dd", "ee", "ff", "gg", "hh"))
            }
        }
    }
    private fun setBannerData(data: List<String>) {
        mBinding.bannerView.setItems(data).setOnItemClickListener(object : BannerItemAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(this@BannerActivity, "position = $position", Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onBackPressed() {
        finish()
    }
}

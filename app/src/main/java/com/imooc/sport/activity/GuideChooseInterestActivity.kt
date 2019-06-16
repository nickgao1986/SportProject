package com.imooc.sport.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.imooc.sport.R
import com.imooc.sport.base.BaseActivity
import com.nick.apps.pregnancy11.mypedometer.fragment.adapter.GuideItemAdapter
import com.nick.apps.pregnancy11.mypedometer.fragment.decoration.SpaceItemDecoration1
import com.nick.apps.pregnancy11.mypedometer.fragment.model.GuideImageModel
import java.util.*

class GuideChooseInterestActivity : BaseActivity() {

    private var mRecyclerView: RecyclerView? = null
    var array = intArrayOf(
        R.drawable.pedo_interesting_choose1,
        R.drawable.pedo_interesting_choose2,
        R.drawable.pedo_interesting_choose3,
        R.drawable.pedo_interesting_choose4,
        R.drawable.pedo_interesting_choose5,
        R.drawable.pedo_interesting_choose6,
        R.drawable.pedo_interesting_choose7,
        R.drawable.pedo_interesting_choose8,
        R.drawable.pedo_interesting_choose9
    )
    var strArray = arrayOf("健身", "跑步", "高效塑性", "科学饮食", "骑行", "篮球", "体态改善", "健康小习惯", "马拉松")

    override fun getTitleString(): Any {
        return ""
    }

    override fun getContentView(): Int {
        return R.layout.pedo_welcome_guide1_layout
    }


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleRecycleView()
        val title = findViewById<View>(R.id.title) as TextView
        title.typeface = Typeface.createFromAsset(mContext.assets, "fonts/PingFang-SC-Semibold.ttf")

        val btn = findViewById<View>(R.id.btn) as TextView
        btn.setOnClickListener { startActivity(Intent(this@GuideChooseInterestActivity, LoginActivity::class.java)) }


    }


    private fun handleRecycleView() {
        mRecyclerView = findViewById<View>(R.id.rv_list) as RecyclerView
        val layoutManager = GridLayoutManager(mContext, 3)
        mRecyclerView!!.layoutManager = layoutManager

        val list = ArrayList<GuideImageModel>()
        for (i in 0..8) {
            list.add(GuideImageModel(array[i], strArray[i]))
        }
        val adapter = GuideItemAdapter(R.layout.pedo_layout_dynamic_image_item, list)
        mRecyclerView!!.adapter = adapter

        val spacing = dip2px(7) // 50px

        mRecyclerView!!.addItemDecoration(SpaceItemDecoration1(spacing))

    }

    fun dip2px(size: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, size.toFloat(), mContext.resources
                .displayMetrics
        ).toInt()
    }

}

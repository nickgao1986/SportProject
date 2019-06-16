package com.imooc.sport.activity

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import com.imooc.sport.R
import com.imooc.sport.base.BaseActivity
import com.nick.apps.pregnancy11.mypedometer.fragment.adapter.MatchItemAdapter
import com.nick.apps.pregnancy11.mypedometer.fragment.decoration.SpaceItemDecoration
import com.nick.apps.pregnancy11.mypedometer.fragment.model.MatchItem
import com.youth.banner.Banner
import java.util.*

class MatchActivity : BaseActivity() {
    private var mRecyclerView: RecyclerView? = null

    override fun getContentView(): Int {
        return R.layout.match_fragment_layout
    }

    override fun getTitleString(): Any? {
        return null
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleRecycleView()
    }

    fun dip2px(size: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, size.toFloat(), mContext.resources
                .displayMetrics
        ).toInt()
    }

    private fun handleRecycleView() {
        mRecyclerView = findViewById<View>(R.id.rv_list) as RecyclerView
        val layoutManager = GridLayoutManager(mContext, 2)
        mRecyclerView!!.layoutManager = layoutManager

        val list = ArrayList<MatchItem>()
        val adapter = MatchItemAdapter(R.layout.pedo_match_adapter_item, list)
        mRecyclerView!!.adapter = adapter
        val headerView =
            mContext.layoutInflater.inflate(R.layout.match_header_layout, mRecyclerView!!.parent as ViewGroup, false)

        val spanCount = 2 // 3 columns
        val spacing = dip2px(6) // 50px
        val includeEdge = false
        mRecyclerView!!.addItemDecoration(SpaceItemDecoration(spacing))

        adapter.addHeaderView(headerView)

        val banner = headerView.findViewById<View>(R.id.banner) as Banner
        //设置图片加载器
        banner.setImageLoader(GlideImageLoader())
        val images = ArrayList<String>()
        // String[] images={"file:///android_asset/pedo_banner_icon1.png"};
        images.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=4128213686,1490868641&fm=26&gp=0.jpg")
        images.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2544063423,3641037535&fm=26&gp=0.jpg")
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559789963231&di=77b9c839ade44c826a7c90c2e5c81b99&imgtype=0&src=http%3A%2F%2F01img.mopimg.cn%2Fmobile%2F20180108%2F20180108074316_9650ed8640892cad200b4ea822a6f519_2.jpeg")

        //设置图片集合
        banner.setImages(images)
        //banner设置方法全部调用完毕时最后调用
        banner.start()
    }
}

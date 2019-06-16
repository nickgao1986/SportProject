package com.imooc.sport.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import cn.jzvd.JZVideoPlayer
import cn.jzvd.JZVideoPlayerStandard
import com.chad.library.adapter.base.BaseQuickAdapter
import com.imooc.sport.R
import com.imooc.sport.base.BaseActivity
import com.nick.apps.pregnancy11.mypedometer.fragment.CardModuleFolderView
import com.nick.apps.pregnancy11.mypedometer.fragment.adapter.DetailItemAdapter
import com.nick.apps.pregnancy11.mypedometer.fragment.helper.JZVideoPlayerStandardControl
import com.nick.apps.pregnancy11.mypedometer.fragment.model.ReplyItem
import com.nick.apps.pregnancy11.mypedometer.fragment.util.LogUtil
import com.nick.apps.pregnancy11.mypedometer.fragment.util.ScreenUtil
import java.util.*

class VideoDetailActivity : BaseActivity() {

    private var bottom_layout: View? = null
    private var comment: TextView? = null
    internal lateinit var videoControl: JZVideoPlayerStandardControl
    private val currentAction: Int = 0
    override fun getTitleString(): Any? {
        return null
    }

    override fun getContentView(): Int {
        return R.layout.detail_fragment_layout
    }


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleRecycleView()
        comment = findViewById<View>(R.id.comment) as TextView
        val topic_list_right_container = findViewById<View>(R.id.topic_list_right_container) as LinearLayout
        topic_list_right_container.setOnClickListener { PedoCommentActivity.startActivity(this@VideoDetailActivity) }
    }

    override fun onPause() {
        super.onPause()
        JZVideoPlayer.setJzUserAction(null)
        JZVideoPlayer.releaseAllVideos()
        JZVideoPlayerStandardControl.setOnClickFullScreenListener(null)
        JZVideoPlayerStandardControl.releaseAllVideos()
    }

    override fun onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return
        }
        super.onBackPressed()
    }


    private fun handleRecycleView() {
        bottom_layout = findViewById(R.id.bottom_layout)
        val mRecyclerView = findViewById<View>(R.id.rv_list) as RecyclerView
        val layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.layoutManager = layoutManager

        val list = ArrayList<ReplyItem>()
        for (i in 0..5)
            list.add(ReplyItem())
        val adapter = DetailItemAdapter(R.layout.pedo_reply_item_layout, list)
        mRecyclerView.adapter = adapter
        val headerView = mContext.layoutInflater.inflate(
            R.layout.pedo_video_detail_header_layout,
            mRecyclerView.parent as ViewGroup,
            false
        )

        videoControl = headerView.findViewById<View>(R.id.jc_video) as JZVideoPlayerStandardControl
        setupVideo()

        val mTextView = headerView.findViewById<View>(R.id.content) as CardModuleFolderView
        mTextView.setRealWidth(ScreenUtil.getScreenWidth(mContext) - ScreenUtil.dip2px(mContext, 32))
        mTextView.display(
            false,
            "有些婴儿从4个多月开始长牙；有些却到10个月开始牙；有些婴儿长牙时会哭闹，" +
                    "有些婴儿长牙期会发烧那么宝宝正常情况下是什么时候长牙，长几颗牙，" +
                    "以及长牙期间会有哪些不适呢？新爸爸新妈妈们在宝宝长牙期间又该如何护理好"
        )

        adapter.addHeaderView(headerView)
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position -> }

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val totalItem = layoutManager.itemCount
                LogUtil.d("scroll", "<<<<firstVisibleItemPosition $firstVisibleItemPosition")
                if (firstVisibleItemPosition > 0) {
                    //说明已经过了header,这时我们要把底部的布局显示出来
                    bottom_layout!!.visibility = View.VISIBLE
                } else {
                    bottom_layout!!.visibility = View.GONE
                }
            }
        })

    }

    private fun setupVideo() {
        val imageUrl =
            "http://v1-default.ixigua.com/8484d2e2a4468c4083b1b2f75e2cacc1/5d00b616/video/m/22099b16f1d10a5463684b14b2298233b2f11624af0e000024dae838c7b7/?rc=M3c5O2tlaGZybTMzZzczM0ApQHRAbzc3ODQ8MzUzMzU0NDQzNDVvQGg1dilAZzN3KUBmM3UpZHNyZ3lrdXJneXJseHdmNTNAaDFpZzQ1ZGBrXy0tYC0wc3MtbyNvIy80MC0uNS0uNDUyLzM2LTojbyM6YS1vIzpgLXAjOmB2aVxiZitgXmJmK15xbDojLi9e"
        videoControl.setUp(imageUrl, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "当年救自己出火海的消防员叔叔竟这么帅，女孩以拥抱表示感恩")
    }
}

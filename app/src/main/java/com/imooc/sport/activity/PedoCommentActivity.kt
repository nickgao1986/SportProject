package com.imooc.sport.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import com.chad.library.adapter.base.BaseQuickAdapter
import com.imooc.sport.R
import com.imooc.sport.base.BaseActivity
import com.nick.apps.pregnancy11.mypedometer.fragment.adapter.DetailItemAdapter
import com.nick.apps.pregnancy11.mypedometer.fragment.model.ReplyItem
import com.nick.apps.pregnancy11.mypedometer.fragment.util.ThemeDialogAnim
import java.util.*


class PedoCommentActivity : BaseActivity(), View.OnClickListener {


    private var mBottomLayout: ViewGroup? = null
    private var mDialogAnim: ThemeDialogAnim? = null
    private val content_layout: LinearLayout? = null
    internal var mDatas: ArrayList<Parcelable>? = null
    private val mSelectedList = ArrayList<String>()
    private val mJoinBtn: TextView? = null
    override fun getTitleString(): Any? {
        return null
    }

    override fun getContentView(): Int {
        return R.layout.pedo_comment_laout
    }


    fun setStatusBarColor(@ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = color
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        intent.getIntExtra(CARD_NUM, 1)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated() {
        super.onViewCreated()
        setStatusBarColor(Color.TRANSPARENT)

        mBottomLayout = findViewById<View>(R.id.theme_bottom_layout) as ViewGroup
        findViewById<View>(R.id.blank_view).setOnClickListener(this)
        findViewById<View>(R.id.close_view).setOnClickListener(this)

        mDialogAnim = ThemeDialogAnim()
        mDialogAnim!!.createAnim(mBottomLayout!!, null)
        handleRecycleView()


    }

    private fun handleRecycleView() {
        val mRecyclerView = findViewById<View>(R.id.rv_list) as RecyclerView
        val layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.layoutManager = layoutManager

        val list = ArrayList<ReplyItem>()
        for (i in 0..5)
            list.add(ReplyItem())
        val adapter = DetailItemAdapter(R.layout.pedo_reply_item_layout, list)
        mRecyclerView.adapter = adapter
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position -> }
    }


    public override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
    }


    protected fun setTipViewWhenNoData() {}

    override fun onClick(v: View) {
        if (v.id == R.id.close_view || v.id == R.id.blank_view) {
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun finish() {
        if (mDialogAnim != null) {
            mDialogAnim!!.finishAnim(mBottomLayout, object : ThemeDialogAnim.AnimationListenerAdapter() {
                override fun onAnimationEnd(animation: Animation) {
                    super@PedoCommentActivity.finish()
                    super@PedoCommentActivity.overridePendingTransition(0, 0)
                }
            })
        } else {
            super.finish()
            super.overridePendingTransition(0, 0)
        }
    }

    companion object {
        val CARD_NUM = "card_num"
        val GUIDE_LIST = "guidelist"

        fun startActivity(context: Context) {
            val intent = Intent()
            intent.setClass(context, PedoCommentActivity::class.java)
            context.startActivity(intent)
        }
    }


}

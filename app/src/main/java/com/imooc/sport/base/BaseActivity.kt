package com.imooc.sport.base

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.Button
import android.widget.RelativeLayout
import com.imooc.sport.R
import com.nick.apps.pregnancy11.mypedometer.fragment.base.PedoActionBar

abstract class BaseActivity : AppCompatActivity(), PedoActionBar.ActionBarListener {

    /**
     * 上下文
     */
    protected lateinit var mContext: Activity

    /**
     * 主布局
     */
    protected lateinit var mBaseFragment: ViewGroup
    /**
     * 内容布局
     */
    private lateinit var mContentLayout: ViewGroup

    protected lateinit var mActionBar: PedoActionBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        mBaseFragment = LayoutInflater.from(this).inflate(R.layout.pedo_activity_base, null) as ViewGroup
        mContentLayout = mBaseFragment.findViewById<View>(R.id.body) as RelativeLayout
        // 当取到的title为null或者为0 则不显示actionbar
        val stub = mBaseFragment.findViewById<View>(R.id.actionbar_title) as ViewStub
        if (titleString != null && titleString != 0) {
            mActionBar = stub.inflate() as PedoActionBar
            mActionBar.setTitle(titleString)
            // 左键监听
            mActionBar.leftButton.setOnClickListener { onLeftButtonClick() }
            // 右键监听
            mActionBar.rightButton.setOnClickListener { onRightButtonClick() }

            initLeftButton(mActionBar.leftButton)
            initRightButton(mActionBar.rightButton)
        }
        contentView = contentView
        onViewCreated()

    }


    override fun initLeftButton(leftButton: Button?) {
    }

    override fun initRightButton(rightButton: Button?) {
    }

    override fun onLeftButtonClick() {
    }

    override fun onRightButtonClick() {
    }

    open fun onViewCreated() {}

    override fun setContentView(view: View?) {
        if (view != null) {
            mBaseFragment.removeView(mContentLayout)
            mBaseFragment.addView(view, mContentLayout!!.layoutParams)
            mContentLayout = view as ViewGroup
        }
        super.setContentView(mBaseFragment)
    }


    /**
     * 重写此方法，实现设置新布局时可以同时带入actionbar
     *
     * @param layoutResID
     */
    override fun setContentView(layoutResID: Int) {
        if (layoutResID != 0) {
            val view = LayoutInflater.from(this).inflate(layoutResID, null)
            setContentView(view)
        } else {
            setContentView(null)
        }
    }


}

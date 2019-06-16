package com.imooc.sport.base

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


abstract class BaseFragment : Fragment() {

    /**
     * 全局activity
     */
    protected lateinit var mContext: FragmentActivity


//    abstract val contentView: Int

    val contentLayout: View?
        get() = null


    abstract fun getContentView(): Int

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = getActivity()!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (getContentView() != 0) {
            inflater.inflate(getContentView(), container, false)
        } else contentLayout
    }

}

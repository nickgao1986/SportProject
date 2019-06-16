package com.imooc.sport.fragment

import android.os.Bundle
import android.view.View
import com.imooc.sport.R
import com.imooc.sport.base.BaseFragment


class MeActivity : BaseFragment() {

    override fun getContentView(): Int {
        return R.layout.pedo_me_fragment_layout
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

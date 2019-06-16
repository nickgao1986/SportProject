package com.imooc.sport.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.widget.EditText
import android.widget.TextView

import com.imooc.sport.R
import com.imooc.sport.base.BaseActivity


class SearchActivity : BaseActivity() {

    private val ed_search: EditText? = null

    override fun onViewCreated() {
        super.onViewCreated()
        //ed_search = (EditText) findViewById(R.id.ed_search);
    }

    override fun getTitleString(): Any? {
        return null
    }

    override fun getContentView(): Int {
        return R.layout.pedo_search_layout
    }

    companion object {

        fun startActivity(context: Context, textView: TextView) {
            val intent = Intent()
            intent.setClass(context, SearchActivity::class.java)
            val compat = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, textView, "textView")
            ActivityCompat.startActivity(context, intent, compat.toBundle())
        }
    }
}

package com.imooc.sport.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.imooc.sport.R
import com.imooc.sport.base.BaseFragment
import com.nick.apps.pregnancy11.mypedometer.fragment.adapter.FragmentItemAdapter
import com.nick.apps.pregnancy11.mypedometer.fragment.model.FragmentItemBean
import java.util.*


class PedoClassifyListFragment : BaseFragment() {

    private val mGroupId: String? = null
    private var mTabId: String? = null
    private var mTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTabId = arguments!!.getString("tabId")
        mTitle = arguments!!.getString("title", "")
    }

    override fun getContentView(): Int {
        return R.layout.pedo_fragment_list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mRecyclerView = view.findViewById<View>(R.id.rv_list) as RecyclerView
        val layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.layoutManager = layoutManager
        val list = ArrayList<FragmentItemBean>()
        for (i in 0..11) {
            list.add(FragmentItemBean())
        }
        val adapter = FragmentItemAdapter(R.layout.pedo_fragment_item_layout, list, mContext)
        mRecyclerView.adapter = adapter
    }

    companion object {

        fun newInstance(tabId: String, title: String): PedoClassifyListFragment {
            val fragment = PedoClassifyListFragment()
            val bundle = Bundle()
            bundle.putString("tabId", tabId)
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }


}

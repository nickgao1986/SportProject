package com.imooc.sport.activity

import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import com.http.api.ApiListener
import com.http.api.ApiUtil
import com.imooc.sport.R
import com.imooc.sport.base.BaseActivity
import com.nick.apps.pregnancy11.mypedometer.fragment.adapter.BAFFragmentAdapter
import com.nick.apps.pregnancy11.mypedometer.fragment.adapter.PedoMoreAllClassfiyAdapter
import com.nick.apps.pregnancy11.mypedometer.fragment.api.CatApi
import com.nick.apps.pregnancy11.mypedometer.fragment.util.ScreenUtil.dip2px
import com.nick.apps.pregnancy11.mypedometer.fragment.view.TipView
import java.util.*


class PedoClassifyListActivity : BaseActivity(), View.OnClickListener {

    private var mTabParentLayout: View? = null
    private var mTabLayout: TabLayout? = null
    private var mViewPager: ViewPager? = null
    private var mTipView: TipView? = null
    private var mAdapter: BAFFragmentAdapter<*>? = null
    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()
    private var categoryGridView: GridView? = null
    private var rlMoreClassify: RelativeLayout? = null
    private lateinit var ivMoreClassify: ImageView
    var bTopMenuShowed: Boolean = false
    private var ll_special_category_content: LinearLayout? = null
    private var linearMenu: RelativeLayout? = null
    private var animation_out: Animation? = null
    private var animation_in: Animation? = null

    override fun onResume() {
        super.onResume()
        if (mViewPager != null && mViewPager!!.currentItem < mFragmentList.size) {
            val fragment = mFragmentList[mViewPager!!.currentItem]
            fragment.onHiddenChanged(false)
        }
    }

    override fun getTitleString(): Any {
        return "群详情"
    }

    override fun getContentView(): Int {
        return R.layout.pedo_classify_list
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        mTabParentLayout = findViewById(R.id.tab_fragment_list_layout)
        mTabLayout = findViewById<View>(R.id.tab_fragment_list_tabs) as TabLayout
        categoryGridView = findViewById<View>(R.id.special_gridview) as GridView
        ll_special_category_content = findViewById<View>(R.id.ll_special_category_content) as LinearLayout
        linearMenu = ll_special_category_content!!.findViewById<View>(R.id.rl_special_category_list) as RelativeLayout
        rlMoreClassify = findViewById<View>(R.id.rlMoreClassify) as RelativeLayout
        rlMoreClassify!!.setOnClickListener(this)
        mViewPager = findViewById<View>(R.id.tab_fragment_list_viewpager) as ViewPager
        mTipView = findViewById<View>(R.id.multi_expert_list_tipview) as TipView
        mAdapter = BAFFragmentAdapter(supportFragmentManager, mFragmentList, mFragmentTitleList)
        mViewPager!!.adapter = mAdapter
        mTipView!!.setClickListener(this)
        animation_in = AnimationUtils.loadAnimation(mContext, R.anim.menu_in)
        animation_out = AnimationUtils.loadAnimation(mContext, R.anim.menu_out)
        loadData()
    }

    fun reflex(tabLayout: TabLayout?) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout!!.post {
            try {
                //拿到tabLayout的mTabStrip属性
                val mTabStrip = tabLayout.getChildAt(0) as LinearLayout

                val dp90 = dip2px(tabLayout.context, 90)

                for (i in 0 until mTabStrip.childCount) {
                    val tabView = mTabStrip.getChildAt(i)

                    //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                    val mTextViewField = tabView.javaClass.getDeclaredField("mTextView")
                    mTextViewField.isAccessible = true

                    val mTextView = mTextViewField.get(tabView) as TextView

                    tabView.setPadding(0, 0, 0, 0)

                    //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                    var width = 0
                    width = mTextView.width
                    if (width == 0) {
                        mTextView.measure(0, 0)
                        width = mTextView.measuredWidth
                    }

                    //设置tab左右间距为tab宽度-textview宽度除以2  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                    val params = tabView.layoutParams as LinearLayout.LayoutParams
                    //params.width = width;
                    params.leftMargin = (tabView.measuredWidth - width) / 2
                    params.rightMargin = (tabView.measuredWidth - width) / 2
                    tabView.layoutParams = params

                    tabView.invalidate()
                }

            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }

    }

    private fun loadData() {
        mTipView!!.setLoadingData(true)
        CatApi().get(mContext, object : ApiListener {
            override fun success(api: ApiUtil) {
                if (isFinishing) {
                    return
                }
                val rsp = api as CatApi
                if (rsp.guideList.size == 0) {
                    return
                }
                mTipView!!.setLoadingData(false)
                mTipView!!.hideView()
                mTabParentLayout!!.visibility = View.VISIBLE
                val linkedHashMap = rsp.categoryTabs
                val dataSet = linkedHashMap.entries

                val tabTarget = 0
                for ((key, value) in dataSet) {
                    mFragmentList.add(PedoClassifyListFragment.newInstance(key, value))
                    mFragmentTitleList.add(value)
                }
                setGridView()
                mAdapter!!.notifyDataSetChanged()
                mTabLayout!!.setupWithViewPager(mViewPager)
                mTabLayout!!.setOnTabSelectedListener(object : TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
                    override fun onTabReselected(tab: TabLayout.Tab?) {
                        val title = tab!!.text
                        showPopMenu()
                        if (TextUtils.isEmpty(title)) return
                    }
                })
                reflex(mTabLayout)
                mViewPager!!.offscreenPageLimit = dataSet.size
                mViewPager!!.currentItem = tabTarget
            }

            override fun failure(apiBase: ApiUtil) {
                if (isFinishing) {
                    return
                }
                mTipView!!.setLoadingData(false)
            }
        })

    }


    private fun setGridView() {
        val classfiyAdapter = PedoMoreAllClassfiyAdapter(mContext, mFragmentTitleList)
        categoryGridView!!.adapter = classfiyAdapter
    }


    override fun onClick(v: View) {
        if (v.id == R.id.rlMoreClassify) {
            showPopMenu()
        } else {
            loadData()
        }
    }

    /**
     * 显示菜单 点击标题的时候
     */
    private fun showPopMenu() {
        if (bTopMenuShowed) {
            return
        }
        bTopMenuShowed = true

        val layoutParams = ll_special_category_content!!.layoutParams as RelativeLayout.LayoutParams
        val category_height = dip2px(mContext, 40)
        layoutParams.topMargin = category_height
        ll_special_category_content!!.requestLayout()
        //显示
        ll_special_category_content!!.visibility = View.VISIBLE
        downAnimation()
        //动画
        linearMenu!!.startAnimation(animation_in)
        //响应
        ll_special_category_content!!.setOnClickListener {
            //点击收缩
            hidePopMenu()
        }
    }

    fun hidePopMenu() {
        if (!bTopMenuShowed) {
            return
        }
        bTopMenuShowed = false

        upAnimation()
        animation_out!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                ll_special_category_content!!.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        linearMenu!!.startAnimation(animation_out)
    }


    /**
     * 按下箭头的动画
     */
    private fun downAnimation() {
       // ObjectAnimator.ofFloat(ivMoreClassify, "rotation", 45f, 90f).setDuration(500).start()
    }

    /**
     * 收回之后箭头的动画
     */
    private fun upAnimation() {
        ObjectAnimator.ofFloat(ivMoreClassify, "rotation", 45f, 90f).setDuration(500).start()
    }


}

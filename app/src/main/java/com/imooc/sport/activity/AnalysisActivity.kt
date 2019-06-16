package com.imooc.sport.activity

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.imooc.sport.R
import com.imooc.sport.base.BaseFragment
import com.nick.apps.pregnancy11.mypedometer.fragment.util.*
import com.nick.apps.pregnancy11.mypedometer.fragment.view.ButtonSelectView
import com.nick.apps.pregnancy11.mypedometer.fragment.view.PedoAnalysisPopup
import kotlinx.android.synthetic.main.pedo_weight_fragment_layout.*

class AnalysisActivity : BaseFragment(), ButtonSelectView.onButtonSelectClickListener {

    internal lateinit var mArray: Array<ButtonSelectView?>
    private var chart2: BarChart? = null
    private var chart: LineChart? = null
    private var mRightButton: ImageView? = null
    private var mIsOpen = false
    private var mRecordPopup: PedoAnalysisPopup? = null
    private var label1: TextView? = null
    private var tv_distance: TextView? = null
    private var label2: TextView? = null
    private var tv_distance1: TextView? = null

    override fun getContentView(): Int {
        return R.layout.pedo_weight_fragment_layout
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        switchTab(0)
    }

    private fun initView(view: View) {
        mArray = arrayOfNulls(4)
        mArray[0] = analysis_run_miles_layout
        mArray[1] = analysis_step_layout
        mArray[2] = analysis_calorie_layout
        mArray[3] = analysis_weight_layout
        analysis_run_miles_layout.setListenter(this)
        analysis_step_layout.setListenter(this)
        analysis_calorie_layout.setListenter(this)
        analysis_weight_layout.setListenter(this)

        label1 = view.findViewById<View>(R.id.label1) as TextView
        label2 = view.findViewById<View>(R.id.label2) as TextView
        tv_distance = view.findViewById<View>(R.id.tv_distance) as TextView
        tv_distance1 = view.findViewById<View>(R.id.tv_distance1) as TextView

        val tv_title = view.findViewById<View>(R.id.tv_title) as TextView
        tv_title.text = "分析"

        mRightButton = view.findViewById<View>(R.id.btn_right) as ImageView
        mRightButton!!.visibility = View.VISIBLE
        mRightButton!!.setOnClickListener {
            if (!mIsOpen) {
                rotatePlusButton(mContext, true)
                mRecordPopup = PedoAnalysisPopup(mContext)
                mRecordPopup!!.bindData(null)
                mRecordPopup!!.showAsDropDown(mRightButton, 0, ScreenUtil.dip2px(mContext, 6))
                mRecordPopup!!.setOnStepPopClickListener { view, showMode ->
                    if (showMode == StepConstants.MODE_SET_TARGET) {
                        ToastUtil.show(mContext, "set target")
                    } else if (showMode == StepConstants.MODE_SET_WEIGHT) {
                        ToastUtil.show(mContext, "set weight")
                    }
                }
                mRecordPopup!!.setOnDismissListener {
                    rotatePlusButton(mContext, false)
                    mIsOpen = false
                }
                mIsOpen = true
            } else {
                mRecordPopup!!.dismiss()
                rotatePlusButton(mContext, false)
                mIsOpen = false
            }
        }
    }

    fun rotatePlusButton(context: Context, isUp: Boolean) {
        var rotate: ObjectAnimator? = null
        if (isUp) {
            rotate = ObjectAnimator.ofFloat(mRightButton, "rotation", 0f, 45f).setDuration(100)
        } else {
            rotate = ObjectAnimator.ofFloat(mRightButton, "rotation", 45f, 0f).setDuration(100)
        }
        rotate!!.interpolator = BounceInterpolator()
        rotate.start()

    }

    private fun setSelect(index: Int) {
        for (i in mArray.indices) {
            val view = mArray[i]
            if (i == index) {
                view?.setSelect(true)
            } else {
                view?.setSelect(false)
            }
        }
    }

    private fun switchTab(index: Int) {
        if (index == 0) {
            chart!!.visibility = View.VISIBLE
            chart2!!.visibility = View.GONE
            LinearLineHelper.cal(chart!!, LinearLineHelper.HANDLE_MILES)
            label1!!.text = "每日里程(米)"
            label2!!.text = "7天总里程(米)"
        } else if (index == 1) {
            chart!!.visibility = View.VISIBLE
            chart2!!.visibility = View.GONE
            LinearLineHelper.cal(chart!!, LinearLineHelper.HANDLE_STEPS)
            label1!!.text = "今日步数"
            label2!!.text = "7天总步数"
        } else if (index == 2) {
            chart!!.visibility = View.GONE
            chart2!!.visibility = View.VISIBLE
            BarHelper.calChat2(chart2!!)
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.analysis_run_miles_layout) {
            setSelect(0)
            switchTab(0)
        } else if (view.id == R.id.analysis_step_layout) {
            setSelect(1)
            switchTab(1)
        } else if (view.id == R.id.analysis_calorie_layout) {
            setSelect(2)
            switchTab(2)
        } else if (view.id == R.id.analysis_weight_layout) {
            setSelect(3)
            switchTab(3)
        }
    }


    override fun onResume() {
        super.onResume()
    }


}

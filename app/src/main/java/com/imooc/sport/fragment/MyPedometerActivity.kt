package com.imooc.sport.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.imooc.sport.R
import com.imooc.sport.base.BaseFragment
import com.imooc.sport.database.AppDatabase
import com.nick.apps.pregnancy11.mypedometer.fragment.util.CalendarUtil
import com.nick.apps.pregnancy11.mypedometer.fragment.util.PedometerUtils
import com.nick.apps.pregnancy11.mypedometer.fragment.view.PedometerProgressBar
import kotlinx.android.synthetic.main.pedo_actionbar.*


class MyPedometerActivity : BaseFragment() {


    var mFootSteps: Long = 0
    private var magicProgressCircle: PedometerProgressBar? = null

    private var tv_calorie: TextView? = null
    private var tv_distance: TextView? = null
    private var tv_text_hint: TextView? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_title.text = "计步器"
        setCurrentStep()


    }

    override fun onResume() {
        super.onResume()
        //        ((BottomActivity)mContext).showTitle();
        //        ((BottomActivity)mContext).setTitle("计步器");
    }

    private fun setCurrentStep() {
//         StepData data =  AppDatabase.getDatabase(mContext).StepDao()
//                       .findByDate(CalendarUtil.getCurrentDate());
//               mFootSteps = data.step;
        var data = AppDatabase.get(mContext).StepDao().findByDate(CalendarUtil.getCurrentDate())
        //mFootSteps = data.step
        mFootSteps = 0
        updateProgressCircleDataDuringRefresh(mFootSteps, 5000)
    }

    fun updateProgressCircleDataDuringRefresh(stepSum: Long, target: Int) {
        if (magicProgressCircle != null) {
            magicProgressCircle!!.setCurrentCount(target, stepSum.toInt())

            val km = PedometerUtils.getDistanceByStep(stepSum)

            tv_distance!!.text = km

            val calorie = PedometerUtils.getCalorieByStepRealValue(mContext, stepSum)
            tv_calorie!!.text = calorie.toString()

            //1瓶500毫升的可乐含热量210卡路里
            var num = calorie / 210 + 1
            if (stepSum == 0L) {
                num = 0
            }
            val recommend = String.format(
                mContext.getString(R.string.pedo_main_hint_text), stepSum,
                num
            )

            tv_text_hint!!.text = recommend
            updateCalorie(stepSum)
        }
    }

    private fun updateCalorie(stepCounts: Long) {
        val calorie = PedometerUtils.getCalorieByStepRealValue(mContext, stepCounts)
        tv_calorie!!.text = calorie.toString()
    }

    fun updateDataWhenSensorChanged() {
        updateProgressCircleDataDuringRefresh(this.mFootSteps, 5000)
    }


    override fun onDestroy() {
        super.onDestroy()


    }


    //    @Override
    //    public Object getTitleString() {
    //        return "计步器";
    //    }

    override fun getContentView(): Int {
        return R.layout.pedometer_fragment_layout
    }

    companion object {

        fun startActivity(context: Context) {
            val intent = Intent()
            intent.setClass(context, MyPedometerActivity::class.java)
            context.startActivity(intent)
        }
    }
}

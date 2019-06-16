package com.imooc.sport.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.fourmob.datetimepicker.date.DatePickerDialogUtil
import com.imooc.sport.R
import com.imooc.sport.base.BaseActivity
import com.nick.apps.pregnancy11.mypedometer.fragment.util.CalendarUtil
import java.util.*

class SetBirthDayActivity : BaseActivity() {

    private var mDatePicker: DatePickerDialogUtil? = null
    private var mUserBirthday: Long = 0
    private var set_birth: TextView? = null
    private var gender1: TextView? = null
    private var gender2: TextView? = null

    override fun getTitleString(): Any {
        return ""
    }

    override fun getContentView(): Int {
        return R.layout.pedo_welcome_set_birth_layout
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = findViewById<View>(R.id.title) as TextView
        title.typeface = Typeface.createFromAsset(mContext.assets, "fonts/PingFang-SC-Semibold.ttf")

        val set_birth_text = findViewById<View>(R.id.set_birth_text) as TextView
        set_birth_text.typeface = Typeface.createFromAsset(mContext.assets, "fonts/PingFang-SC-Semibold.ttf")


        val btn = findViewById<View>(R.id.btn) as TextView
        btn.setOnClickListener {
            startActivity(
                Intent(
                    this@SetBirthDayActivity,
                    GuideChooseInterestActivity::class.java
                )
            )
        }

        gender1 = findViewById<View>(R.id.gender1) as TextView
        gender2 = findViewById<View>(R.id.gender2) as TextView

        gender1!!.setOnClickListener {
            gender1!!.isSelected = true
            gender2!!.isSelected = false
        }

        gender2!!.setOnClickListener {
            gender1!!.isSelected = false
            gender2!!.isSelected = true
        }


        set_birth = findViewById<View>(R.id.set_birth) as TextView
        set_birth!!.setOnClickListener { showSetBirth() }

    }

    private fun showSetBirth() {
        val minCalender = Calendar.getInstance(Locale.CHINA)
        val currentYear = minCalender.get(Calendar.YEAR)
        minCalender.set(currentYear - 20, 0, 1)
        val maxCalender = Calendar.getInstance(Locale.CHINA)
        maxCalender.set(currentYear - 12, 0, 1)
        val selectCalendar = Calendar.getInstance(Locale.CHINA)
        selectCalendar.set(currentYear - 26, 0, 1)

        if (mDatePicker == null) {
            mDatePicker = DatePickerDialogUtil(
                this,
                selectCalendar,
                DatePickerDialogUtil.ShowState.SHOW_DATE_AND_TEXT_ONLY,
                maxCalender,
                minCalender
            )
            mDatePicker!!.listener = object : DatePickerDialogUtil.OnDateChangeListener {
                override fun onDateChange(calendar: Calendar) {}

                override fun onConfirm(calendar: Calendar) {
                    mUserBirthday = calendar.timeInMillis
                    set_birth!!.text = CalendarUtil.format4(mUserBirthday)
                }
            }
        }
        mDatePicker!!.calendar = selectCalendar
        mDatePicker!!.show()
    }
}

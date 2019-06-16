package com.imooc.sport.activity

import android.graphics.Typeface
import android.os.Bundle
import com.fourmob.datetimepicker.date.NumberPicker
import com.fourmob.datetimepicker.date.TextPickerDialogUtil
import com.imooc.sport.R
import com.imooc.sport.base.BaseActivity
import kotlinx.android.synthetic.main.pedo_height_and_weight_layout.*
import java.util.*

class SetHeightAndWeightActivity : BaseActivity() {


    private var mHeight = 175
    private var mWeight = 70
    override fun getTitleString(): Any? {
        return null
    }

    override fun getContentView(): Int {
        return R.layout.pedo_height_and_weight_layout
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gender.typeface = Typeface.createFromAsset(mContext.assets, "fonts/PingFang-SC-Semibold.ttf")
        //
        weight.typeface = Typeface.createFromAsset(mContext.assets, "fonts/PingFang-SC-Semibold.ttf")


        set_height.setOnClickListener { showSetHeightDialog() }
        set_weight.setOnClickListener { showSetWeightDialog() }
    }

    private fun showSetHeightDialog() {
        val heightPickDialog = TextPickerDialogUtil(mContext)
        heightPickDialog.addItems(getItem(216, "", "cm", 57))
        heightPickDialog.listener = object : TextPickerDialogUtil.OnSelectChangeListener {
            override fun onDateChange(value: Array<Int>, changePicker: NumberPicker) {}

            override fun onConfirm(value: Array<Int>) {
                mHeight = value[0] + 57
                set_height.text = String.format(Locale.getDefault(), "%dcm", mHeight)
            }
        }

        heightPickDialog.pickers[0].value = mHeight - 57
        heightPickDialog.show(getString(R.string.set_height))
    }

    private fun showSetWeightDialog() {
        val weightPickDialog = TextPickerDialogUtil(mContext)
        weightPickDialog.addItems(getItem(146, "", " kg", 5))
        weightPickDialog.listener = object : TextPickerDialogUtil.OnSelectChangeListener {
            override fun onDateChange(value: Array<Int>, changePicker: NumberPicker) {}

            override fun onConfirm(value: Array<Int>) {
                mWeight = value[0] + 5
                set_weight!!.text = String.format(Locale.getDefault(), "%dkg", mWeight)
            }
        }

        weightPickDialog.pickers[0].value = mWeight - 5
        weightPickDialog.show(getString(R.string.set_weight))
    }

    private fun getItem(length: Int, startTag: String, endTag: String, start: Int): Array<String?> {
        val item = arrayOfNulls<String>(length)

        for (i in start until length + start) {
            item[i - start] = startTag + i + endTag
        }
        return item
    }
}

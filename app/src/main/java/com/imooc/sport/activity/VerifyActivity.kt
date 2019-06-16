package com.imooc.sport.activity

import android.content.Intent
import android.os.Bundle
import com.imooc.sport.R
import com.imooc.sport.base.BaseActivity
import com.nick.apps.pregnancy11.mypedometer.fragment.BottomActivity
import com.nick.apps.pregnancy11.mypedometer.fragment.view.VerifyCodeView

class VerifyActivity : BaseActivity() {

    override fun getTitleString(): Any {
        return ""
    }

    override fun getContentView(): Int {
        return R.layout.pedo_verify_code_layout
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val verify_code_view = findViewById(R.id.verify_code_view) as VerifyCodeView
        verify_code_view.setInputCompleteListener(object : VerifyCodeView.InputCompleteListener {
            override fun inputComplete() {
                startActivity(Intent(this@VerifyActivity, BottomActivity::class.java))
                finish()
            }

            override fun invalidContent() {

            }
        })
    }
}

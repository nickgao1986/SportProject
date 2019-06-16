package com.imooc.sport.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.imooc.sport.R
import com.imooc.sport.base.BaseActivity

class PhoneCodeLoginActivity : BaseActivity() {
    override fun getTitleString(): Any? {
        return null
    }

    override fun getContentView(): Int {
        return R.layout.pedo_phone_code_login_layout
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username_login = findViewById<View>(R.id.username_login) as TextView
        username_login.setOnClickListener { finish() }

        val btn = findViewById<View>(R.id.btn) as TextView
        btn.setOnClickListener { startActivity(Intent(this@PhoneCodeLoginActivity, VerifyActivity::class.java)) }
    }
}

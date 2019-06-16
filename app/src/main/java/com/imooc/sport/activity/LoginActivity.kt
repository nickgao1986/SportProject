package com.imooc.sport.activity

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.http.api.ApiListener
import com.http.api.ApiUtil
import com.imooc.sport.R
import com.imooc.sport.base.BaseActivity
import com.nick.apps.pregnancy11.mypedometer.fragment.BottomActivity
import com.nick.apps.pregnancy11.mypedometer.fragment.api.LoginApi
import com.nick.apps.pregnancy11.mypedometer.fragment.util.ScreenUtil
import com.nick.apps.pregnancy11.mypedometer.fragment.view.PedoUsernamePopup
import kotlinx.android.synthetic.main.pedo_login_layout.*


class LoginActivity : BaseActivity(), PedoUsernamePopup.OnStepPopClickListener {

    private val isPopUp = false
    override fun getTitleString(): Any {
        return "登录"
    }

    override fun getContentView(): Int {
        return R.layout.pedo_login_layout
    }


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        username_icon.setOnClickListener { test(username) }
        phone_login.setOnClickListener { startActivity(Intent(this@LoginActivity, PhoneCodeLoginActivity::class.java)) }

        btn.setOnClickListener {

            LoginApi("", "").post(object : ApiListener {
                override fun success(api: ApiUtil) {
                    startActivity(Intent(this@LoginActivity, BottomActivity::class.java))
                    this@LoginActivity.finish()
                }

                override fun failure(api: ApiUtil) {

                }
            })
        }
        val password = findViewById<View>(R.id.password) as EditText
        password.transformationMethod = PasswordTransformationMethod.getInstance()
        val password_icon = findViewById<View>(R.id.password_icon) as ImageView
        password_icon.setOnClickListener {
            password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            password.setSelection(password.text.length)
        }
    }

    private fun test(username: EditText) {
        val mRecordPopup = PedoUsernamePopup(mContext)
        mRecordPopup.bindData(null)
        mRecordPopup.setOnStepPopClickListener(this)
        mRecordPopup.showAsDropDown(username, 0, ScreenUtil.dip2px(mContext, 15))
    }

    override fun onStepPopClick(view: View, showMode: Int) {

    }


}

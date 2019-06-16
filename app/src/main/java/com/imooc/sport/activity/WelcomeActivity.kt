package com.imooc.sport.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.widget.ImageView
import com.imooc.sport.R
import com.imooc.sport.base.BaseActivity
import com.nick.apps.pregnancy11.mypedometer.fragment.util.SharedPreferencesUtil

class WelcomeActivity: BaseActivity() {


    internal lateinit var mIVEntry: ImageView

    override fun getTitleString(): Any? {
        return null
    }

    override fun getContentView(): Int {
        return R.layout.pedo_welcome_layout
    }

    override fun onViewCreated() {
        super.onViewCreated()
        val isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true)!!
        mIVEntry = findViewById(R.id.iv_entry)
        startAnim()
    }

    private fun startAnim() {

        val animatorX = ObjectAnimator.ofFloat(mIVEntry, "scaleX", 1f, SCALE_END)
        val animatorY = ObjectAnimator.ofFloat(mIVEntry, "scaleY", 1f, SCALE_END)

        val set = AnimatorSet()
        set.setDuration(ANIM_TIME.toLong()).play(animatorX).with(animatorY)
        set.start()

        set.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator) {
                startActivity(Intent(this@WelcomeActivity, SetAvatarActivity::class.java))
                this@WelcomeActivity.finish()
            }
        })
    }

    companion object {
        private val ANIM_TIME = 1000

        private val SCALE_END = 1.15f
    }


}
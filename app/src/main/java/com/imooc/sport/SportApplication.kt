package com.imooc.sport

import android.app.Application
import android.content.Context
import com.facebook.drawee.backends.pipeline.Fresco

class SportApplication : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        Fresco.initialize(this)
    }


}

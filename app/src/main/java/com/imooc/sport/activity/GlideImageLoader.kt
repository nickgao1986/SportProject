package com.imooc.sport.activity

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.facebook.drawee.view.SimpleDraweeView
import com.youth.banner.loader.ImageLoader

class GlideImageLoader : ImageLoader() {
    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        //用fresco加载图片简单用法，记得要写下面的createImageView方法
        val uri = Uri.parse(path as String)
        imageView.setImageURI(uri)
    }

    //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
    override fun createImageView(context: Context): ImageView {
        //使用fresco，需要创建它提供的ImageView，当然你也可以用自己自定义的具有图片加载功能的ImageView
        return SimpleDraweeView(context)
    }
}
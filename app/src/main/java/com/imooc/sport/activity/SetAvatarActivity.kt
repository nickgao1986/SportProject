package com.imooc.sport.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.image.CropImage
import com.image.PedoCropImageActivity
import com.imooc.sport.R
import com.imooc.sport.base.BaseActivity
import java.io.File
import java.text.SimpleDateFormat

class SetAvatarActivity : BaseActivity() {

    private var iv_set_avatar: SimpleDraweeView? = null
    var mFilePath = ""

    override fun getTitleString(): Any {
        return ""
    }

    override fun getContentView(): Int {
        return R.layout.pedo_welcome_set_avatar_layout
    }


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val btn = findViewById<View>(R.id.btn) as TextView
        btn.setOnClickListener { startActivity(Intent(this@SetAvatarActivity, SetBirthDayActivity::class.java)) }
        iv_set_avatar = findViewById<View>(R.id.iv_set_avatar) as SimpleDraweeView
        iv_set_avatar!!.setOnClickListener { showDialog() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHOTO_PICKED && resultCode == Activity.RESULT_OK) {
            val intent = Intent(this, PedoCropImageActivity::class.java)
            formatIntent(intent)
            intent.data = data!!.data
            startActivityForResult(intent, CropImage.CROP_PHOTO_FINISHED)

        } else if (requestCode == PHOTO_CROP && resultCode == Activity.RESULT_OK) {
            val intent = Intent(this, PedoCropImageActivity::class.java)
            formatIntent(intent)
            intent.data = Uri.fromFile(File(mFilePath))
            startActivityForResult(intent, CropImage.CROP_PHOTO_FINISHED)
        } else if (requestCode == CropImage.CROP_PHOTO_FINISHED) {
            val uri = "file:///sdcard/rc.jpg"
            val controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setTapToRetryEnabled(true)
                .build()
            iv_set_avatar!!.controller = controller
        }
    }


    protected fun formatIntent(intent: Intent) {
        val metric = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metric)
        val width = metric.widthPixels
        val height = metric.heightPixels
        CropImage.mScreenWidth = width
        CropImage.mScreenHeight = height
        intent.type = "image/*"
        intent.putExtra("crop", "true")
        intent.putExtra("outputX", width)
        intent.putExtra("outputY", height)
        intent.putExtra("aspectX", width)
        intent.putExtra("aspectY", height)
        intent.putExtra("scale", false)
        intent.putExtra("noFaceDetection", true)
        intent.putExtra("setWallpaper", false)
        val mTempFile = File(Environment.getExternalStorageDirectory(), "bg.jpg")
        val uri = Uri.fromFile(mTempFile)
        intent.putExtra("output", uri)
    }


    private fun showDialog() {
        AlertDialog.Builder(this@SetAvatarActivity)
            .setItems(R.array.select_image_dialog_items) { dialog, which ->
                if (which == 0) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val sdf = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
                    mFilePath = "/sdcard/DCIM/Camera/" + sdf.format(java.util.Date()) + ".jpg"
                    val myCaptureFile = File(mFilePath)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(myCaptureFile))
                    startActivityForResult(intent, PHOTO_CROP)
                } else {
                    val intent = Intent()
                    intent.type = "image/*"
                    intent.action = Intent.ACTION_PICK
                    startActivityForResult(intent, PHOTO_PICKED)
                }
            }
            .create().show()
    }

    companion object {
        private val PHOTO_CROP = 2
        private val PHOTO_PICKED = 1
    }


}

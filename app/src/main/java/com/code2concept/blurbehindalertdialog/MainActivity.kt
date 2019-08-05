package com.code2concept.blurbehindalertdialog

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView

import com.code2concept.blurbehindalertdialog.utils.AppUtils
import com.code2concept.blurbehindalertdialog.views.BlurView

class MainActivity : Activity() {

    private var alert: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.blurButton).setOnClickListener {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("coderconsole.com")
            builder.setMessage("BlurBackground of dialog using renderscript api. \nClick Ok to Exit ")
            builder.setPositiveButton(getString(android.R.string.ok)) { dialog, which -> dialog.cancel() }

            alert = builder.create()

            alert?.findViewById<TextView>(android.R.id.title)?.gravity = Gravity.CENTER


            Thread(Runnable {
                AppUtils.screenShot(this){bitmap ->
                    val blurBitmap: Bitmap? = BlurView(application).blurBackground(bitmap, 10)
                    runOnUiThread {
                        setAlertDialogBackground(blurBitmap)
                    }
                }
            }).start()


        }
    }
    private fun setAlertDialogBackground(result: Bitmap?) {
        val draw = BitmapDrawable(resources, result)
        val window = alert?.window
        window?.setBackgroundDrawable(draw)
        window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER)
        alert?.show()

        val textView = alert?.findViewById<TextView>(android.R.id.title)
        textView?.gravity = Gravity.CENTER
    }

}

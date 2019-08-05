package com.code2concept.blurbehindalertdialog.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.support.annotation.RequiresApi
import android.view.PixelCopy

/**
 * AppUtils helps to take screenshot of the application
 */
class AppUtils {



    companion object {


        /**
         * Take the screenshot of the view. Its deprecated after API 27. Use
         * @see getBitmapFromView
         * @param activity
         * @param callback as Bitmap
         */
        private fun takeScreenShot(activity: Activity, callback: (Bitmap) -> Unit) {
            val view = activity.window.decorView
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache()


            val b1 = view.drawingCache
            val frame = Rect()
            activity.window.decorView.getWindowVisibleDisplayFrame(frame)
            val statusBarHeight = frame.top

            val display = activity.windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            val width = size.x
            val height = size.y


            val b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight)
            view.destroyDrawingCache()

            callback(b)
        }


        /**
         * Take screenshot using PixelCopy api for android Oreo and above
         * @param view
         * @param activity
         * @param callback as Bitmap
         */
        @RequiresApi(Build.VERSION_CODES.O)
        private fun takeScreenShotOreo(activity: Activity, callback: (Bitmap) -> Unit) {
            activity.window?.let { window ->

                val view = activity.window.decorView

                val frame = Rect()
                activity.window.decorView.getWindowVisibleDisplayFrame(frame)
                val statusBarHeight = frame.top

                val bitmap = Bitmap.createBitmap(view.width, view.height - statusBarHeight, Bitmap.Config.ARGB_8888)
                val locationOfViewInWindow = IntArray(2)
                view.getLocationInWindow(locationOfViewInWindow)
                try {
                    PixelCopy.request(window, Rect(locationOfViewInWindow[0], locationOfViewInWindow[1], locationOfViewInWindow[0] + view.width, locationOfViewInWindow[1] + view.height), bitmap, { copyResult ->
                        if (copyResult == PixelCopy.SUCCESS) {
                            callback(bitmap)
                        }
                    }, Handler(activity.mainLooper))
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                }
            }
        }


        fun screenShot(activity: Activity, callback: (Bitmap) -> Unit) =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) takeScreenShotOreo(activity, callback)
                else takeScreenShot(activity, callback)

    }
}

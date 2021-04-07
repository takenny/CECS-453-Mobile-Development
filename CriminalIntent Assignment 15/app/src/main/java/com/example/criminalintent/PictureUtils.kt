package com.example.criminalintent

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point

fun KTgetScaledBitmap(path: String, activity: Activity): Bitmap {
    val KTsize = Point()
    activity.windowManager.defaultDisplay.getSize(KTsize)
    return KTgetScaledBitmap(path, KTsize.x, KTsize.y)
}

fun KTgetScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {
// Read in the dimensions of the image on disk
    var KToptions = BitmapFactory.Options()
    KToptions.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, KToptions)
    val KTsrcWidth = KToptions.outWidth.toFloat()
    val KTsrcHeight = KToptions.outHeight.toFloat()
// Figure out how much to scale down by
    var KTinSampleSize = 1
    if (KTsrcHeight > destHeight || KTsrcWidth > destWidth) {
        val KTheightScale = KTsrcHeight / destHeight
        val KTwidthScale = KTsrcWidth / destWidth
        val KTsampleScale = if (KTheightScale > KTwidthScale) {
            KTheightScale
        } else {
            KTwidthScale
        }
        KTinSampleSize = Math.round(KTsampleScale)
    }
    KToptions = BitmapFactory.Options()
    KToptions.inSampleSize = KTinSampleSize
// Read in and create final bitmap
    return BitmapFactory.decodeFile(path, KToptions)
}
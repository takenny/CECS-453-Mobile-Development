package com.bignerdranch.android.photogallery

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

const val KTNOTIFICATION_CHANNEL_ID = "flickr_poll"
class PhotoGalleryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val KTname = getString(R.string.notification_channel_name)
            val KTimportance = NotificationManager.IMPORTANCE_DEFAULT
            val KTchannel =
                NotificationChannel(KTNOTIFICATION_CHANNEL_ID, KTname, KTimportance)
            val KTnotificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            KTnotificationManager.createNotificationChannel(KTchannel)
        }
    }
}
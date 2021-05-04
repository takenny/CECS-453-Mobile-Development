package com.bignerdranch.android.photogallery

import android.app.Activity
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat

private const val TAG = "NotificationReceiver"
class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "received result: $resultCode")
        if (resultCode != Activity.RESULT_OK) {
        // A foreground activity canceled the broadcast
            return
        }
        val KTrequestCode = intent.getIntExtra(KTPollWorker.KTREQUEST_CODE, 0)
        val KTnotification: Notification =
            intent.getParcelableExtra(KTPollWorker.KTNOTIFICATION)!!
        val KTnotificationManager = NotificationManagerCompat.from(context)
        KTnotificationManager.notify(KTrequestCode, KTnotification)
    }

}
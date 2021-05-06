package com.bignerdranch.android.photogallery

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

private const val TAG = "PollWorker"
class KTPollWorker(val KTcontext: Context, workerParams: WorkerParameters)
    : Worker(KTcontext, workerParams) {
    override fun doWork(): Result {
        val KTquery = QueryPreferences.getStoredQuery(KTcontext)
        val KTlastResultId = QueryPreferences.getLastResultId(KTcontext)
        val KTitems: List<GalleryItem> = if (KTquery.isEmpty()) {
            FlickrFetchr().fetchPhotosRequest()
                .execute()
                .body()
                ?.photos
                ?.KTgalleryItems
        } else {
            FlickrFetchr().searchPhotosRequest(KTquery)
                .execute()
                .body()
                ?.photos
                ?.KTgalleryItems
        } ?: emptyList()

        if (KTitems.isEmpty()) {
            return Result.success()
        }
        val KTresultId = KTitems.first().id
        if (KTresultId == KTlastResultId) {
            Log.i(TAG, "Got an old result: $KTresultId")
        } else {
            Log.i(TAG, "Got a new result: $KTresultId")
            QueryPreferences.setLastResultId(KTcontext, KTresultId)
            val KTintent = PhotoGalleryActivity.KTnewIntent(KTcontext)
            val KTpendingIntent = PendingIntent.getActivity(KTcontext, 0, KTintent, 0)
            val KTresources = KTcontext.resources
            val KTnotification = NotificationCompat
                    .Builder(KTcontext, KTNOTIFICATION_CHANNEL_ID)
                    .setTicker(KTresources.getString(R.string.new_pictures_title))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(KTresources.getString(R.string.new_pictures_title))
                    .setContentText(KTresources.getString(R.string.new_pictures_text))
                    .setContentIntent(KTpendingIntent)
                    .setAutoCancel(true)
                    .build()
            val KTnotificationManager = NotificationManagerCompat.from(KTcontext)
            KTnotificationManager.notify(0, KTnotification)
            //showBackgroundNotification(0, KTnotification)
        }


        return Result.success()
    }

    private fun showBackgroundNotification(
        requestCode: Int,
        notification: Notification
    ) {
        val KTintent = Intent(KTACTION_SHOW_NOTIFICATION).apply {
            putExtra(KTREQUEST_CODE, requestCode)
            putExtra(KTNOTIFICATION, notification)
        }
        KTcontext.sendOrderedBroadcast(KTintent, KTPERM_PRIVATE)
    }

    companion object {
        const val KTACTION_SHOW_NOTIFICATION =
                "com.bignerdranch.android.photogallery.SHOW_NOTIFICATION"
        const val KTPERM_PRIVATE = "com.bignerdranch.android.photogallery.PRIVATE"
        const val KTREQUEST_CODE = "REQUEST_CODE"
        const val KTNOTIFICATION = "NOTIFICATION"
    }

}
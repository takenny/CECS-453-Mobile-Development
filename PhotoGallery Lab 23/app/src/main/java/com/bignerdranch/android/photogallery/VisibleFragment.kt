package com.bignerdranch.android.photogallery

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment

private const val TAG = "VisibleFragment"

abstract class VisibleFragment : Fragment() {
    private val KTonShowNotification = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // If we receive this, we're visible, so cancel
            // the notification
            Log.i(TAG, "canceling notification")
            resultCode = Activity.RESULT_CANCELED
        }
    }
    override fun onStart() {
        super.onStart()
        val KTfilter = IntentFilter(KTPollWorker.KTACTION_SHOW_NOTIFICATION)
        requireActivity().registerReceiver(
            KTonShowNotification,
            KTfilter,
            KTPollWorker.KTPERM_PRIVATE,
            null
        )
    }
    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(KTonShowNotification)
    }
}
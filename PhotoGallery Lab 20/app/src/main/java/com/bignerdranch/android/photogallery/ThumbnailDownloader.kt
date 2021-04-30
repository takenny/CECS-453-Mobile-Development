package com.bignerdranch.android.photogallery

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.util.concurrent.ConcurrentHashMap

private const val TAG = "ThumbnailDownloader"
private const val MESSAGE_DOWNLOAD = 0

class ThumbnailDownloader<in T> (
    private val KTresponseHandler: Handler,
    private val KTonThumbnailDownloaded: (T, Bitmap) -> Unit
): HandlerThread(TAG){

    val KTfragmentLifecycleObserver: LifecycleObserver =
        object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun setup() {
                Log.i(TAG, "Starting background thread")
                start()
                looper
            }
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun tearDown() {
                Log.i(TAG, "Destroying background thread")
                quit()
            }
        }

    val KTviewLifecycleObserver: LifecycleObserver =
        object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun clearQueue() {
                Log.i(TAG, "Clearing all requests from queue")
                KTrequestHandler.removeMessages(MESSAGE_DOWNLOAD)
                KTrequestMap.clear()
            }
        }

    private var KThasQuit = false
    private lateinit var KTrequestHandler: Handler
    private val KTrequestMap = ConcurrentHashMap<T, String>()
    private val KTflickrFetchr = FlickrFetchr()

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("HandlerLeak")
    override fun onLooperPrepared() {
        KTrequestHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    val target = msg.obj as T
                    Log.i(TAG, "Got a request for URL: ${KTrequestMap[target]}")
                    handleRequest(target)
                }
            }
        }
    }

    override fun quit(): Boolean {
        KThasQuit = true
        return super.quit()
    }

    fun queueThumbnail(target: T, url: String) {
        Log.i(TAG, "Got a URL: $url")
        KTrequestMap[target] = url
        KTrequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target)
            .sendToTarget()
    }

    private fun handleRequest(target: T) {
        val KTurl = KTrequestMap[target] ?: return
        val KTbitmap = KTflickrFetchr.fetchPhoto(KTurl) ?: return

        KTresponseHandler.post(Runnable {
            if (KTrequestMap[target] != KTurl || KThasQuit) {
                return@Runnable
            }
            KTrequestMap.remove(target)
            KTonThumbnailDownloaded(target, KTbitmap)
        })
    }
}
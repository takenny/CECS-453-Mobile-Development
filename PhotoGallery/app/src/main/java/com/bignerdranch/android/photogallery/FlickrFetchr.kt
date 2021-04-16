package com.bignerdranch.android.photogallery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.photogallery.api.FlickrApi
import com.bignerdranch.android.photogallery.api.FlickrResponse
import com.bignerdranch.android.photogallery.api.PhotoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "FlickrFetchr"
class FlickrFetchr {
    private val KTflickrApi: FlickrApi
    init {
        val KTretrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        KTflickrApi = KTretrofit.create(FlickrApi::class.java)
    }

    fun fetchPhotos(): LiveData<List<GalleryItem>> {
        val KTresponseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()
        val KTflickrRequest: Call<FlickrResponse> = KTflickrApi.fetchPhotos()
        KTflickrRequest.enqueue(object : Callback<FlickrResponse> {
            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }
            override fun onResponse(
                call: Call<FlickrResponse>,
                response: Response<FlickrResponse>
            ) {
                Log.d(TAG, "Response received")
                val KTflickrResponse: FlickrResponse? = response.body()
                val KTphotoResponse: PhotoResponse? = KTflickrResponse?.photos
                var KTgalleryItems: List<GalleryItem> = KTphotoResponse?.KTgalleryItems
                    ?: mutableListOf()
                KTgalleryItems = KTgalleryItems.filterNot {
                    it.url.isBlank()
                }
                KTresponseLiveData.value = KTgalleryItems
            }
        })
        return KTresponseLiveData
    }
}

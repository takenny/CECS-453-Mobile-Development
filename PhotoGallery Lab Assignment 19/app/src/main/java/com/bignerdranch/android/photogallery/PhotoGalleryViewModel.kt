package com.bignerdranch.android.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class PhotoGalleryViewModel : ViewModel() {
    val KTgalleryItemLiveData: LiveData<List<GalleryItem>>
    init {
        KTgalleryItemLiveData = FlickrFetchr().fetchPhotos()
    }
}
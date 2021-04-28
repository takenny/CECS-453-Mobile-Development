package com.bignerdranch.android.photogallery

import android.app.Application
import androidx.lifecycle.*

class PhotoGalleryViewModel(private val KTapp: Application) : AndroidViewModel(KTapp) {
    val KTgalleryItemLiveData: LiveData<List<GalleryItem>>

    private val KTflickrFetchr = FlickrFetchr()
    private val KTmutableSearchTerm = MutableLiveData<String>()
    val KTsearchTerm: String
        get() = KTmutableSearchTerm.value ?: ""
    init {
        KTmutableSearchTerm.value = QueryPreferences.getStoredQuery(KTapp)
        KTgalleryItemLiveData = Transformations.switchMap(KTmutableSearchTerm) { KTsearchTerm ->
            if (KTsearchTerm.isBlank()) {
                KTflickrFetchr.fetchPhotos()
            } else {
                KTflickrFetchr.searchPhotos(KTsearchTerm)
            }
        }
    }

    fun fetchPhotos(query: String = "") {
        QueryPreferences.setStoredQuery(KTapp, query)
        KTmutableSearchTerm.value = query
    }

}
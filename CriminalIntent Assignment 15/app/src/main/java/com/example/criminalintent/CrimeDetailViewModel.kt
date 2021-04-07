package com.example.criminalintent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.io.File
import java.util.*

class KTCrimeDetailViewModel() : ViewModel() {
    private val KTcrimeRepository = KTCrimeRepository.get()
    private val KTcrimeIdLiveData = MutableLiveData<UUID>()
    var KTcrimeLiveData: LiveData<Crime?> =
        Transformations.switchMap(KTcrimeIdLiveData) { KTcrimeId ->
            KTcrimeRepository.getCrime(KTcrimeId)
        }

    fun KTloadCrime(KTcrimeId: UUID) {
        KTcrimeIdLiveData.value = KTcrimeId
    }
    fun KTsaveCrime(crime: Crime) {
        KTcrimeRepository.KTupdateCrime(crime)
    }
    fun KTgetPhotoFile(crime: Crime): File {
        return KTcrimeRepository.KTgetPhotoFile(crime)
    }
}

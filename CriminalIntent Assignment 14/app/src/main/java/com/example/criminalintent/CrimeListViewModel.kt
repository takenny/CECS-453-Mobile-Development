package com.example.criminalintent

import androidx.lifecycle.ViewModel

class KTCrimeListViewModel : ViewModel() {
    private val KTcrimeRepository = KTCrimeRepository.get()
    val KTcrimeListLiveData = KTcrimeRepository.getCrimes()

    fun KTaddCrime(crime: Crime) {
        KTcrimeRepository.KTaddCrime(crime)
    }
}
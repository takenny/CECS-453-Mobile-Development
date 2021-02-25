package com.example.criminalintent

import androidx.lifecycle.ViewModel

class KTCrimeListViewModel : ViewModel() {
    val KTcrimes = mutableListOf<Crime>()
    init {
        for (i in 0 until 100) {
            val KTcrime = Crime()
            KTcrime.title = "Crime #$i"
            KTcrime.isSolved = i % 2 == 0
            KTcrimes += KTcrime
        }
    }
}
package com.example.criminalintent

import android.app.Application

class KTCriminalIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KTCrimeRepository.initialize(this)
    }
}

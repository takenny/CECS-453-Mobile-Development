package com.example.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(),
    KTCrimeListFragment.KTCallbacks{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val KTcurrentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (KTcurrentFragment == null) {
            val KTfragment = KTCrimeListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, KTfragment)
                .commit()
        }

    }

    override fun KTonCrimeSelected(KTcrimeId: UUID) {
        val KTfragment = CrimeFragment.newInstance(KTcrimeId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, KTfragment)
            .addToBackStack(null)
            .commit()
    }
}
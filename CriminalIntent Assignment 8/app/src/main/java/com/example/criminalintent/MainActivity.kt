package com.example.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
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
}
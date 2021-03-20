package com.example.criminalintent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.criminalintent.Crime

@Database(entities = [ Crime::class ], version=1, exportSchema = false)
@TypeConverters(KTCrimeTypeConverters::class)
abstract class KTCrimeDatabase : RoomDatabase() {
    abstract fun crimeDao(): KTCrimeDao
}
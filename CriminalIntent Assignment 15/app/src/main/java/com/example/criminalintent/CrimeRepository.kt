package com.example.criminalintent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.criminalintent.database.KTCrimeDatabase
import com.example.criminalintent.database.migration_1_2
import java.io.File
import java.util.*
import java.util.concurrent.Executors

private const val KTDATABASE_NAME = "crime-database"

class KTCrimeRepository private constructor(context: Context) {
    private val database : KTCrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        KTCrimeDatabase::class.java,
        KTDATABASE_NAME
    ).addMigrations(migration_1_2)
            .build()
    private val KTcrimeDao = database.crimeDao()
    private val KTexecutor = Executors.newSingleThreadExecutor()
    private val KTfilesDir = context.applicationContext.filesDir

    fun getCrimes(): LiveData<List<Crime>> = KTcrimeDao.getCrimes()
    fun getCrime(id: UUID): LiveData<Crime?> = KTcrimeDao.getCrime(id)

    fun KTupdateCrime(crime: Crime) {
        KTexecutor.execute {
            KTcrimeDao.updateCrime(crime)
        }
    }
    fun KTaddCrime(crime: Crime) {
        KTexecutor.execute {
            KTcrimeDao.addCrime(crime)
        }
    }

    fun KTgetPhotoFile(crime: Crime): File = File(KTfilesDir, crime.photoFileName)

    companion object {
        private var INSTANCE: KTCrimeRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = KTCrimeRepository(context)
            }
        }
        fun get(): KTCrimeRepository {
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}
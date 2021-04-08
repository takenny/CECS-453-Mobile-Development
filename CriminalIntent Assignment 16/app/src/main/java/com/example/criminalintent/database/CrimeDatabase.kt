package com.example.criminalintent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.criminalintent.Crime

@Database(entities = [ Crime::class ], version=2)
@TypeConverters(KTCrimeTypeConverters::class)
abstract class KTCrimeDatabase : RoomDatabase() {
    abstract fun crimeDao(): KTCrimeDao
}

val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
                "ALTER TABLE Crime ADD COLUMN suspect TEXT NOT NULL DEFAULT ''"
        )
    }
}

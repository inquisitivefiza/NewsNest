package com.example.newsnest.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add the missing 'category' column with a default value of 'general'
        database.execSQL(
            "ALTER TABLE articles ADD COLUMN category TEXT NOT NULL DEFAULT 'general'"
        )
    }
}

@Database(
    entities = [ArticleEntity::class],
    version = 2,                  // bumped from 1 → 2
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "newsnest_db"
                )
                    .addMigrations(MIGRATION_1_2)   // register migration
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
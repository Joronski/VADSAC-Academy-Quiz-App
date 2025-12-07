package com.example.finalscasestudy.data.local

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "quiz_app_db"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}

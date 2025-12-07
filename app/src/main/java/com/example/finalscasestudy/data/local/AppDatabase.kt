package com.example.finalscasestudy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.finalscasestudy.data.local.dao.QuizAttemptDao
import com.example.finalscasestudy.data.local.dao.QuizDao
import com.example.finalscasestudy.data.local.dao.UserDao
import com.example.finalscasestudy.data.local.entities.Quiz
import com.example.finalscasestudy.data.local.entities.QuizAttempt
import com.example.finalscasestudy.data.local.entities.User

@Database(entities = [User::class, Quiz::class, QuizAttempt::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun quizDao(): QuizDao
    abstract fun quizAttemptDao(): QuizAttemptDao
}

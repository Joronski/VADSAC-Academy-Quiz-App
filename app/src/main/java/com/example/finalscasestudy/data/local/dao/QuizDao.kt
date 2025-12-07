package com.example.finalscasestudy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.finalscasestudy.data.local.entities.Quiz

@Dao
interface QuizDao {

    @Query("SELECT * FROM quiz_table WHERE category = :category AND difficulty = :difficulty LIMIT 1")
    suspend fun getQuiz(category: String, difficulty: String): Quiz?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: Quiz)
}

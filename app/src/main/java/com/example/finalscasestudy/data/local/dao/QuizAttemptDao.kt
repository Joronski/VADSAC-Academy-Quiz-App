package com.example.finalscasestudy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.finalscasestudy.data.local.entities.QuizAttempt

@Dao
interface QuizAttemptDao {

    @Insert
    suspend fun insertAttempt(attempt: QuizAttempt)

    @Update
    suspend fun updateAttempt(attempt: QuizAttempt)

    @Query("SELECT * FROM quiz_attempt_table WHERE userEmail = :userEmail AND category = :category AND difficulty = :difficulty LIMIT 1")
    suspend fun getAttempt(userEmail: String, category: String, difficulty: String): QuizAttempt?

    @Query("SELECT * FROM quiz_attempt_table WHERE userEmail = :userEmail ORDER BY id DESC")
    suspend fun getAllAttemptsForUser(userEmail: String): List<QuizAttempt>
}

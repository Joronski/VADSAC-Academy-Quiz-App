package com.example.finalscasestudy.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_attempt_table")
data class QuizAttempt(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userEmail: String,  // use email as unique identifier
    val category: String,
    val difficulty: String,
    val score: Int,
    val totalQuestions: Int,
    val correct: Int,
    val wrong: Int,
    val missed: Int
)

package com.example.finalscasestudy.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_table")
data class Quiz(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val difficulty: String,
    val questionsJson: String
)

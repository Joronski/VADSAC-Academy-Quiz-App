package com.example.finalscasestudy.data.local.entities

data class QuizQuestion(
    val questionText: String,
    val answers: List<String?>,
    val correctIndex: Int
)

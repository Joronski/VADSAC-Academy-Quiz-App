package com.example.finalscasestudy.data.repository

import com.example.finalscasestudy.data.local.dao.QuizAttemptDao
import com.example.finalscasestudy.data.local.entities.QuizAttempt

class QuizAttemptRepository(private val dao: QuizAttemptDao) {
    suspend fun insertAttempt(attempt: QuizAttempt) {
        dao.insertAttempt(attempt)
    }
    suspend fun updateAttempt(attempt: QuizAttempt) {
        dao.updateAttempt(attempt)
    }

    suspend fun getAttempt(userEmail: String, category: String, difficulty: String): QuizAttempt? {
        return dao.getAttempt(userEmail, category, difficulty)
    }
    suspend fun getAllAttemptsForUser(userEmail: String): List<QuizAttempt> {
        return dao.getAllAttemptsForUser(userEmail)
    }
}

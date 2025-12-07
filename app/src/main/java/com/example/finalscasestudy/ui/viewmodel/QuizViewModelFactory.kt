package com.example.finalscasestudy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalscasestudy.data.repository.QuizAttemptRepository
import com.example.finalscasestudy.data.repository.QuizRepository

class QuizViewModelFactory(
    private val repository: QuizRepository,
    private val quizAttemptRepository: QuizAttemptRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizViewModel(repository, quizAttemptRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

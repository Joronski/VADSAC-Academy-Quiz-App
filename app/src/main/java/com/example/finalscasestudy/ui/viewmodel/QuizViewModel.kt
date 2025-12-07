package com.example.finalscasestudy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalscasestudy.data.local.entities.Quiz
import com.example.finalscasestudy.data.local.entities.QuizAttempt
import com.example.finalscasestudy.data.local.entities.QuizQuestion
import com.example.finalscasestudy.data.repository.QuizRepository
import com.example.finalscasestudy.data.repository.QuizAttemptRepository
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuizViewModel(
    private val repository: QuizRepository,
    private val attemptRepository: QuizAttemptRepository,
    private val gson: Gson = Gson()
) : ViewModel() {

    private val _quiz = MutableStateFlow<Quiz?>(null)
    val quiz: StateFlow<Quiz?> = _quiz

    private val _questions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val questions: StateFlow<List<QuizQuestion>> = _questions

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _timer = MutableStateFlow(60)
    val timer: StateFlow<Int> = _timer

    private val _completed = MutableStateFlow(false)
    val completed: StateFlow<Boolean> = _completed

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val userAnswers = mutableMapOf<Int, Int?>()
    private var timerJob: Job? = null

    val allUserAnswers: Map<Int, Int?> get() = userAnswers

    private var currentUserEmail: String? = null

    // Quiz Attempts state
    private val _quizAttempts = MutableStateFlow<List<QuizAttempt>>(emptyList())
    val quizAttempts: StateFlow<List<QuizAttempt>> = _quizAttempts

    /** Load a quiz for a user, stores the email internally */
    fun loadQuiz(category: String, difficulty: String, userEmail: String) {
        resetQuiz()
        currentUserEmail = userEmail

        viewModelScope.launch {
            try {
                val apiDifficulty = when (difficulty.lowercase()) {
                    "easy" -> "Easy"
                    "moderate", "medium" -> "Medium"
                    "hard" -> "Hard"
                    else -> "Easy"
                }

                val quizEntity = repository.getOrCreateQuiz(category, apiDifficulty)
                _quiz.value = quizEntity

                val questionsList =
                    gson.fromJson(quizEntity.questionsJson, Array<QuizQuestion>::class.java).toList()

                if (questionsList.isEmpty()) {
                    throw Exception("No questions available for $category / $apiDifficulty")
                }

                _questions.value = questionsList
                _currentIndex.value = 0

                startTimer()
            } catch (e: Exception) {
                _error.value = "Failed to load quiz: ${e.message ?: "Unknown error"}"
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            _timer.value = 60
            while (_timer.value > 0) {
                delay(1000)
                _timer.value -= 1
            }
            autoSubmitCurrentAnswer()
        }
    }

    fun selectAnswer(answerIndex: Int) {
        userAnswers[_currentIndex.value] = answerIndex
    }

    fun submitCurrentAnswer() {
        autoSubmitCurrentAnswer()
    }

    /** Automatically moves to the next question or finalizes the quiz */
    private fun autoSubmitCurrentAnswer() {
        if (_currentIndex.value < _questions.value.size - 1) {
            _currentIndex.value += 1
            startTimer()
        } else {
            currentUserEmail?.let { finalizeQuiz(it) }
        }
    }

    /** Calculates score and inserts or updates attempt in DB */
    private fun finalizeQuiz(userEmail: String) {
        timerJob?.cancel()

        val questionsList = _questions.value
        val total = questionsList.size
        var correct = 0
        var wrong = 0
        var missed = 0

        questionsList.forEachIndexed { index, question ->
            when (val answer = userAnswers[index]) {
                null -> missed++
                question.correctIndex -> correct++
                else -> wrong++
            }
        }

        val score = correct
        val category = _quiz.value?.category ?: ""
        val difficulty = _quiz.value?.difficulty ?: ""

        viewModelScope.launch {
            val existingAttempt = attemptRepository.getAttempt(userEmail, category, difficulty)
            if (existingAttempt != null) {
                attemptRepository.updateAttempt(
                    existingAttempt.copy(
                        score = score,
                        totalQuestions = total,
                        correct = correct,
                        wrong = wrong,
                        missed = missed
                    )
                )
            } else {
                attemptRepository.insertAttempt(
                    QuizAttempt(
                        userEmail = userEmail,
                        category = category,
                        difficulty = difficulty,
                        score = score,
                        totalQuestions = total,
                        correct = correct,
                        wrong = wrong,
                        missed = missed
                    )
                )
            }
            _completed.value = true
        }
    }

    fun resetQuiz() {
        timerJob?.cancel()
        _quiz.value = null
        _questions.value = emptyList()
        _currentIndex.value = 0
        _timer.value = 60
        _completed.value = false
        _error.value = null
        userAnswers.clear()
        currentUserEmail = null
    }

    fun resetError() {
        _error.value = null
    }

    fun getSelectedAnswerIndexForCurrentQuestion(): Int? = userAnswers[_currentIndex.value]

    fun getSelectedAnswerIndexForCurrentQuestionAt(index: Int): Int? = userAnswers[index]

    /** Load all quiz attempts for the current user */
    fun loadQuizAttempts(userEmail: String) {
        viewModelScope.launch {
            val attempts = attemptRepository.getAllAttemptsForUser(userEmail)
            _quizAttempts.value = attempts
        }
    }
}

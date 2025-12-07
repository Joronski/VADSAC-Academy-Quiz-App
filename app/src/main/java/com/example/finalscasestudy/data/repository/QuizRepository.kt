package com.example.finalscasestudy.data.repository

import com.example.finalscasestudy.data.local.dao.QuizDao
import com.example.finalscasestudy.data.local.entities.Quiz
import com.example.finalscasestudy.data.local.entities.QuizQuestion
import com.example.finalscasestudy.data.remote.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuizRepository(
    private val quizDao: QuizDao,
    private val gson: Gson = Gson()
) {

    private val API_KEY = "IpZiC6LJOY11KnWT4zbW1l56hRSupenHDhaX5NX0"

    suspend fun getOrCreateQuiz(category: String, difficulty: String): Quiz {
        return withContext(Dispatchers.IO) {

            // Try loading from Room first
            val cachedQuiz = quizDao.getQuiz(category, difficulty)
            if (cachedQuiz != null) return@withContext cachedQuiz

            // Map app difficulty to API expected difficulty
            val apiDifficulty = when (difficulty.lowercase()) {
                "easy" -> "Easy"
                "moderate", "medium" -> "Medium"
                "hard" -> "Hard"
                else -> "Easy"
            }

            // Fetch from API
            val apiQuestions = RetrofitClient.api.getQuestions(
                apiKey = API_KEY,
                category = category,
                difficulty = apiDifficulty,
                limit = 10
            )

            // Handle empty results
            if (apiQuestions.isEmpty()) {
                throw Exception("No questions returned by API for $category / $apiDifficulty")
            }

            val convertedQuestions = apiQuestions.map { q ->
                val answers = listOfNotNull(
                    q.answers?.answer_a,
                    q.answers?.answer_b,
                    q.answers?.answer_c,
                    q.answers?.answer_d,
                    q.answers?.answer_e,
                    q.answers?.answer_f
                )

                val correctIndex = q.correct_answers?.let { correct ->
                    listOf(
                        correct.answer_a_correct,
                        correct.answer_b_correct,
                        correct.answer_c_correct,
                        correct.answer_d_correct,
                        correct.answer_e_correct,
                        correct.answer_f_correct
                    ).indexOfFirst { it == "true" }
                } ?: -1

                QuizQuestion(
                    questionText = q.question ?: "",
                    answers = answers,
                    correctIndex = correctIndex
                )
            }

            val json = gson.toJson(convertedQuestions)

            val quizEntity = Quiz(
                category = category,
                difficulty = difficulty,
                questionsJson = json
            )

            quizDao.insertQuiz(quizEntity)
            quizEntity
        }
    }
}

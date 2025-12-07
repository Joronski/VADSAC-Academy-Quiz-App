package com.example.finalscasestudy.data.remote

import com.example.finalscasestudy.data.remote.dto.ApiQuestionDto
import retrofit2.http.GET
import retrofit2.http.Query

interface QuizApiService {

    @GET("questions")
    suspend fun getQuestions(
        @Query("apiKey") apiKey: String,
        @Query("category") category: String,
        @Query("difficulty") difficulty: String,
        @Query("limit") limit: Int = 10
    ): List<ApiQuestionDto>
}

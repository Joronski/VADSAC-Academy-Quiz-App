package com.example.finalscasestudy.data.remote.dto

data class ApiQuestionDto(
    val id: Int?,
    val question: String?,
    val description: String?,
    val answers: AnswersDto?,
    val multiple_correct_answers: String?,
    val correct_answers: CorrectAnswersDto?,
    val explanation: String?,
    val tip: String?,
    val tags: List<TagDto>?,
    val category: String?,
    val difficulty: String?
)

data class AnswersDto(
    val answer_a: String?,
    val answer_b: String?,
    val answer_c: String?,
    val answer_d: String?,
    val answer_e: String?,
    val answer_f: String?
)

data class CorrectAnswersDto(
    val answer_a_correct: String?,
    val answer_b_correct: String?,
    val answer_c_correct: String?,
    val answer_d_correct: String?,
    val answer_e_correct: String?,
    val answer_f_correct: String?
)

data class TagDto(
    val name: String?
)

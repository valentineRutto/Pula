package com.example.pula.data.model


import com.google.gson.annotations.SerializedName

data class SurveyResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("questions")
    val questions: List<Question?>?,
    @SerializedName("start_question_id")
    val startQuestionId: String?
) {
    data class Question(
        @SerializedName("answer_type")
        val answerType: String?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("next")
        val next: Any?,
        @SerializedName("options")
        val options: List<Option?>?,
        @SerializedName("question_text")
        val questionText: String?,
        @SerializedName("question_type")
        val questionType: String?
    ) {
        data class Option(
            @SerializedName("display_text")
            val displayText: String?,
            @SerializedName("value")
            val value: String?
        )
    }
}